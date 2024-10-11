package com.tre.centralkitchen.service.impl;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.NumberConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.constant.business.InstructionsConstants;
import com.tre.centralkitchen.common.constant.business.WorkReportConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.BeanCopyUtils;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.common.utils.SimplePdfTableUtils;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.bo.system.WorkReportBo;
import com.tre.centralkitchen.domain.vo.common.MailListVo;
import com.tre.centralkitchen.domain.vo.common.StoreShortNameVo;
import com.tre.centralkitchen.domain.vo.system.MtVariousAutoPrintVO;
import com.tre.centralkitchen.domain.vo.system.WorkReportVo;
import com.tre.centralkitchen.mapper.MtVariousAutoPrintMapper;
import com.tre.centralkitchen.mapper.WorkReportMapper;
import com.tre.centralkitchen.service.AutoPrintService;
import com.tre.centralkitchen.service.WorkReportService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 作業報告書
 * </p>
 *
 * @author 10253955
 * @since 2023-12-19 13:11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WorkReportServiceImpl implements WorkReportService {

    private final WorkReportMapper workReportMapper;
    private final MasterService masterService;
    private final MtVariousAutoPrintMapper autoPrintMapper;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final AutoPrintService autoPrintService;

    /**
     * 作業報告書の検索
     *
     * @param pageParam Paging parameter
     * @param param     Query parameter
     * @return Json object of data list and total data
     */
    @Override
    public TableDataInfo<WorkReportVo> search(PageQuery pageParam, WorkReportBo param) {
        Page<WorkReportVo> page = workReportMapper.queryList(param, pageParam.build());
        // query result processing
        this.resultHandle(page.getRecords(), param, this.getGroupId());
        return TableDataInfo.build(page);
    }

    /**
     * 作業報告書のCSVサマリ
     *
     * @param param    Query parameter
     * @param response response
     */
    @Override
    public void downloadCsvSummary(WorkReportBo param, HttpServletResponse response) {
        List<WorkReportVo> list = workReportMapper.queryCsvData(param);
        // query result processing
        this.resultHandle(list, param, this.getGroupId());
        // set not produce plan data null
        list = this.setNotProPlanDataNull(list);
        Map<Integer, String> storeIdNameMap = list.stream()
                .peek(wrv -> {
                    if (Objects.isNull(wrv.getStoreName())) {
                        wrv.setStoreName("");
                    }
                })
                .collect(Collectors.toMap(WorkReportVo::getStoreId, WorkReportVo::getStoreName, (v1, v2) -> v2));

        List<Integer> storeIdList = storeIdNameMap.keySet().stream().sorted().collect(Collectors.toList());
        // get csv output header
        LinkedHashMap<String, String> header = this.getCsvOutputHeader(param.getDateType());

        List<LinkedHashMap<String, Object>> dataList = new ArrayList<>();
        Map<String, List<WorkReportVo>> groupMap = list.stream().collect(Collectors.groupingBy(wrv ->
                wrv.getCenterId() + wrv.getScheduleDateStr()
                        + wrv.getMailNo() + wrv.getLineId()
                        + wrv.getJan() + wrv.getCallCode()
                        + wrv.getItemName() + wrv.getExpTime()
                        + wrv.getExpirationTime()));
        groupMap.values().forEach(workReportVos -> {
            // same key list get first one
            WorkReportVo workReportVo = workReportVos.get(0);
            Map<String, Object> beanMap = BeanCopyUtils.copyToMap(workReportVo);
            LinkedHashMap<String, Object> data = new LinkedHashMap<>();
            if (Objects.nonNull(beanMap)) {
                // add fixed column values
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    String field = entry.getKey();
                    String fieldName = entry.getValue();
                    Object value = beanMap.getOrDefault(field, null);
                    data.put(fieldName, value);
                }
            }
            // 店舗コード sum(指示数) mapping
            Map<Integer, Integer> storeIdInstructionNumSumMap = workReportVos.stream()
                    .filter(wrv -> Objects.nonNull(wrv.getInstructionNum()))
                    .collect(Collectors.groupingBy(WorkReportVo::getStoreId, Collectors.summingInt(WorkReportVo::getInstructionNum)));
            // add values for dynamic columns（店舗）
            for (Integer storeId : storeIdList) {
                String storeName = storeIdNameMap.get(storeId);
                Integer instructionNumSumValue = storeIdInstructionNumSumMap.getOrDefault(storeId, null);

                // convert store name to column header
                String name = "(" + storeId + ")" + storeName;
                data.put(name, instructionNumSumValue);
                header.put(storeId.toString(), name);
            }
            dataList.add(data);
        });

        // order by 納品予定日 asc
        List<LinkedHashMap<String, Object>> result = dataList.stream()
                .sorted(Comparator.comparing(obj -> StrUtil.toStringOrNull(obj.get("dlvschedDateStr")), Comparator.nullsLast(String::compareTo)))
                .collect(Collectors.toList());

        String[] headers = new String[header.size()];
        header.values().toArray(headers);

        SimpleCsvTableUtils.csvExport(response, SysConstants.WORK_REPORT_FILE_NAME_CSV, result, headers);
    }

    @Override
    public void downloadPdf(WorkReportBo bo, HttpServletResponse response) {
        List<WorkReportVo> list = workReportMapper.queryCsvData(bo);
        this.resultHandle(list, bo, this.getGroupId());
        // set not produce plan data null
        list = this.setNotProPlanDataNull(list);
        List<WorkReportVo> listNew = list.stream().filter(a -> Arrays.asList(bo.getPrintIds().split(",")).contains(a.getId())).collect(Collectors.toList());
        Map<String, List<WorkReportVo>> map = listNew.stream().collect(Collectors.groupingBy(WorkReportVo::getId, LinkedHashMap::new, Collectors.toList()));
        if (map.isEmpty()) {
            throw new SysBusinessException(SysConstantInfo.NO_DATA_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.NO_DATA_ERROR_CODE);
        }
        String fileName = SimplePdfTableUtils.getFileName(WorkReportConstants.WEB_PAGE_TITLE, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
        try (OutputStream out = response.getOutputStream(); PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out))) {
            SimplePdfTableUtils.setResponseHeader(response, fileName);
            PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH);
            Document document = new Document(pdfDocument, PageSize.A4, false);
            SimplePdfTableUtils.setMargin(document, 15f, 15f, 25f, 15f);

            int i = 1;
            for (Map.Entry<String, List<WorkReportVo>> entry : map.entrySet()) {
                List<WorkReportVo> data = entry.getValue();
                renderFirstPage(document, pdfFont, data);
                SimplePdfTableUtils.nextPage(document);
                renderSecondPage(document, pdfFont, data);
                if (i != map.size()) {
                    SimplePdfTableUtils.nextPage(document);
                }
                i++;
            }

            SimplePdfTableUtils.renderPageNum(pdfDocument, document);
            document.flush();
            document.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SysBusinessException();
        }
    }

    private void renderFirstPage(Document document, PdfFont pdfFont, List<WorkReportVo> data) {
        headCommon(document, pdfFont, data);

        List<UnitValue> frameUnitValueList2 = new ArrayList<>();
        frameUnitValueList2.add(UnitValue.createPercentValue(60f));
        frameUnitValueList2.add(UnitValue.createPercentValue(40f));
        Table table2 = SimplePdfTableUtils.createTable(frameUnitValueList2.toArray(UnitValue[]::new), true);
        Cell cell4 = SimplePdfTableUtils.createCell();
        SimplePdfTableUtils.setBorder(cell4, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), new SolidBorder(0.5f));
        SimplePdfTableUtils.setPadding(cell4, 0f, 0f);
        setLeftCell4(cell4, pdfFont, data);
        Cell cell5 = SimplePdfTableUtils.createCell();
        SimplePdfTableUtils.setPadding(cell5, 0f, 0f);
        SimplePdfTableUtils.setBorder(cell5, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        setRightCell5(cell5, pdfFont, data);

        SimplePdfTableUtils.addCell(table2, cell4, cell5);
        document.add(table2);

    }


    private void renderSecondPage(Document document, PdfFont pdfFont, List<WorkReportVo> data) {
        headCommon(document, pdfFont, data);
        secondHead(document, pdfFont, data);
        secondStore(document, pdfFont, data);
    }

    private void secondStore(Document document, PdfFont pdfFont, List<WorkReportVo> data) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        for (int i = 1; i <= WorkReportConstants.ROW_NUMBER; i++) {
            frameUnitValueList.add(UnitValue.createPercentValue(WorkReportConstants.CELL_WIDTH));
        }
        Table table = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);

        Map<Integer, List<WorkReportVo>> map = data.stream().collect(Collectors.groupingBy(WorkReportVo::getPurchaseGroupCd, LinkedHashMap::new, Collectors.toList()));
        for (Map.Entry<Integer, List<WorkReportVo>> entry : map.entrySet()) {
            List<WorkReportVo> groupData = entry.getValue();

            Cell cell1 = SimplePdfTableUtils.createCell(pdfFont, 10f, 1, 5);
            SimplePdfTableUtils.add(cell1, SimplePdfTableUtils.createParagraph(WorkReportConstants.BLOCK +
                    (Objects.isNull(groupData.get(0).getPurchaseGroupName()) ? "" : groupData.get(0).getPurchaseGroupName()) + WorkReportConstants.BLOCK));
            SimplePdfTableUtils.setPadding(cell1, 0f, 0f);
            SimplePdfTableUtils.setAlignment(cell1, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
            SimplePdfTableUtils.setBorder(cell1, Border.NO_BORDER);

            Cell cell2 = SimplePdfTableUtils.createCell(pdfFont, 10f, 1, 3);
            int productSum = groupData.stream().map(WorkReportVo::getInstructionNum).filter(Objects::nonNull).mapToInt(num -> num).sum();
            SimplePdfTableUtils.add(cell2, SimplePdfTableUtils.createParagraph(productSum + " 個"));
            SimplePdfTableUtils.setPadding(cell2, 0f, 0f);
            SimplePdfTableUtils.setAlignment(cell2, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
            SimplePdfTableUtils.setBorder(cell2, Border.NO_BORDER);

            Cell cell3 = SimplePdfTableUtils.createCell(pdfFont, 10f, 1, 7);
            int containerUnit = 0;
            if (!Objects.isNull(data.get(0).getContainerUnit())) {
                containerUnit = data.get(0).getContainerUnit();
            }
            String str = containerUnit == 0 ? "" : (productSum / containerUnit) + " - " + (productSum % containerUnit);
            SimplePdfTableUtils.add(cell3, SimplePdfTableUtils.createParagraph(str));
            SimplePdfTableUtils.setPadding(cell3, 0f, 0f);
            SimplePdfTableUtils.setAlignment(cell3, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
            SimplePdfTableUtils.setBorder(cell3, Border.NO_BORDER);

            SimplePdfTableUtils.addCell(table, cell1, cell2, cell3);

            for (int j = 0; j < groupData.size(); j++) {
                Cell cell4 = SimplePdfTableUtils.createCell(pdfFont, 10f);
                SimplePdfTableUtils.setPadding(cell4, 0f, 0f);
                List<UnitValue> frameUnitValueList2 = new ArrayList<>();
                frameUnitValueList2.add(UnitValue.createPercentValue(100f));
                Table table2 = SimplePdfTableUtils.createTable(frameUnitValueList2.toArray(UnitValue[]::new), true);

                Cell cell5 = SimplePdfTableUtils.createCell(pdfFont, 10f);
                Paragraph paragraph1 = SimplePdfTableUtils.createParagraph(pdfFont, 10f, Objects.isNull(groupData.get(j).getStoreId()) ? "" : String.valueOf(groupData.get(j).getStoreId()));
                if (!Objects.isNull(groupData.get(j).getInstructionNum()) && groupData.get(j).getInstructionNum() > 0) {
                    SimplePdfTableUtils.setFontColor(paragraph1, 255, 255, 255);
                    SimplePdfTableUtils.setBackgroundColor(cell5, 94, 94, 94);
                } else {
                    SimplePdfTableUtils.setFontColor(paragraph1, 166, 166, 166);
                }
                SimplePdfTableUtils.setSize(paragraph1, WorkReportConstants.ROW_Width, 18f);
                SimplePdfTableUtils.add(cell5, paragraph1);
                SimplePdfTableUtils.setPadding(cell5, 0f, 2f);
                SimplePdfTableUtils.setAlignment(cell5, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
                SimplePdfTableUtils.setBorder(cell5, Border.NO_BORDER);

                Cell cell6 = SimplePdfTableUtils.createCell(pdfFont, 10f);
                Paragraph paragraph2 = SimplePdfTableUtils.createParagraph(pdfFont, 10f, Objects.isNull(groupData.get(j).getStoreName()) ? "" : groupData.get(j).getStoreName());
                if (!Objects.isNull(groupData.get(j).getInstructionNum()) && groupData.get(j).getInstructionNum() > 0) {
                    SimplePdfTableUtils.setFontColor(paragraph2, 255, 255, 255);
                    SimplePdfTableUtils.setBackgroundColor(cell5, 94, 94, 94);
                } else {
                    SimplePdfTableUtils.setFontColor(paragraph2, 166, 166, 166);
                }
                SimplePdfTableUtils.setSize(paragraph2, WorkReportConstants.ROW_Width, 18f);
                SimplePdfTableUtils.add(cell6, paragraph2);
                SimplePdfTableUtils.setPadding(cell6, 0f, 2f);
                SimplePdfTableUtils.setAlignment(cell6, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.TOP);
                SimplePdfTableUtils.setBorder(cell6, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(new DeviceRgb(166, 166, 166), 0.5f), Border.NO_BORDER);
                if (!Objects.isNull(groupData.get(j).getInstructionNum()) && groupData.get(j).getInstructionNum() > 0) {
                    SimplePdfTableUtils.setBackgroundColor(cell6, 94, 94, 94);
                }
                Cell cell7 = SimplePdfTableUtils.createCell(pdfFont, 10f);
                Paragraph paragraph3 = SimplePdfTableUtils.createParagraph(Objects.isNull(groupData.get(j).getInstructionNum()) ? "" : String.valueOf(groupData.get(j).getInstructionNum()));
                SimplePdfTableUtils.setSize(paragraph3, WorkReportConstants.ROW_Width, 18f);
                SimplePdfTableUtils.add(cell7, paragraph3);
                SimplePdfTableUtils.setPadding(cell7, 0f, 2f);
                SimplePdfTableUtils.setAlignment(cell7, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
                SimplePdfTableUtils.setBorder(cell7, Border.NO_BORDER);

                SimplePdfTableUtils.addCell(table2, cell5, cell6, cell7);
                cell4.add(table2);
                SimplePdfTableUtils.addCell(table, cell4);
            }
            if (groupData.size() % WorkReportConstants.ROW_NUMBER != 0) {
                Cell cell5 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, (groupData.size() > WorkReportConstants.ROW_NUMBER ?
                        groupData.size() - groupData.size() % WorkReportConstants.ROW_NUMBER : WorkReportConstants.ROW_NUMBER - groupData.size()));
                SimplePdfTableUtils.setBorder(cell5, Border.NO_BORDER);
                SimplePdfTableUtils.addCell(table, cell5);
            }
        }
        document.add(table);
    }

    private static void secondHead(Document document, PdfFont pdfFont, List<WorkReportVo> data) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(20f));
        frameUnitValueList.add(UnitValue.createPercentValue(10f));
        frameUnitValueList.add(UnitValue.createPercentValue(25f));
        frameUnitValueList.add(UnitValue.createPercentValue(15f));
        frameUnitValueList.add(UnitValue.createPercentValue(15f));
        frameUnitValueList.add(UnitValue.createPercentValue(15f));
        Table table = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);

        Cell cell1 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell1, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getMailNo()) ? "" : data.get(0).getMailNo() + " ")
                .add(SimplePdfTableUtils.createParagraph(pdfFont, 12f, WorkReportConstants.MAIL)));
        SimplePdfTableUtils.setPadding(cell1, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell1, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell1, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), new SolidBorder(0.5f));

        Cell cell2 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell2, SimplePdfTableUtils.createParagraph(WorkReportConstants.CALL_CODE));
        SimplePdfTableUtils.setPadding(cell2, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell2, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell2, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell2, 221, 221, 221);

        Cell cell3 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell3, SimplePdfTableUtils.createParagraph(String.valueOf(Objects.isNull(data.get(0).getCallCode()) ? "" : data.get(0).getCallCode())));
        SimplePdfTableUtils.setPadding(cell3, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell3, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell3, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell6 = SimplePdfTableUtils.createCell(pdfFont, 14f);
        SimplePdfTableUtils.add(cell6, SimplePdfTableUtils.createParagraph(WorkReportConstants.OFFICIAL_DUTIES));
        SimplePdfTableUtils.setPadding(cell6, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell6, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell6, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell7 = SimplePdfTableUtils.createCell(pdfFont, 14f);
        SimplePdfTableUtils.add(cell7, SimplePdfTableUtils.createParagraph(WorkReportConstants.BEGIN_OFFICIAL));
        SimplePdfTableUtils.setPadding(cell7, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell7, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell7, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell8 = SimplePdfTableUtils.createCell(pdfFont, 14f);
        SimplePdfTableUtils.add(cell8, SimplePdfTableUtils.createParagraph(WorkReportConstants.END_OFFICIAL));
        SimplePdfTableUtils.setPadding(cell8, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell8, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell8, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell4 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell4, SimplePdfTableUtils.createParagraph(WorkReportConstants.PRODUCT_NAME));
        SimplePdfTableUtils.setPadding(cell4, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell4, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell4, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), new SolidBorder(0.5f));
        SimplePdfTableUtils.setBackgroundColor(cell4, 221, 221, 221);

        Cell cell5 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setPadding(cell5, 0f, 0f);
        SimplePdfTableUtils.add(cell5, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getItemName()) ? "" : data.get(0).getItemName()));
        SimplePdfTableUtils.setAlignment(cell5, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell5, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell9 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.setPadding(cell9, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell9, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell9, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell10 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.setPadding(cell10, 0f, 0f);
        SimplePdfTableUtils.add(cell10, SimplePdfTableUtils.createParagraph(WorkReportConstants.SEMICOLON));
        SimplePdfTableUtils.setAlignment(cell10, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell10, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell11 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.setPadding(cell11, 0f, 0f);
        SimplePdfTableUtils.add(cell11, SimplePdfTableUtils.createParagraph(WorkReportConstants.SEMICOLON));
        SimplePdfTableUtils.setAlignment(cell11, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell11, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        SimplePdfTableUtils.addCell(table, cell1, cell2, cell3, cell6, cell7, cell8, cell4, cell5, cell9, cell10, cell11);
        document.add(table);
    }

    private static void headCommon(Document document, PdfFont pdfFont, List<WorkReportVo> data) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(30f));
        frameUnitValueList.add(UnitValue.createPercentValue(40f));
        frameUnitValueList.add(UnitValue.createPercentValue(30f));
        Table table = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);
        Cell cell1 = SimplePdfTableUtils.createCell(pdfFont, 20f);
        SimplePdfTableUtils.add(cell1, SimplePdfTableUtils.createParagraph(WorkReportConstants.HOMEWORK_REPORT));
        SimplePdfTableUtils.setPadding(cell1, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell1, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBackgroundColor(cell1, 221, 221, 221);
        Cell cell2 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell2, SimplePdfTableUtils.createParagraph(WorkReportConstants.DELIVERY_DAY + WorkReportConstants.SEMICOLON + data.get(0).getDlvschedDate().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_FORMAT))));
        SimplePdfTableUtils.setPadding(cell2, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell2, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        Cell cell3 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell3, SimplePdfTableUtils.createParagraph(WorkReportConstants.MANUFACTURING_DAY + WorkReportConstants.SEMICOLON + data.get(0).getProductDate().format(DateTimeFormatter.ofPattern(FormatConstants.MONTH_DAY_FORMAT))));
        SimplePdfTableUtils.setPadding(cell3, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell3, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.addCell(table, cell1, cell2, cell3);
        document.add(table);
    }

    private void setLeftCell4(Cell leftCell4, PdfFont pdfFont, List<WorkReportVo> data) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(25f));
        frameUnitValueList.add(UnitValue.createPercentValue(22.5f));
        frameUnitValueList.add(UnitValue.createPercentValue(8f));
        frameUnitValueList.add(UnitValue.createPercentValue(24.5f));
        frameUnitValueList.add(UnitValue.createPercentValue(15f));
        Table table = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);

        Cell cell1 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell1, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getMailNo()) ? "" : data.get(0).getMailNo() + " ")
                .add(SimplePdfTableUtils.createParagraph(pdfFont, 12f, WorkReportConstants.MAIL)));
        SimplePdfTableUtils.setPadding(cell1, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell1, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell1, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell2 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell2, SimplePdfTableUtils.createParagraph(WorkReportConstants.CALL_CODE));
        SimplePdfTableUtils.setPadding(cell2, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell2, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell2, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell2, 221, 221, 221);

        Cell cell3 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 3);
        SimplePdfTableUtils.add(cell3, SimplePdfTableUtils.createParagraph(String.valueOf(Objects.isNull(data.get(0).getCallCode()) ? "" : data.get(0).getCallCode())));
        SimplePdfTableUtils.setPadding(cell3, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell3, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell3, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell5 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell5, SimplePdfTableUtils.createParagraph(WorkReportConstants.PRODUCT_NAME));
        SimplePdfTableUtils.setPadding(cell5, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell5, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell5, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell5, 221, 221, 221);

        Cell cell6 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 4);
        SimplePdfTableUtils.setPadding(cell6, 0f, 0f);
        SimplePdfTableUtils.add(cell6, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getItemName()) ? "" : data.get(0).getItemName()));
        SimplePdfTableUtils.setAlignment(cell6, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell6, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell7 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell7, SimplePdfTableUtils.createParagraph(WorkReportConstants.INTERNAL_CAPACITY_SALES));
        SimplePdfTableUtils.setPadding(cell7, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell7, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell7, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell7, 221, 221, 221);

        Cell cell8 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.add(cell8, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getVolume()) ? "" : data.get(0).getVolume() + " ")
                .add(SimplePdfTableUtils.createParagraph(pdfFont, 12f, Objects.isNull(data.get(0).getUnitName()) ? "" : data.get(0).getUnitName())));
        SimplePdfTableUtils.setPadding(cell8, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell8, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell8, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell10 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.add(cell10, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getPrice()) ? "" : data.get(0).getPrice() + " ")
                .add(SimplePdfTableUtils.createParagraph(pdfFont, 12f, WorkReportConstants.YEN)));
        SimplePdfTableUtils.setPadding(cell10, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell10, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell10, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell12 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell12, SimplePdfTableUtils.createParagraph(WorkReportConstants.PRODUCTION_INSTRUCTIONS));
        SimplePdfTableUtils.setPadding(cell12, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell12, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell12, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell12, 221, 221, 221);

        Cell cell13 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);
        SimplePdfTableUtils.add(cell13, SimplePdfTableUtils.createParagraph(WorkReportConstants.PRODUCTION_ACHIEVEMENTS));
        SimplePdfTableUtils.setPadding(cell13, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell13, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell13, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell13, 221, 221, 221);

        Cell cell14 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);
        SimplePdfTableUtils.add(cell14, SimplePdfTableUtils.createParagraph(WorkReportConstants.INCREASE_DECREASE));
        SimplePdfTableUtils.setPadding(cell14, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell14, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell14, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell14, 221, 221, 221);

        Cell cell15 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        int productSum = data.stream().map(WorkReportVo::getInstructionNum).filter(Objects::nonNull).mapToInt(num -> num).sum();
        SimplePdfTableUtils.add(cell15, SimplePdfTableUtils.createParagraph(String.valueOf(productSum)));
        SimplePdfTableUtils.setPadding(cell15, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell15, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell15, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell16 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setAlignment(cell16, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell16, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell17 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setAlignment(cell17, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell17, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell18 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell18, SimplePdfTableUtils.createParagraph(WorkReportConstants.LABEL_PUBLISHER));
        SimplePdfTableUtils.setPadding(cell18, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell18, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell18, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell18, 221, 221, 221);

        Cell cell19 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setPadding(cell19, 0f, 0f);
        SimplePdfTableUtils.setBorder(cell19, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell19, 221, 221, 221);

        Cell cell20 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell20, SimplePdfTableUtils.createParagraph(WorkReportConstants.PRODUCTION_CONTAINERS));
        SimplePdfTableUtils.setPadding(cell20, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell20, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell20, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell20, 221, 221, 221);

        Cell cell21 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        int containerUnit = 0;
        if (!Objects.isNull(data.get(0).getContainerUnit())) {
            containerUnit = data.get(0).getContainerUnit();
        }
        SimplePdfTableUtils.add(cell21, SimplePdfTableUtils.createParagraph(containerUnit + " ")
                .add(SimplePdfTableUtils.createParagraph(pdfFont, 10f, WorkReportConstants.ENTER)));
        SimplePdfTableUtils.setPadding(cell21, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell21, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell21, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell22 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.setPadding(cell22, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell22, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell22, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell23 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setPadding(cell23, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell23, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell23, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell23, 221, 221, 221);

        Cell cell24 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setPadding(cell24, 0f, 0f);
        SimplePdfTableUtils.add(cell24,
                SimplePdfTableUtils.createParagraph(containerUnit == 0 ? "" : String.valueOf(productSum / containerUnit))
                        .add(SimplePdfTableUtils.createParagraph(pdfFont, 8f, "コンテナ  "))
                        .add(SimplePdfTableUtils.createParagraph(containerUnit == 0 ? "" : String.valueOf(productSum % containerUnit)))
                        .add(SimplePdfTableUtils.createParagraph(pdfFont, 8f, "個")));
        SimplePdfTableUtils.setAlignment(cell24, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell24, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell25 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell25, SimplePdfTableUtils.createParagraph(WorkReportConstants.MANUFACTURING_RESPONSIBLE));
        SimplePdfTableUtils.setPadding(cell25, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell25, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell25, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell25, 221, 221, 221);

        Cell cell26 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);
        SimplePdfTableUtils.add(cell26, SimplePdfTableUtils.createParagraph(WorkReportConstants.PRODUCTION_BEGIN));
        SimplePdfTableUtils.setPadding(cell26, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell26, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell26, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell26, 221, 221, 221);

        Cell cell27 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);
        SimplePdfTableUtils.add(cell27, SimplePdfTableUtils.createParagraph(WorkReportConstants.PRODUCTION_END));
        SimplePdfTableUtils.setPadding(cell27, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell27, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell27, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell27, 221, 221, 221);

        Cell cell28 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.setPadding(cell28, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell28, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell28, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell29 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setPadding(cell29, 0f, 0f);
        SimplePdfTableUtils.add(cell29, SimplePdfTableUtils.createParagraph(WorkReportConstants.SEMICOLON));
        SimplePdfTableUtils.setAlignment(cell29, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell29, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell30 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setPadding(cell30, 0f, 0f);
        SimplePdfTableUtils.add(cell30, SimplePdfTableUtils.createParagraph(WorkReportConstants.SEMICOLON));
        SimplePdfTableUtils.setAlignment(cell30, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell30, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell31 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 3);
        SimplePdfTableUtils.add(cell31, SimplePdfTableUtils.createParagraph(WorkReportConstants.FOOD_INTAKE));
        SimplePdfTableUtils.setPadding(cell31, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell31, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell31, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell31, 221, 221, 221);

        Cell cell32 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);
        SimplePdfTableUtils.add(cell32, SimplePdfTableUtils.createParagraph(WorkReportConstants.LABEL_VERIFICATION));
        SimplePdfTableUtils.setPadding(cell32, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell32, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell32, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell32, 221, 221, 221);

        Cell cell33 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 3);
        SimplePdfTableUtils.add(cell33, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getTasteQy()) ? "" : String.valueOf(data.get(0).getTasteQy())));
        SimplePdfTableUtils.setPadding(cell33, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell33, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell33, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell34 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 2);
        SimplePdfTableUtils.setPadding(cell34, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell34, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell34, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell35 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell35, SimplePdfTableUtils.createParagraph(WorkReportConstants.PROMOTIONAL_LABEL));
        SimplePdfTableUtils.setPadding(cell35, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell35, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell35, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell35, 221, 221, 221);

        Cell cell36 = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 4);
        SimplePdfTableUtils.setPadding(cell36, 0f, 0f);
        SimplePdfTableUtils.add(cell36, SimplePdfTableUtils.createParagraph(WorkReportConstants.YES_NO));
        SimplePdfTableUtils.setAlignment(cell36, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell36, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        SimplePdfTableUtils.setMargin(table, 0f, 0f);
        SimplePdfTableUtils.addCell(table, cell1, cell2, cell3, cell5, cell6, cell7, cell8, cell10, cell12, cell13, cell14, cell15, cell16, cell17, cell18, cell19, cell20,
                cell21, cell22, cell23, cell24, cell25, cell26, cell27, cell28, cell29, cell30,
                cell31, cell32, cell33, cell34, cell35, cell36);
        leftCell4.add(table);
    }

    private void setRightCell5(Cell rightCell5, PdfFont pdfFont, List<WorkReportVo> data) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(85f));
        frameUnitValueList.add(UnitValue.createPercentValue(15f));
        Table table = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);
        Cell cell1 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell1, SimplePdfTableUtils.createParagraph(WorkReportConstants.CONSUMPTION_PERIOD + "  " +
                (Objects.isNull(data.get(0).getExpTime()) ? "" :
                        DateUtil.format(DateUtil.parse(data.get(0).getExpirationTime(), FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR),
                                FormatConstants.YEAR_MONTH_DAY_HOUR_FORMAT) + WorkReportConstants.HOUR)));
        SimplePdfTableUtils.setPadding(cell1, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell1, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell1, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell2 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell2, SimplePdfTableUtils.createParagraph(Objects.isNull(data.get(0).getExpTime()) ? "" : data.get(0).getExpTime() + WorkReportConstants.H));
        SimplePdfTableUtils.setPadding(cell2, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell2, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell2, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell3 = SimplePdfTableUtils.createCell(pdfFont, 14f, 1, 2);
        SimplePdfTableUtils.add(cell3, SimplePdfTableUtils.createParagraph(WorkReportConstants.INFORMATION_LABEL));
        SimplePdfTableUtils.setAlignment(cell3, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell3, Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell3, 221, 221, 221);

        Cell cell4 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);
        SimplePdfTableUtils.add(cell4, SimplePdfTableUtils.createParagraph(""));
        SimplePdfTableUtils.setBorder(cell4, Border.NO_BORDER);
        SimplePdfTableUtils.setSize(cell4, null, 282f);

        Cell cell5 = SimplePdfTableUtils.createCell(1, 2);
        SimplePdfTableUtils.setBorder(cell5, Border.NO_BORDER);
        SimplePdfTableUtils.setPadding(cell5, 0f, 0f);
        setRightBottomCell5(cell5, pdfFont);

        SimplePdfTableUtils.setMargin(table, 0f, 0f);
        SimplePdfTableUtils.addCell(table, cell1, cell2, cell3, cell4, cell5);
        rightCell5.add(table);
    }

    private void setRightBottomCell5(Cell bottomCell5, PdfFont pdfFont) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(45f));
        frameUnitValueList.add(UnitValue.createPercentValue(55f));
        Table table = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);
        SimplePdfTableUtils.setMargin(table, 0f, 0f);
        table.setBorder(Border.NO_BORDER);

        Cell cell1 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell1, SimplePdfTableUtils.createParagraph(WorkReportConstants.EXCHANGE_DURING_TRANSIT));
        SimplePdfTableUtils.setPadding(cell1, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell1, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell1, new SolidBorder(0.5f), new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell1, 221, 221, 221);

        Cell cell2 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell2, SimplePdfTableUtils.createParagraph(WorkReportConstants.YES_NO));
        SimplePdfTableUtils.setPadding(cell2, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell2, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell2, new SolidBorder(0.5f), Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell3 = SimplePdfTableUtils.createCell(1, 2);
        SimplePdfTableUtils.setPadding(cell3, 0f, 0f);
        SimplePdfTableUtils.setBorder(cell3, Border.NO_BORDER);
        setRightBottomCell3(cell3, pdfFont);

        Cell cell4 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell4, SimplePdfTableUtils.createParagraph(WorkReportConstants.EXCHANGE_DURING_TRANSIT));
        SimplePdfTableUtils.setPadding(cell4, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell4, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell4, new SolidBorder(0.5f), new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);
        SimplePdfTableUtils.setBackgroundColor(cell4, 221, 221, 221);

        Cell cell5 = SimplePdfTableUtils.createCell(pdfFont, 16f);
        SimplePdfTableUtils.add(cell5, SimplePdfTableUtils.createParagraph(WorkReportConstants.YES_NO));
        SimplePdfTableUtils.setPadding(cell5, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell5, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell5, new SolidBorder(0.5f), Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell6 = SimplePdfTableUtils.createCell(1, 2);
        SimplePdfTableUtils.setPadding(cell6, 0f, 0f);
        SimplePdfTableUtils.setBorder(cell6, Border.NO_BORDER);
        setRightBottomCell3(cell6, pdfFont);

        SimplePdfTableUtils.addCell(table, cell1, cell2, cell3, cell4, cell5, cell6);
        SimplePdfTableUtils.setMargin(table, 0f, 0f);
        bottomCell5.add(table);
    }

    private void setRightBottomCell3(Cell bottomCell3, PdfFont pdfFont) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(20f));
        frameUnitValueList.add(UnitValue.createPercentValue(25f));
        frameUnitValueList.add(UnitValue.createPercentValue(30f));
        frameUnitValueList.add(UnitValue.createPercentValue(25f));
        Table table = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);
        SimplePdfTableUtils.setMargin(table, 0f, 0f);

        Cell cell1 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell1, SimplePdfTableUtils.createParagraph(WorkReportConstants.TIME));
        SimplePdfTableUtils.setPadding(cell1, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell1, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell1, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell2 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.setBorder(cell2, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell3 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(cell3, SimplePdfTableUtils.createParagraph(WorkReportConstants.RESIDUAL_LABEL));
        SimplePdfTableUtils.setPadding(cell3, 0f, 0f);
        SimplePdfTableUtils.setAlignment(cell3, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell3, Border.NO_BORDER, new SolidBorder(0.5f), new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell4 = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.setBorder(cell4, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(0.5f), Border.NO_BORDER);

        Cell cell5 = SimplePdfTableUtils.createCell(1, 4);
        SimplePdfTableUtils.setBorder(cell5, Border.NO_BORDER);
        SimplePdfTableUtils.setSize(cell5, null, 140f);

        SimplePdfTableUtils.addCell(table, cell1, cell2, cell3, cell4, cell5);
        SimplePdfTableUtils.setMargin(table, 0f, 0f);
        bottomCell3.add(table);
    }


    /**
     * 作業報告書のCSV出力
     *
     * @param param    Query parameter
     * @param response response
     */
    @Override
    public void downloadCsvOutput(WorkReportBo param, HttpServletResponse response) {
        List<WorkReportVo> list = workReportMapper.queryCsvData(param);
        // query result processing
        this.resultHandle(list, param, this.getGroupId());
        // set not produce plan data null
        list = this.setNotProPlanDataNull(list);
        // get csv summary header
        LinkedHashMap<String, String> header = this.getCsvSummaryHeader(param.getDateType());
        String[] headers = new String[header.size()];
        header.values().toArray(headers);
        String fileName = SimpleCsvTableUtils.getFileName(SysConstants.WORK_REPORT_FILE_NAME_CSV, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
        SimpleCsvTableUtils.printBeansToRespStream(response, fileName, list, WorkReportVo.class, headers);
    }

    @Override
    public void printWorkReportPdf(Integer centerId) throws IOException {
        List<MtVariousAutoPrintVO> autoPrintList = autoPrintMapper.selectAllList(centerId, 3);
        if (autoPrintList.isEmpty()) {
            log.info("NO DATA:" + SysConstantInfo.PRINT_LINE_ERROR);
            return;
        }
        Integer[] lineIds = autoPrintList.stream().map(MtVariousAutoPrintVO::getLineId).toArray(Integer[]::new);
        CommonMailNoBo commonMailNoBo = new CommonMailNoBo();
        commonMailNoBo.setCenterId(centerId);
        List<MailListVo> mailList = masterService.getMailList(commonMailNoBo);
        Integer[] mailNos = mailList.stream().map(MailListVo::getMailNo).collect(Collectors.toList()).toArray(new Integer[0]);

        WorkReportBo bo = new WorkReportBo();
        bo.setCenterId(centerId);
        bo.setMailNo(mailNos);
        bo.setLineId(lineIds);
        bo.setDateType(0);
        bo.setStDate(LocalDate.now());
        bo.setEdDate(LocalDate.now());

//        bo.setLineId(new Integer[]{25});
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        bo.setStDate(LocalDate.parse("2023-12-27", formatter));
//        bo.setEdDate(LocalDate.parse("2023-12-27", formatter));

        List<WorkReportVo> list = workReportMapper.queryCsvData(bo);
        this.resultHandle(list, bo, this.getGroupId());
        // set not produce plan data null
        list = this.setNotProPlanDataNull(list);
        if (list.isEmpty()) {
            log.info("NO DATA:" + centerId);
        } else {
            Map<String, List<WorkReportVo>> map = list.stream().collect(Collectors.groupingBy(WorkReportVo::getId, LinkedHashMap::new, Collectors.toList()));
            String fileName = InstructionsConstants.WEB_PAGE_FILE_4 + "_" + new SimpleDateFormat(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR).format(new Date()) + "_" + centerId + ".pdf";
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(simpleFmtTableUtils.getTempFilePath(fileName).toFile()));
            try (Document document = new Document(pdfDocument, PageSize.A4, false)) {
                PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH);
                SimplePdfTableUtils.setMargin(document, 15f, 15f, 25f, 15f);

                int i = 1;
                for (Map.Entry<String, List<WorkReportVo>> entry : map.entrySet()) {
                    List<WorkReportVo> data = entry.getValue();
                    Map<Integer, Integer> lineIdQyMap = autoPrintList.stream()
                            .collect(Collectors.toMap(MtVariousAutoPrintVO::getLineId, MtVariousAutoPrintVO::getQy, (v1, v2) -> v2));
                    Integer qy = lineIdQyMap.get(data.get(0).getLineId());
                    for (int j = 1; j <= qy; j++) {
                        renderFirstPage(document, pdfFont, data);
                        SimplePdfTableUtils.nextPage(document);
                        renderSecondPage(document, pdfFont, data);
                        if (j != qy) {
                            SimplePdfTableUtils.nextPage(document);
                        }
                    }
                    if (i != map.size()) {
                        SimplePdfTableUtils.nextPage(document);
                    }
                    i++;
                }

                SimplePdfTableUtils.renderPageNum(pdfDocument, document);
                document.flush();
                document.close();
                boolean flag = autoPrintService.startPrintPdf(centerId, simpleFmtTableUtils.pdfSplitter(fileName));
                if (flag) {
                    log.info("Successfully print pdf file:" + centerId);
                } else {
                    log.info("Failed print pdf file:" + centerId);
                    throw new SysBusinessException(SysConstantInfo.PRINT_ERROR, HttpStatus.HTTP_OK, SysConstantInfo.PRINT_ERROR_CODE);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new SysBusinessException();
            }
        }
    }

    /**
     * get csv summary header
     *
     * @return header
     */
    private LinkedHashMap<String, String> getCsvSummaryHeader(Integer dateType) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        Field[] fields = WorkReportVo.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Alias.class)) {
                continue;
            }
            // get csv header
            this.getHeader(dateType, result, field, field.getName());
        }
        return result;
    }

    /**
     * get csv output header
     *
     * @param dateType date type
     * @return header
     */
    private LinkedHashMap<String, String> getCsvOutputHeader(Integer dateType) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        Field[] fields = WorkReportVo.class.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Alias.class)) {
                continue;
            }
            String fieldName = field.getName();
            if (StrUtil.equals(fieldName, "storeId")
                    || StrUtil.equals(fieldName, "storeName")
                    || StrUtil.equals(fieldName, "instructionNum")) {
                continue;
            }
            // get csv header
            this.getHeader(dateType, result, field, fieldName);
        }
        return result;
    }

    /**
     * get csv header
     *
     * @param dateType date type
     */
    private void getHeader(Integer dateType, LinkedHashMap<String, String> result, Field field, String fieldName) {
        Alias alias = field.getAnnotation(Alias.class);
        String aliasValue = alias.value();
        if (StrUtil.equals(fieldName, "scheduleDateStr")) {
            List<String> split = StrUtil.split(aliasValue, "/");
            if (dateType == NumberConstants.NUM_INT_0) {
                aliasValue = split.get(0);
            } else {
                aliasValue = split.get(1);
            }
        }
        result.put(fieldName, aliasValue);
    }

    /**
     * query result processing
     *
     * @param result     query result
     * @param param      Query parameter
     * @param idFunction id function
     */
    private void resultHandle(List<WorkReportVo> result, WorkReportBo param, Function<WorkReportVo, String> idFunction) {
        Function<LocalDate, String> formatFunction = this.formatFunction();

        List<StoreShortNameVo> shortNameList = masterService.getStoreShortName(param.getCenterId(), true);
        Map<String, String> idNameMap = shortNameList.stream()
                .collect(Collectors.toMap(vo -> String.valueOf(vo.getCenterId()) + vo.getLineId() + vo.getMailNo() + vo.getStoreId(),
                        StoreShortNameVo::getStoreShortName, (v1, v2) -> v2));

        Integer dateType = param.getDateType();
        result.forEach(wrv -> {
            wrv.setId(idFunction.apply(wrv));

            String scheduleDateStr;
            if (dateType == NumberConstants.NUM_INT_0) {
                scheduleDateStr = formatFunction.apply(wrv.getProductDate());
            } else {
                scheduleDateStr = formatFunction.apply(wrv.getDlvschedDate());
            }
            wrv.setScheduleDateStr(scheduleDateStr);

            // 消費期限 = 製造日（生産日）＋ 賞味期間(H) ÷ 24
            if (Objects.nonNull(wrv.getProductDate()) && Objects.nonNull(wrv.getExpTime())) {
                LocalDateTime expirationTime = wrv.getProductDate().atTime(0, 0, 0).plusHours(wrv.getExpTime());
                wrv.setExpirationTime(DateUtil.format(expirationTime, FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));
            }

            String key = String.valueOf(wrv.getCenterId()) + wrv.getLineId() + wrv.getMailNo() + wrv.getStoreId();
            String storeShortName = idNameMap.getOrDefault(key, wrv.getStoreName());
            wrv.setStoreShortName(storeShortName);
        });
    }

    /**
     * set not produce plan data null
     *
     * @param result select result
     */
    private List<WorkReportVo> setNotProPlanDataNull(List<WorkReportVo> result) {
        result.forEach(wrv -> {
            if (Objects.nonNull(wrv.getProStoreId()) && !wrv.getProStoreId().equals(wrv.getStoreId())) {
                wrv.setExpTime(null);
                wrv.setExpirationTime(null);
                wrv.setTasteQy(null);
                wrv.setInstructionNum(null);
            }
        });
        Function<WorkReportVo, String> distinctKeyExtractor = vo ->
                vo.getCenterId() + vo.getScheduleDateStr()
                        + vo.getMailNo() + vo.getLineId()
                        + vo.getJan() + vo.getCallCode() + vo.getStoreId();
        // distinct by key
        return result.stream().filter(this.distinctByKey(distinctKeyExtractor)).collect(Collectors.toList());
    }

    /**
     * custom deduplication function
     *
     * @param keyExtractor deduplication rule
     * @param <T>          object entity
     * @return deduplication result
     */
    private <T> Predicate<T> distinctByKey(Function<T, String> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * key = 生産日、納品予定日、便、ラインid、品番
     *
     * @return key
     */
    private Function<WorkReportVo, String> getGroupId() {
        Function<LocalDate, String> formatFunction = this.formatFunction();
        return vo -> String.join("-", formatFunction.apply(vo.getProductDate()),
                formatFunction.apply(vo.getDlvschedDate()),
                String.valueOf(vo.getMailNo()),
                String.valueOf(vo.getLineId()),
                String.valueOf(vo.getCallCode()));
    }

    /**
     * LocalDate to Str yyyy/MM/dd
     *
     * @return Function
     */
    private Function<LocalDate, String> formatFunction() {
        return localDate -> {
            if (Objects.isNull(localDate)) {
                return null;
            }
            LocalDateTime time = localDate.atTime(0, 0, 0);
            return DateUtil.format(time, FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01);
        };
    }
}
