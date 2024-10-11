package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.WarehouseItemModifyBo;
import com.tre.centralkitchen.domain.bo.system.WarehouseItemPredictionBo;
import com.tre.centralkitchen.domain.po.TrStock;
import com.tre.centralkitchen.domain.vo.system.*;
import com.tre.centralkitchen.mapper.OrderQtyMapper;
import com.tre.centralkitchen.mapper.TrStockMapper;
import com.tre.centralkitchen.mapper.WarehouseItemPredictionMapper;
import com.tre.centralkitchen.service.WarehouseItemPredictionService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WarehouseItemPredictionServiceImpl implements WarehouseItemPredictionService {

    private final WarehouseItemPredictionMapper mapper;
    private final TrStockMapper trStockMapper;
    private final OrderQtyMapper orderQtyMapper;


    @Override
    public TableDataInfo<WarehouseItemPredictionVo> search(WarehouseItemPredictionBo bo, PageQuery page) {
        bo.build();
        Page<WarehouseItemPredictionVo> pageVo = mapper.search(bo, page.build());
        List<holidayVo> holidays = mapper.searchHoliday();
        LocalDate beginDate = LocalDate.now();
        LocalDate date = bo.getDate();
        LocalDate endDate = date.plusDays(7);
        List<WarehouseQyVo> qyVoList = mapper.searchVo(bo.getCenterId(), beginDate, endDate);
        LocalDate now = LocalDate.now();
        Date startDate = Date.from(now.atStartOfDay(ZoneId.systemDefault()).toInstant());
       Date lastDate =  Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<ShWOrderQtyVo> qtyVoList = orderQtyMapper.searchOrderQtyVo(bo.getCenterId(), startDate,lastDate);
        List<WarehouseItemPredictionVo> records = pageVo.getRecords();
        Integer id = 1;
        for (WarehouseItemPredictionVo record : records) {
            record.setId(id++);
            String itemId = record.getItemId();
            record.setUnit("c/S");
            List<WarehouseQyVo> voList = qyVoList.stream().filter(v -> v.getItemId().equals(itemId)).collect(Collectors.toList());
            List<ShWOrderQtyVo> qtyList = qtyVoList.stream().filter(v -> v.getItemId().equals(itemId)).collect(Collectors.toList());
            if (ObjectUtil.isEmpty(voList)) {
                record.setWarehouseItemDate(null);
            } else {
                List<WarehouseItemDateVo> itemDates = computeDateVo(voList, record, date, holidays,qtyList);
                record.setWarehouseItemDate(itemDates);
            }
        }
        return TableDataInfo.build(pageVo);
    }

    private List<WarehouseItemDateVo> computeDateVo(List<WarehouseQyVo> voList, WarehouseItemPredictionVo record, LocalDate paramDate, List<holidayVo> holidays, List<ShWOrderQtyVo> qtyList) {
        LocalDate date = record.getStockDate();
        List<WarehouseItemDateVo> warehouseItemDateVos = new LinkedList<>();
        List<LocalDate> dateList = new LinkedList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate days = date.plusDays(i);
            dateList.add(days);
        }
        if (paramDate.equals(date)) {
            Map<LocalDate, WarehouseQyVo> map = voList.stream().collect(Collectors.toMap(WarehouseQyVo::getDlvSCHedDate, Function.identity()));
            BigDecimal Dlvsched;
            Map<Date, Integer> qtyVoMap = new HashMap<>();
            if (ObjectUtil.isNotEmpty(qtyList)){
                 qtyVoMap = qtyList.stream().collect(Collectors.toMap(ShWOrderQtyVo::getDeliveryDate, ShWOrderQtyVo::getOrderQty));
            }
            BigDecimal tStorkQy = record.getTStorkQy();
            for (int i = 0; i < dateList.size(); i++) {
                LocalDate localDate = dateList.get(i);
                if (tStorkQy == null) {
                    tStorkQy = BigDecimal.valueOf(0.0);
                }
                if (ObjectUtil.isEmpty(qtyVoMap)){
                    Dlvsched = BigDecimal.valueOf(0.0);
                }else {
                    Date qtyDate =  Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    if (qtyVoMap.get(qtyDate) == null){
                        Dlvsched = BigDecimal.valueOf(0.0);
                    }else {
                        Dlvsched = BigDecimal.valueOf(qtyVoMap.get(qtyDate));
                    }
                }
                WarehouseItemDateVo dateVo = new WarehouseItemDateVo();
                WarehouseQyVo vo = map.get(localDate);
                if (localDate.equals(dateList.get(0))) {
                    dateVo.setTStorkQy(tStorkQy);
                    if (vo != null) {
                        dateVo.setInstQy(vo.getInstQy());
                    } else {
                        dateVo.setInstQy(BigDecimal.valueOf(0.0));
                    }
                } else {
                    BigDecimal yesterdayStorkQy = warehouseItemDateVos.get(i - 1).getTStorkQy();
                    if (vo != null) {
                        tStorkQy = yesterdayStorkQy.add( Dlvsched).subtract(vo.getInstQy());
                        dateVo.setInstQy(vo.getInstQy());
                    } else {
                        tStorkQy = yesterdayStorkQy.add(Dlvsched).subtract(BigDecimal.valueOf(0.0));
                        dateVo.setInstQy(BigDecimal.valueOf(0.0));
                    }
                    dateVo.setTStorkQy(tStorkQy);
                }
                dateVo.setDlvsched(Dlvsched);
                dateVo.setStockDate(localDate);
                List<String> holidayList = holidays.stream().map(holidayVo::getHoliday).collect(Collectors.toList());
                if (holidayList.contains(localDate.toString())) {
                    dateVo.setFlag(1);
                } else {
                    dateVo.setFlag(0);
                }

                warehouseItemDateVos.add(dateVo);

            }
        } else {
            LocalDate toDay = record.getStockDate();
            LocalDate beginDay = paramDate;
            LocalDate endDay = beginDay.plusDays(7);
            LocalDate temp = toDay;
            List<LocalDate> beforeDates = new LinkedList<>();
            while (temp.isBefore(beginDay)) {
                beforeDates.add(temp);
                temp = temp.plusDays(1);
            }
            Map<LocalDate, WarehouseQyVo> map = voList.stream().collect(Collectors.toMap(WarehouseQyVo::getDlvSCHedDate, Function.identity()));
            BigDecimal Dlvsched = BigDecimal.valueOf(0);
            BigDecimal tStorkQy = record.getTStorkQy();
            BigDecimal tempTstorkQy = null;
            for (int i = 0; i < beforeDates.size(); i++) {
                WarehouseQyVo vo = map.get(beforeDates.get(i));
                BigDecimal instQy = vo != null ? vo.getInstQy() : BigDecimal.valueOf(0);
                if (beforeDates.get(i).equals(date)) {
                    tempTstorkQy = tStorkQy != null ? tStorkQy : BigDecimal.valueOf(0);
                } else {
                    tempTstorkQy = tempTstorkQy.add(Dlvsched).subtract(instQy);
                }
            }
            List<LocalDate> afterDates = new LinkedList<>();
            while (!beginDay.isAfter(endDay)) {
                afterDates.add(beginDay);
                beginDay = beginDay.plusDays(1);
            }
            BigDecimal storkQy = tempTstorkQy;
            for (int i = 0; i < afterDates.size(); i++) {

                WarehouseItemDateVo itemDateVo = new WarehouseItemDateVo();
                WarehouseQyVo vo = map.get(afterDates.get(i));
                BigDecimal instQy = vo != null ? vo.getInstQy() : BigDecimal.valueOf(0);
                storkQy = storkQy.add(Dlvsched).subtract(instQy);
                itemDateVo.setTStorkQy(storkQy);
                itemDateVo.setDlvsched(Dlvsched);
                itemDateVo.setStockDate(afterDates.get(i));
                itemDateVo.setInstQy(instQy);
                if (holidays.contains(afterDates.get(i))) {
                    itemDateVo.setFlag(1);
                } else {
                    itemDateVo.setFlag(0);
                }
                warehouseItemDateVos.add(itemDateVo);
            }
        }
        return warehouseItemDateVos;
    }

    @Override
    public void downloadCSV(WarehouseItemPredictionBo bo, HttpServletResponse response) {

        String fileName = "在庫_在庫予測_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR)) + "CSV";
        String[] title = {"センター", "倉庫", "原材料JAN", "原材料名", "ベンダーCD", "ベンダー名", "安全在庫", "賞味期限", "単位", "納品数", "生産数", "在庫数",
                "納品数", "生産数", "在庫数", "納品数", "生産数", "在庫数", "納品数", "生産数", "在庫数", "納品数", "生産数", "在庫数", "納品数", "生産数", "在庫数", "納品数", "生産数", "在庫数"};

        bo.build();
        List<WarehouseItemPredictionVo> predictionVos = mapper.search(bo);
        List<holidayVo> holidays = mapper.searchHoliday();
        LocalDate beginDate = LocalDate.now();
        LocalDate date = bo.getDate();
        LocalDate endDate = date.plusDays(7);
        List<String> dateList = new LinkedList<>();
        List<WarehouseQyVo> qyVoList = mapper.searchVo(bo.getCenterId(), beginDate, endDate);
        List<ShWOrderQtyVo> qtyVoList = orderQtyMapper.searchOrderQtyVo(bo.getCenterId(), new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));
        for (WarehouseItemPredictionVo vo : predictionVos) {
            String itemId = vo.getItemId();
            vo.setUnit("c/S");
            List<WarehouseQyVo> voList = qyVoList.stream().filter(v -> v.getItemId().equals(itemId)).collect(Collectors.toList());
            List<ShWOrderQtyVo> qtyVos = qtyVoList.stream().filter(v -> v.getItemId().equals(itemId)).collect(Collectors.toList());
            if (ObjectUtil.isEmpty(voList)) {
                vo.setWarehouseItemDate(null);
            } else {
                List<WarehouseItemDateVo> itemDates = computeDateVo(voList, vo, date, holidays,qtyVos);
                vo.setWarehouseItemDate(itemDates);
                dateList = itemDates.stream().map(v -> v.getStockDate().toString()).collect(Collectors.toList());
            }
        }
        try {
            this.download(response, fileName, predictionVos, title, dateList);
        } catch (IOException e) {
            System.out.println("e = " + e);
        }

    }

    @Override
    public void update(List<WarehouseItemModifyBo> bos) {
        LocalDate now = LocalDate.now();
        for (WarehouseItemModifyBo bo : bos) {
            TrStock stock = BeanUtil.toBean(bo, TrStock.class);
                stock.setQy(bo.getTStorkQy());
                UpdateWrapper<TrStock> wrapper = new UpdateWrapper<>();
                wrapper.lambda().eq(TrStock::getCenterId, stock.getCenterId())
                        .eq(TrStock::getItemId, stock.getItemId())
                        .eq(TrStock::getWarehouseId, stock.getWarehouseId())
                        .eq(TrStock::getStockDate, stock.getStockDate());
                trStockMapper.update(stock, wrapper);
            }
    }

    public void download(HttpServletResponse response, String fileName, List<WarehouseItemPredictionVo> voList, String[] title, List<String> dateList) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(MailConstants.WAREHOUSE_ITEM_PREDICTION_CSV_NAME);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);
        sheet.autoSizeColumn(0);

        Row row0 = sheet.createRow(0);
        row0.setRowStyle(style);
        int j = 0;
        for (int i = 0; i < title.length; i++) {
            Cell cell = row0.createCell(i, CellType.STRING);
            if (title[i].equals("納品数")) {
                cell.setCellValue(dateList.get(j));
                j++;
            }
        }
        Row row1 = sheet.createRow(1);
        row1.setRowStyle(style);
        for (int i = 0; i < title.length; i++) {
            Cell cell = row1.createCell(i, CellType.STRING);
            cell.setCellValue(title[i]);
        }

        for (int i = 0; i < voList.size(); i++) {
            Row row = sheet.createRow(i + 2);
            WarehouseItemPredictionVo vo = voList.get(i);
            row.setRowStyle(style);
            Cell cell0 = row.createCell(0, CellType.STRING);
            cell0.setCellValue(vo.getCenterName());
            Cell cell1 = row.createCell(1, CellType.STRING);
            cell1.setCellValue(vo.getWarehouseName());
            Cell cell2 = row.createCell(2, CellType.STRING);
            cell2.setCellValue(vo.getItemId());
            Cell cell3 = row.createCell(3, CellType.STRING);
            cell3.setCellValue(vo.getItemName());
            Cell cell4 = row.createCell(4, CellType.STRING);
            cell4.setCellValue(vo.getVendorId());
            Cell cell5 = row.createCell(5, CellType.STRING);
            cell5.setCellValue(vo.getVendorName());
            Cell cell6 = row.createCell(6, CellType.STRING);
            if (vo.getSafetyStockQy() == null) {
                cell6.setCellValue(0);
            } else {
                cell6.setCellValue(vo.getSafetyStockQy());
            }
            Cell cell7 = row.createCell(7, CellType.STRING);
            cell7.setCellValue(vo.getExpTime());
            Cell cell8 = row.createCell(8, CellType.STRING);
            cell8.setCellValue(vo.getUnit());
            if (vo.getWarehouseItemDate() != null) {
                List<WarehouseItemDateVo> warehouseItemDate = vo.getWarehouseItemDate();
                int cellNum = 8;
                for (int k = 0; k < warehouseItemDate.size(); k++) {
                    WarehouseItemDateVo itemDateVo = warehouseItemDate.get(k);
                    Cell cell9 = row.createCell(cellNum + 1, CellType.STRING);
                    cell9.setCellValue(String.valueOf(itemDateVo.getDlvsched()));
                    Cell cell10 = row.createCell(cellNum + 2, CellType.STRING);
                    cell10.setCellValue(String.valueOf(itemDateVo.getInstQy()));
                    Cell cell11 = row.createCell(cellNum + 3, CellType.STRING);
                    cell11.setCellValue(String.valueOf(itemDateVo.getTStorkQy()));
                    cellNum = cellNum + 3;
                }
            }

        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        ops.close();
    }


}
