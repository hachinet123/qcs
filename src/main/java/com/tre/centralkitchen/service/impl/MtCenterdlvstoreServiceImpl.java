package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.CenterdlvstoreBo;
import com.tre.centralkitchen.domain.bo.system.MtCenterdlvstoreBo;
import com.tre.centralkitchen.domain.po.MtCenterdlvstore;
import com.tre.centralkitchen.domain.vo.system.CenterdlvstoreVo;
import com.tre.centralkitchen.domain.vo.system.MtCenterdlvstoreVo;
import com.tre.centralkitchen.mapper.MtCenterdlvstoreMapper;
import com.tre.centralkitchen.service.MtCenterdlvstoreService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MtCenterdlvstoreServiceImpl implements MtCenterdlvstoreService {

    private final MtCenterdlvstoreMapper mapper;


    @Override
    public TableDataInfo<MtCenterdlvstoreVo> search(MtCenterdlvstoreBo bo, PageQuery pageQuery) {

        Page<MtCenterdlvstoreVo> page = mapper.search(pageQuery.build(), bo);
        List<MtCenterdlvstoreVo> records = page.getRecords();
        for (MtCenterdlvstoreVo record : records) {
            String newOpenedDate = formatDate(record.getOpenedDate());
            record.setOpenedDate(newOpenedDate);
        }
        return TableDataInfo.build(page);
    }

    @Override
    public void downloadCSV(MtCenterdlvstoreBo bo, HttpServletResponse response) {
        PageQuery pageQuery = new PageQuery();
        Page<MtCenterdlvstoreVo> page = mapper.search(pageQuery.build(), bo);
        List<MtCenterdlvstoreVo> records = page.getRecords();
        for (MtCenterdlvstoreVo record : records) {
            String newOpenedDate = formatDate(record.getOpenedDate());
            record.setOpenedDate(newOpenedDate);
        }
        SimpleCsvTableUtils.easyCsvExport(response, MailConstants.MT_CENTERDLV_STORE_CSV_NAME, records, MtCenterdlvstoreVo.class);
    }

    private String formatDate(String openedDate) {
        DateTime dateTime = DateUtil.parse(openedDate, "yyyyMMdd");
        String newOpenedDate = DateUtil.format(dateTime, "yyyy/MM/dd");
        return newOpenedDate;
    }

    @Override
    public void save(CenterdlvstoreBo bo) {
        MtCenterdlvstore mtCenterdlvstore = BeanUtil.copyProperties(bo, MtCenterdlvstore.class);
        mapper.insert(mtCenterdlvstore);
    }

    @Override
    public CenterdlvstoreVo info(CenterdlvstoreBo centerdlvstoreBo) {
        CenterdlvstoreVo centerdlvstoreVo = mapper.centerdlvstoreSelect(centerdlvstoreBo);
        String reqDate = centerdlvstoreVo.getReqDate();
        String updDate = centerdlvstoreVo.getUpdDate();
        String newResDate = reqDate.substring(0, reqDate.lastIndexOf(":"));
        String newUpdDate = updDate.substring(0, updDate.lastIndexOf(":"));
        centerdlvstoreVo.setReqDate(newResDate);
        centerdlvstoreVo.setUpdDate(newUpdDate);
        return centerdlvstoreVo;
    }

    @Override
    public void update(CenterdlvstoreBo centerdlvstoreBo) {
        MtCenterdlvstore mtCenterdlvstore = BeanUtil.copyProperties(centerdlvstoreBo, MtCenterdlvstore.class);
        UpdateWrapper<MtCenterdlvstore> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(MtCenterdlvstore::getCenterId, mtCenterdlvstore.getCenterId())
                .eq(MtCenterdlvstore::getLineId, mtCenterdlvstore.getLineId())
                .eq(MtCenterdlvstore::getSeq, mtCenterdlvstore.getSeq());
        mapper.update(mtCenterdlvstore, wrapper);
    }

    @Override
    public void delete(CenterdlvstoreBo centerdlvstoreBo) {
        MtCenterdlvstore mtCenterdlvstore = BeanUtil.copyProperties(centerdlvstoreBo, MtCenterdlvstore.class);
        mtCenterdlvstore.setFDel(1);
        UpdateWrapper<MtCenterdlvstore> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(MtCenterdlvstore::getCenterId, mtCenterdlvstore.getCenterId())
                .eq(MtCenterdlvstore::getLineId, mtCenterdlvstore.getLineId())
                .eq(MtCenterdlvstore::getSeq, mtCenterdlvstore.getSeq());
        mapper.update(mtCenterdlvstore, wrapper);
    }
}
