package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.common.domain.CustomPageData;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.OrderStatusBo;
import com.tre.centralkitchen.domain.bo.system.ProducePlanBo;
import com.tre.centralkitchen.domain.dto.OrderStatusDto;
import com.tre.centralkitchen.domain.po.MtCenterstatus;
import com.tre.centralkitchen.domain.po.MtMailcontrol;
import com.tre.centralkitchen.domain.po.MtProductwkgrp;
import com.tre.centralkitchen.domain.po.WkCenterorder;
import com.tre.centralkitchen.domain.vo.system.*;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.IOrderStatusService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderStatusServiceImpl implements IOrderStatusService {
    private final OrderStatusMapper orderStatusMapper;
    private final MtMailcontrolMapper mtMailcontrolMapper;
    private final MtProductwkgrpMapper mtProductwkgrpMapper;
    private final WkCenterorderMapper wkCenterorderMapper;
    private final MtCenterstatusMapper mtCenterstatusMapper;
    @Value("${LinkSubIp.ip20}")
    private String ip20;
    @Value("${LinkSubIp.ip120}")
    private String ip120;

    public static String[] daysBetween(String startDate, String endDate) throws ParseException {
        DateTime sdateTime = DateUtil.parseDate(startDate);
        DateTime edateTime = DateUtil.parseDate(endDate);
        List<String> daysLi = ListUtils.newArrayList();
        DateUtil.rangeToList(sdateTime, edateTime, DateField.DAY_OF_YEAR).forEach(dateTime -> {
            String dateStr = DateUtil.format(dateTime, new SimpleDateFormat("yyyy/MM/dd"));
            daysLi.add(dateStr);
        });

        return daysLi.toArray(new String[0]);
    }

    @Override
    public CustomPageData getOrderStatusList(OrderStatusBo orderStatusBo, PageQuery pageQuery) {
        return getOrderStatusListFromIdc(orderStatusBo, pageQuery);
    }

    @SneakyThrows
    @Override
    public void downloadCsvSummary(OrderStatusBo orderStatusBo, HttpServletResponse response) {
        List<List<Object>> data = ListUtils.newArrayList();
        String[] date = daysBetween(orderStatusBo.getDeliveryStartDate(), orderStatusBo.getDeliveryEndDate());

        PageQuery pageQuery = new PageQuery();
        CustomPageData customPageData = getOrderStatusListFromIdc(orderStatusBo, pageQuery);

        String fileName = SimpleCsvTableUtils.getFileName("PC受注_受注状況", "yyyyMMddHHmmss");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=" + fileName + ".csv");
        switch (orderStatusBo.getDisplayFormat()) {
            case 1:
                List<OrderStatusVo> recordsJan = customPageData.getRows();
                Map<String, List<OrderStatusVo>> itemMapJan = recordsJan.stream().collect(Collectors.groupingBy(OrderStatusVo::getJan, LinkedHashMap::new, Collectors.toList()));
                convert(data, date, itemMapJan);
                EasyExcelFactory.write(response.getOutputStream()).excelType(ExcelTypeEnum.CSV).charset(Charset.forName("Shift_JIS")).head(headWrite(date, false)).sheet("PC受注_受注状況").doWrite(data);
                break;
            case 2:
                List<OrderStatusVo> recordsBin = customPageData.getRows();
                Map<String, List<OrderStatusVo>> itemMapBin = recordsBin.stream().collect(Collectors.groupingBy(OrderStatusVo::getJan, LinkedHashMap::new, Collectors.toList()));
                for (Map.Entry<String, List<OrderStatusVo>> key : itemMapBin.entrySet()) {
                    Map<String, List<OrderStatusVo>> mailNoMap = key.getValue().stream().collect(Collectors.groupingBy(OrderStatusVo::getMailNo, LinkedHashMap::new, Collectors.toList()));
                    convert(data, date, mailNoMap);
                }
                EasyExcelFactory.write(response.getOutputStream()).excelType(ExcelTypeEnum.CSV).charset(Charset.forName("Shift_JIS")).head(headWrite(date, false)).sheet("PC受注_受注状況").doWrite(data);
                break;
            case 3:
                List<OrderStatusStoreVo> recordsStore = customPageData.getRows();
                Map<String, List<OrderStatusStoreVo>> itemMapStore = recordsStore.stream().collect(Collectors.groupingBy(OrderStatusStoreVo::getJan, LinkedHashMap::new, Collectors.toList()));
                for (Map.Entry<String, List<OrderStatusStoreVo>> key : itemMapStore.entrySet()) {
                    Map<String, List<OrderStatusStoreVo>> mailNoMap = key.getValue().stream().collect(Collectors.groupingBy(OrderStatusStoreVo::getMailNo, LinkedHashMap::new, Collectors.toList()));
                    for (Map.Entry<String, List<OrderStatusStoreVo>> key1 : mailNoMap.entrySet()) {
                        Map<String, List<OrderStatusStoreVo>> branchCdMap = key1.getValue().stream().collect(Collectors.groupingBy(OrderStatusStoreVo::getBranchCd, LinkedHashMap::new, Collectors.toList()));
                        convertStore(data, date, branchCdMap);
                    }
                }
                EasyExcelFactory.write(response.getOutputStream()).excelType(ExcelTypeEnum.CSV).charset(Charset.forName("Shift_JIS")).head(headWrite(date, true)).sheet("PC受注_受注状況").doWrite(data);
                break;
            default:
                break;
        }
    }

    private void convert(List<List<Object>> data, String[] date, Map<String, List<OrderStatusVo>> map) {
        DecimalFormat format = new DecimalFormat(",###");
        for (Map.Entry<String, List<OrderStatusVo>> entryValue : map.entrySet()) {
            List<OrderStatusVo> li = entryValue.getValue();
            List<Object> row = ListUtils.newArrayList();
            row.add(li.get(0).getSupplierName());
            row.add(li.get(0).getMailNo());
            row.add(li.get(0).getLineName());
            row.add(li.get(0).getWorkGroupName());
            row.add(li.get(0).getJan());
            row.add(li.get(0).getCallCd());
            row.add(li.get(0).getProductName());
            row.add(li.get(0).getItemSpecs());
            row.add(li.get(0).getItemContent());

            int total = 0;
            for (String dateStr : date) {
                if (li.stream().anyMatch(a -> a.getDeliveryDate().equals(dateStr))) {
                    Integer orderQty = li.stream().filter(a -> a.getDeliveryDate().equals(dateStr)).findAny().get().getOrderQty();
                    row.add(format.format(orderQty));
                    total = total + orderQty;
                } else {
                    row.add("");
                }
            }
            row.add(format.format(total));
            data.add(row);
        }
    }

    private void convertStore(List<List<Object>> data, String[] date, Map<String, List<OrderStatusStoreVo>> map) {
        DecimalFormat format = new DecimalFormat(",###");
        for (Map.Entry<String, List<OrderStatusStoreVo>> entryValue : map.entrySet()) {
            List<OrderStatusStoreVo> li = entryValue.getValue();
            List<Object> row = ListUtils.newArrayList();
            row.add(li.get(0).getSupplierName());
            row.add(li.get(0).getMailNo());
            row.add(li.get(0).getLineName());
            row.add(li.get(0).getWorkGroupName());
            row.add(li.get(0).getJan());
            row.add(li.get(0).getCallCd());
            row.add(li.get(0).getProductName());
            row.add(li.get(0).getItemSpecs());
            row.add(li.get(0).getItemContent());
            row.add(li.get(0).getBranchCd());
            row.add(li.get(0).getBranchName());


            int total = 0;
            for (String dateStr : date) {
                if (li.stream().anyMatch(a -> a.getDeliveryDate().equals(dateStr))) {
                    Integer orderQty = li.stream().filter(a -> a.getDeliveryDate().equals(dateStr)).findAny().get().getOrderQty();
                    row.add(format.format(orderQty));
                    total = total + orderQty;
                } else {
                    row.add("");
                }
            }
            row.add(format.format(total));
            data.add(row);
        }
    }

    @Override
    public void downloadCsv(OrderStatusBo orderStatusBo, HttpServletResponse response) {
        PageQuery pageQuery = new PageQuery();
        CustomPageData customPageData = getOrderStatusListFromIdc(orderStatusBo, pageQuery);
        switch (orderStatusBo.getDisplayFormat()) {
            case 1:
            case 2:
                SimpleCsvTableUtils.easyCsvExport(response, "PC受注_受注状況", customPageData.getRows(), OrderStatusVo.class);
                break;
            case 3:
                SimpleCsvTableUtils.easyCsvExport(response, "PC受注_受注状況", customPageData.getRows(), OrderStatusStoreVo.class);
                break;
            default:
                break;
        }
    }

    private List<List<String>> headWrite(String[] days, boolean type) {

        List<List<String>> list = ListUtils.newArrayList();
        String headstr = "合計/受注数,センター; ,便; ,ライン; ,作業グループ; ,JAN; ,品番; ,商品名; ,規格; ,内容量";
        if (type) {
            headstr = "合計/受注数,センター; ,便; ,ライン; ,作業グループ; ,JAN; ,品番; ,商品名; ,規格; ,内容量; ,店舗番号; ,店舗名";
        }
        for (String h : headstr.split(";")) {
            List<String> headLi = ListUtils.newArrayList();
            headLi.addAll(Arrays.asList(h.split(",")));
            list.add(headLi);
        }
        for (int i = 0; i < days.length; i++) {
            List<String> headDay = ListUtils.newArrayList();
            if (i == 0) {
                headDay.add("納品予定日");
            } else {
                headDay.add("");
            }
            headDay.add(days[i]);
            list.add(headDay);
        }
        List<String> headTotal = ListUtils.newArrayList();
        headTotal.addAll(Arrays.asList(",総計".split(",")));
        list.add(headTotal);
        return list;
    }

    @Override
    public void insertProducePlanWk(ProducePlanBo bo) {
        OrderStatusDto dto = BeanUtil.toBean(bo, OrderStatusDto.class);
        dto.setDeliveryStartDate(DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -1)));
        dto.setDeliveryEndDate(DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), 7)));

        QueryWrapper<MtMailcontrol> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
        List<OrderStatusMailControlVo> orderStatusMailControlDtoList = mtMailcontrolMapper.selectVoList(wrapper, OrderStatusMailControlVo.class);
        dto.setMailControlList(orderStatusMailControlDtoList);

        QueryWrapper<MtProductwkgrp> workgroupQueryWrapper = new QueryWrapper<>();
        workgroupQueryWrapper.lambda().eq(MtProductwkgrp::getCenterId, bo.getCenterId());
        if (null != bo.getLineIds() && bo.getLineIds().length > 0) {
            workgroupQueryWrapper.lambda().in(MtProductwkgrp::getLineId, Arrays.asList(bo.getLineIds()));
        }

        List<OrderStatusWorkGroupVo> orderStatusWorkGroupDtoList = mtProductwkgrpMapper.selectVoList(workgroupQueryWrapper, OrderStatusWorkGroupVo.class);
        dto.setWorkGroupList(orderStatusWorkGroupDtoList);

        QueryWrapper<MtCenterstatus> mtCenterstatusQueryWrapper = new QueryWrapper<>();
        mtCenterstatusQueryWrapper.eq("center_id", bo.getCenterId());
        MtCenterstatusVo mtSupplierpc = mtCenterstatusMapper.selectVoOne(mtCenterstatusQueryWrapper);
        dto.setVendorId(mtSupplierpc.getVendorId());
        List<ProducePlanVo> vos = orderStatusMapper.getProducePlan(dto, ip20, ip120);

        QueryWrapper<WkCenterorder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("center_id", bo.getCenterId());
        wkCenterorderMapper.delete(queryWrapper);
        Date now = new Date();
        List<WkCenterorder> li = ListUtils.newArrayList();
        vos.forEach(producePlanVo -> {
            WkCenterorder wkCenterorder = BeanUtil.toBean(producePlanVo, WkCenterorder.class);
            wkCenterorder.setDlvschedDate(DateUtil.parseDate(producePlanVo.getDeliveryDate()));
            wkCenterorder.setCenterId(producePlanVo.getSupplierCd());
            wkCenterorder.setStoreId(Integer.parseInt(producePlanVo.getBranchCD()));
            wkCenterorder.setItemId(producePlanVo.getJan());
            wkCenterorder.setQy(producePlanVo.getOrderQty());

            wkCenterorder.setInsDate(now);
            wkCenterorder.setInsTime(now);
            wkCenterorder.setUpdDate(now);
            wkCenterorder.setUpdTime(now);
            wkCenterorder.setInsUserId(Long.valueOf(producePlanVo.getUserCd()));
            wkCenterorder.setUpdUserId(Long.valueOf(producePlanVo.getUserCd()));
            li.add(wkCenterorder);
        });
        wkCenterorderMapper.insertBatch(li);
    }

    @SneakyThrows
    private CustomPageData getOrderStatusListFromIdc(OrderStatusBo orderStatusBo, PageQuery pageQuery) {
        OrderStatusDto dto = BeanUtil.toBean(orderStatusBo, OrderStatusDto.class);
        dto.setPageNum(pageQuery.getPageNum());
        dto.setPageSize(pageQuery.getPageSize());

        List<OrderStatusMailControlVo> orderStatusMailControlDtoList = mtMailcontrolMapper.getOrderStatus(orderStatusBo.getCenterId(), Arrays.asList(orderStatusBo.getMailNos()));
        dto.setMailControlList(orderStatusMailControlDtoList);

        QueryWrapper<MtProductwkgrp> workgroupQueryWrapper = new QueryWrapper<>();
        workgroupQueryWrapper.lambda().eq(MtProductwkgrp::getCenterId, orderStatusBo.getCenterId());
        if (null != orderStatusBo.getLineIds() && orderStatusBo.getLineIds().length > 0) {
            workgroupQueryWrapper.lambda().in(MtProductwkgrp::getLineId, Arrays.asList(orderStatusBo.getLineIds()));
        }
        if (null != orderStatusBo.getWorkGroupIds() && orderStatusBo.getWorkGroupIds().length > 0) {
            workgroupQueryWrapper.lambda().in(MtProductwkgrp::getId, Arrays.asList(orderStatusBo.getWorkGroupIds()));
        }

        List<OrderStatusWorkGroupVo> orderStatusWorkGroupDtoList = mtProductwkgrpMapper.selectVoList(workgroupQueryWrapper, OrderStatusWorkGroupVo.class);
        dto.setWorkGroupList(orderStatusWorkGroupDtoList);
        CustomPageData customPageData = new CustomPageData<>();
        switch (orderStatusBo.getDisplayFormat()) {
            case 1:
            case 2:
                customPageData = orderStatusMapper.getOrderStatusList(dto, ip20, ip120);
                break;
            case 3:
                customPageData = orderStatusMapper.getOrderStatusListStore(dto, ip20, ip120);
                break;
            default:
                break;
        }
        return customPageData;
    }
}
