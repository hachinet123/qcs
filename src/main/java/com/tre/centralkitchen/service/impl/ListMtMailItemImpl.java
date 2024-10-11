package com.tre.centralkitchen.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.CustomPageData;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.ListMtMailItemBo;
import com.tre.centralkitchen.domain.vo.system.ListMtMailItemVo;
import com.tre.centralkitchen.mapper.ListMtMailItemMapper;
import com.tre.centralkitchen.mapper.ShTPcOrderListMapper;
import com.tre.centralkitchen.service.ListMtMailItemService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 商品別便
 */
@RequiredArgsConstructor
@Service
public class ListMtMailItemImpl implements ListMtMailItemService {
    private final ListMtMailItemMapper listMtMailItemMapper;
    private final ShTPcOrderListMapper shTPcOrderListMapper;
    @Value("${LinkSubIp.ip120}")
    private String ip120;


    private static void checkout(ListMtMailItemBo listMtMailItemBo) {
        String mailNoList = listMtMailItemBo.getMailNo();
        String linesList = listMtMailItemBo.getLineId();
        if (mailNoList != null && !mailNoList.isEmpty()) {
            List<String> list1 = Arrays.asList(mailNoList.split(","));
            listMtMailItemBo.setMailNoList(list1);
        }
        if (linesList != null && !linesList.isEmpty()) {
            List<String> list = Arrays.asList(linesList.split(","));
            listMtMailItemBo.setLinesList(list);
        }
    }

    @Override
    public void downloadCSV(ListMtMailItemBo bo, HttpServletResponse response) {
        if (bo.getCenterId() == null) {
            String fileName = SimpleCsvTableUtils.getFileName(MailConstants.LIST_MAIL_ITEM_CSV_NAME, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
            String[] header = SimpleCsvTableUtils.getHeaders(MailConstants.LIST_MAIL_ITEM_CSV_HEADER, StringConstants.COMMA);
            SimpleCsvTableUtils.printBeansToRespStream(response, fileName, new ArrayList<>(), ListMtMailItemVo.class, header);
        } else {
            PageQuery pageQuery1 = new PageQuery();
            checkout(bo);
            Page<ListMtMailItemVo> page = getListMtMailItemVoPage(bo, pageQuery1);
            SimpleCsvTableUtils.easyCsvExport(response, MailConstants.LIST_MAIL_ITEM_CSV_NAME, page.getRecords(), ListMtMailItemVo.class);
        }
    }

    @Override
    public TableDataInfo<ListMtMailItemVo> queryList(ListMtMailItemBo bo, PageQuery pageQuery) {
        checkout(bo);
        Page<ListMtMailItemVo> page = getListMtMailItemVoPage(bo, pageQuery);
        return TableDataInfo.build(page);
    }

    @NotNull
    private Page<ListMtMailItemVo> getListMtMailItemVoPage(ListMtMailItemBo bo, PageQuery pageQuery) {
        bo.setPageSize(pageQuery.getPageSize());
        bo.setPageNum(pageQuery.getPageNum());
        CustomPageData<ListMtMailItemVo> list = shTPcOrderListMapper.queryList(bo, ip120);
        Page<ListMtMailItemVo> page = new Page<>();
        page.setRecords(list.getRows());
        page.setTotal(list.getTotal());
        page.setPages(list.getPages());
        page.setCurrent(list.getCurrent());
        page.setSize(list.getSize());
        extracted(page);
        return page;
    }

    private void extracted(Page<ListMtMailItemVo> page) {
        List<String> basics = listMtMailItemMapper.selectBasics();
        List<String> cases = listMtMailItemMapper.selectCases();
        page.getRecords().forEach(a -> {
            if (cases.contains(a.getCases())) {
                a.setCsFlg("個別");
            } else if (basics.contains(a.getBasics())) {
                a.setCsFlg("基準");
            } else {
                a.setCsFlg("");
            }
        });
    }
}
