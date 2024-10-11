package com.tre.centralkitchen.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.IssueCheckDataBo;
import com.tre.centralkitchen.domain.vo.system.IssueCheckDataVo;
import com.tre.centralkitchen.mapper.IssueCheckDataMapper;
import com.tre.centralkitchen.service.IssueCheckDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;

@RequiredArgsConstructor
@Service
public class IssueCheckDataServiceImpl implements IssueCheckDataService {
    private final IssueCheckDataMapper issueCheckDataMapper;

    @Override
    public TableDataInfo<IssueCheckDataVo> queryList(IssueCheckDataBo bo, PageQuery pageQuery) {
        checkout(bo);
        Page<IssueCheckDataVo> issueCheckDataVoPage = issueCheckDataMapper.queryList(bo, pageQuery.build());
        extractedViewData(issueCheckDataVoPage.getRecords());
        return TableDataInfo.build(issueCheckDataVoPage);
    }

    @Override
    public void downloadCSV(IssueCheckDataBo bo, HttpServletResponse response) {
        checkout(bo);
        List<IssueCheckDataVo> issueCheckDataVos = issueCheckDataMapper.queryList(bo);
        extractedViewData(issueCheckDataVos);
        SimpleCsvTableUtils.easyCsvExport(response, SysConstants.ISSUE_CHECK_DATA, issueCheckDataVos, IssueCheckDataVo.class);
    }

    private static void checkout(IssueCheckDataBo bo) {
        String linesList = bo.getLineId();
        String storeName = bo.getStoreId();
        if (linesList != null && !linesList.isEmpty()) {
            List<String> list = Arrays.asList(linesList.split(","));
            bo.setLineIdList(list);
        }
        if (storeName != null && !storeName.isEmpty()) {
            List<String> list = Arrays.asList(storeName.split(","));
            bo.setStoreIdList(list);
        }
    }

    private static void extractedViewData(List<IssueCheckDataVo> issueCheckDataVos) {
        issueCheckDataVos.forEach(a -> {
            if (a.getMailNo() == 20) {
                a.setDeliveryNumber(abs(a.getDeliveryNumber()));
            }
            if (a.getTeikanTypeid() == 0) {
                if (a.getWeightAm() == null || a.getWeightAm().compareTo(new BigDecimal("0")) == 0) {
                    a.setCostRcp(BigDecimal.valueOf(0));
                    a.setCost(BigDecimal.valueOf(0));
                    a.setPrice(0);
                } else {
                    a.setCostRcp(a.getCostRcpAm().multiply(BigDecimal.valueOf(100)).divide(a.getWeightAm(), 2, RoundingMode.HALF_UP));
                    a.setCost(a.getCostAm().multiply(BigDecimal.valueOf(100)).divide(a.getWeightAm(), 2, RoundingMode.HALF_UP));
                    a.setPrice(Integer.parseInt(a.getPriceAm().multiply(BigDecimal.valueOf(100)).divide(a.getWeightAm(), 0, RoundingMode.HALF_UP).toString()));
                }
            }
            if (a.getWeightAm() == null) {
                a.setWeightAm(new BigDecimal("0"));
            }
        });
    }
}
