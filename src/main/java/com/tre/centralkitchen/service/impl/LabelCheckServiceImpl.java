package com.tre.centralkitchen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itextpdf.html2pdf.attach.impl.layout.form.element.CheckBox;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.tre.centralkitchen.common.annotation.PdfTableProp;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.constant.business.InstructionsConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.PdfTablePropInfo;
import com.tre.centralkitchen.common.domain.TableDataInfoExtend;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.common.utils.SimplePdfTableUtils;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.bo.system.LabelCheckBo;
import com.tre.centralkitchen.domain.vo.common.MailListVo;
import com.tre.centralkitchen.domain.vo.system.LabelCheckVo;
import com.tre.centralkitchen.domain.vo.system.MtVariousAutoPrintVO;
import com.tre.centralkitchen.mapper.LabelCheckMapper;
import com.tre.centralkitchen.mapper.MtVariousAutoPrintMapper;
import com.tre.centralkitchen.service.AutoPrintService;
import com.tre.centralkitchen.service.LabelCheckService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * ラベルチェックリスト
 * </p>
 *
 * @author 10253955
 * @since 2023-12-11 16:42
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LabelCheckServiceImpl implements LabelCheckService {

    private static final String FIELD = "field";

    private static final String FIELD_NAME = "fieldName";

    public static final String PDF_NAME = "ラベルチェックリスト";

    private final LabelCheckMapper labelCheckMapper;
    private final MasterService masterService;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final AutoPrintService autoPrintService;
    private final MtVariousAutoPrintMapper autoPrintMapper;


    /**
     * ラベルチェックリスト検索
     *
     * @param pageParam Paging parameter
     * @param param     Query parameter
     * @return Json object of data list and total data
     */
    @Override
    public TableDataInfoExtend getLabelCheckList(PageQuery pageParam, LabelCheckBo param) {
        // get mail no
        List<Integer> mailNoList = this.getMailNoList(param.getCenterId());
        param.setMailNoList(mailNoList);

        Page<Map<String, Object>> pageList = labelCheckMapper.queryList(param, pageParam.build());

        // result handle
        List<Map<String, Object>> finalList = this.resultHandle(pageList.getRecords());

        // get field and fieldName
        List<Map<String, String>> fieldAliasNameList = this.getField(mailNoList);

        return TableDataInfoExtend.build(finalList, pageList.getTotal(), fieldAliasNameList);
    }

    /**
     * ラベルチェックリストのPDF出力
     *
     * @param param    Query parameter
     * @param response response
     */
    @Override
    public void downloadLabelCheckPdf(LabelCheckBo param, HttpServletResponse response) {
        // get mail no
        List<Integer> mailNoList = this.getMailNoList(param.getCenterId());
        param.setMailNoList(mailNoList);

        List<Map<String, Object>> list = labelCheckMapper.queryList(param);
        if (CollUtil.isEmpty(list)) {
            throw new SysBusinessException(SysConstantInfo.NO_DATA_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.NO_DATA_ERROR_CODE);
        }
        try {
            String fileName = SimplePdfTableUtils.getFileName(PDF_NAME, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
            OutputStream out = response.getOutputStream();
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));
            SimplePdfTableUtils.setResponseHeader(response, fileName);
            this.createPdf(list, mailNoList, pdfDocument);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SysBusinessException();
        }
    }

    /**
     * ラベルチェックリストのPDF印刷
     *
     * @param centerId center id
     */
    @Override
    public void printLabelCheckInstructionsPdf(Integer centerId) throws IOException {
        List<MtVariousAutoPrintVO> autoPrintList = autoPrintMapper.selectAllList(centerId, 4);
        if (CollUtil.isEmpty(autoPrintList)) {
            log.info("NO DATA:" + SysConstantInfo.PRINT_LINE_ERROR);
            return;
        }
        Integer[] lineIdArr = autoPrintList.stream().map(MtVariousAutoPrintVO::getLineId).toArray(Integer[]::new);
        // get mail no
        List<Integer> mailNoList = this.getMailNoList(centerId);
        LocalDate nowDate = LocalDate.now();
        LabelCheckBo param = LabelCheckBo.builder()
                .centerId(centerId).dateType(0).lineId(lineIdArr)
                .deliveryStartDate(nowDate).deliveryEndDate(nowDate)
                .mailNoList(mailNoList).build();
        List<Map<String, Object>> list = labelCheckMapper.queryList(param);
        if (CollUtil.isEmpty(list)) {
            log.info("NO DATA:" + centerId);
            return;
        }
        // result handle
        list = this.resultHandle(list);
        Map<Integer, Integer> lineIdQyMap = autoPrintList.stream()
                .collect(Collectors.toMap(MtVariousAutoPrintVO::getLineId, MtVariousAutoPrintVO::getQy, (v1, v2) -> v2));
        // get header parameter settings
        PdfTablePropInfo prop = this.getTableProp(LabelCheckVo.class, mailNoList);

        LocalDateTime curDateTime = LocalDateTime.now();
        String currentTime = curDateTime.format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));
        String fileName = InstructionsConstants.WEB_PAGE_FILE_3 + "_"
                + new SimpleDateFormat(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR).format(new Date())
                + "_" + centerId + ".pdf";
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(simpleFmtTableUtils.getTempFilePath(fileName).toFile()));
            PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH);
            Document document = new Document(pdfDocument, PageSize.A4, false);
            SimplePdfTableUtils.setMargin(document, 15f, 15f);

            // group by lineId
            Map<Integer, List<Map<String, Object>>> map = list.stream()
                    .collect(Collectors.groupingBy(detail -> Integer.parseInt(StrUtil.toString(detail.get("lineId")))));
            // sort by line_id asc
            List<Integer> lineIdList = map.keySet().stream().sorted().collect(Collectors.toList());
            for (Integer lineId : lineIdList) {
                Integer qy = lineIdQyMap.get(lineId);
                if (qy == 0) {
                    continue;
                }
                for (int i = 0; i < qy; i++) {
                    if (i != 0) {
                        SimplePdfTableUtils.nextPage(document);
                    }
                    this.createPdf(list, prop, currentTime, document, pdfFont);
                }
            }
            SimplePdfTableUtils.renderPageNum(pdfDocument, document);
            document.flush();
            document.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SysBusinessException(SysConstantInfo.PRINT_ERROR, HttpStatus.HTTP_OK, SysConstantInfo.PRINT_ERROR_CODE);
        }
        boolean flag = autoPrintService.startPrintPdf(centerId, simpleFmtTableUtils.pdfSplitter(fileName));
        if (flag) {
            log.info("Successfully print pdf file:" + centerId);
        } else {
            log.info("Failed print pdf file:" + centerId);
            throw new SysBusinessException(SysConstantInfo.PRINT_ERROR, HttpStatus.HTTP_OK, SysConstantInfo.PRINT_ERROR_CODE);
        }
    }

    /**
     * create pdf to pdf document
     *
     * @param list        data list
     * @param mailNoList  mail no list
     * @param pdfDocument pdf document
     * @throws IOException io exception
     */
    private void createPdf(List<Map<String, Object>> list, List<Integer> mailNoList,
                           PdfDocument pdfDocument) throws IOException {
        // result handle
        List<Map<String, Object>> finalList = this.resultHandle(list);

        // get header parameter settings
        PdfTablePropInfo prop = this.getTableProp(LabelCheckVo.class, mailNoList);

        // group by lineId,scheduleDateStr
        Map<Integer, Map<String, List<Map<String, Object>>>> map = finalList.stream()
                .collect(Collectors.groupingBy(detail -> Integer.parseInt(StrUtil.toString(detail.get("lineId"))),
                        Collectors.groupingBy(detail -> StrUtil.toString(detail.get("scheduleDateStr")))));
        // sort by line_id asc
        List<Integer> lineIdList = map.keySet().stream().sorted().collect(Collectors.toList());

        LocalDateTime curDateTime = LocalDateTime.now();
        String currentTime = curDateTime.format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));

        int lineIdSize = lineIdList.size();
        PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH);
        Document document = new Document(pdfDocument, PageSize.A4, false);
        SimplePdfTableUtils.setMargin(document, 15f, 15f);

        for (int i = 0; i < lineIdSize; i++) {
            Integer lineId = lineIdList.get(i);
            Map<String, List<Map<String, Object>>> groupMap = map.get(lineId);
            List<String> scheduleDateStrList = groupMap.keySet().stream().sorted().collect(Collectors.toList());
            int scheduleDateStrListSize = scheduleDateStrList.size();
            for (int j = 0; j < scheduleDateStrListSize; j++) {
                if (i != 0 || j != 0) {
                    SimplePdfTableUtils.nextPage(document);
                }
                String scheduleDateStr = scheduleDateStrList.get(j);
                List<Map<String, Object>> groupDataList = groupMap.get(scheduleDateStr);
                // generate tables for each group separately
                this.renderTablePage(groupDataList, prop, pdfFont, document, currentTime, scheduleDateStr);
            }
        }
        SimplePdfTableUtils.renderPageNum(pdfDocument, document);
        document.flush();
        document.close();
    }

    /**
     * create pdf to pdf document
     *
     * @param list        data list
     * @param prop        header parameter settings
     * @param currentTime current time
     * @param document    document
     * @param pdfFont     pdf font
     */
    private void createPdf(List<Map<String, Object>> list, PdfTablePropInfo prop,
                           String currentTime, Document document, PdfFont pdfFont) {
        // group scheduleDateStr
        Map<String, List<Map<String, Object>>> map = list.stream()
                .collect(Collectors.groupingBy(detail -> StrUtil.toString(detail.get("scheduleDateStr"))));
        List<String> scheduleDateStrList = map.keySet().stream().sorted().collect(Collectors.toList());
        for (String scheduleDateStr : scheduleDateStrList) {
            List<Map<String, Object>> groupDataList = map.get(scheduleDateStr);
            // generate tables for each group separately
            this.renderTablePage(groupDataList, prop, pdfFont, document, currentTime, scheduleDateStr);
        }
    }

    /**
     * generate tables for each group separately
     *
     * @param dataList        each group record
     * @param prop            header parameter settings
     * @param pdfFont         pdf font
     * @param document        document
     * @param currentTime     印刷日時
     * @param scheduleDateStr 生産日/納品予定日
     */
    private void renderTablePage(List<Map<String, Object>> dataList,
                                 PdfTablePropInfo prop,
                                 PdfFont pdfFont,
                                 Document document,
                                 String currentTime,
                                 String scheduleDateStr) {
        String lineName = StrUtil.toString(dataList.get(0).get("lineName"));
        // 36 records per page
        List<List<Map<String, Object>>> splitList = CollUtil.split(dataList, 36);
        int size = splitList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                SimplePdfTableUtils.nextPage(document);
            }
            List<Map<String, Object>> mapList = splitList.get(i);
            Table detailTable = SimplePdfTableUtils.createTable(prop.getColumnsWidth(), true);
            // create a header for each page
            this.renderTotalTableHead(currentTime, scheduleDateStr, lineName, document, pdfFont);

            // create header per page
            this.renderDetailTableHead(prop, pdfFont, detailTable);

            for (Map<String, Object> data : mapList) {
                // table add content
                this.renderDetailTableBody(prop, pdfFont, data, detailTable);
            }
            SimplePdfTableUtils.add(document, new Div().add(detailTable));
        }
    }

    /**
     * create header per page
     *
     * @param prop        header parameter settings
     * @param pdfFont     pdf font
     * @param detailTable the table where the header is located
     */
    private void renderDetailTableHead(PdfTablePropInfo prop, PdfFont pdfFont, Table detailTable) {
        Function<String, Cell> cellFunction = title -> {
            Cell cell = SimplePdfTableUtils.createCell(pdfFont, 10f);
            SimplePdfTableUtils.add(cell, SimplePdfTableUtils.createParagraph(title).setBold());
            SimplePdfTableUtils.setAlignment(cell, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
            SimplePdfTableUtils.setBorder(cell, new SolidBorder(0.2f));
            SimplePdfTableUtils.setSize(cell, null, 20f);
            SimplePdfTableUtils.setBackgroundColor(cell, 230, 230, 230);
            return cell;
        };
        for (int i = 0; i < prop.getColumn(); i++) {
            String title = prop.getTitleList()[i];
            Cell cell = cellFunction.apply(title);
            SimplePdfTableUtils.addCell(detailTable, cell);
        }
    }

    /**
     * table add content
     *
     * @param prop        header parameter settings
     * @param pdfFont     pdf font
     * @param data        record per row
     * @param detailTable the table where the row is located
     */
    private void renderDetailTableBody(PdfTablePropInfo prop, PdfFont pdfFont, Map<String, Object> data, Table detailTable) {
        data.put("checkBox", "");
        for (int i = 0; i < prop.getColumn(); i++) {
            Cell cell = SimplePdfTableUtils.createCell(pdfFont, prop.getFontSizeList()[i]);
            String method = prop.getMethodList()[i];

            Object o = data.get(method);
            if (StrUtil.equals(method, "tasteFlg")) {
                if ((int) o == 1) {
                    o = "〇";
                } else {
                    o = null;
                }
            }
            // set the background color to gray when there are no values in the cell
            if (o == null) {
                cell.setBackgroundColor(new DeviceRgb(192, 192, 192));
            }
            if (o instanceof Integer || o instanceof Long) {
                o = NumberUtil.decimalFormat(FormatConstants.THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL, Long.parseLong(o.toString()));
            }
            if (prop.getIsBoldList()[i]) {
                cell.setBold();
            }
            if (StrUtil.equals(method, "checkBox")) {
                cell.add(new CheckBox(""));
                SimplePdfTableUtils.setAlignment(cell, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
            } else {
                SimplePdfTableUtils.add(cell, SimplePdfTableUtils.createParagraph(o == null ? StringConstants.BLANK : o.toString()));
                SimplePdfTableUtils.setAlignment(cell, prop.getTList()[i], prop.getHList()[i], prop.getVList()[i]);
            }
            SimplePdfTableUtils.setBorder(cell, new SolidBorder(0.2f));
            SimplePdfTableUtils.setSize(cell, null, 20f);
            SimplePdfTableUtils.setPadding(cell, 0f, 2f);
            SimplePdfTableUtils.setMargin(cell, 0f, 0f);
            detailTable.addCell(cell);
        }

    }

    /**
     * get header parameter settings
     *
     * @param entity     entity
     * @param mailNoList mail no
     * @return header parameter settings
     */
    private PdfTablePropInfo getTableProp(Class<?> entity, List<Integer> mailNoList) {
        List<Map<String, String>> mailNoMapList = mailNoList.stream().sorted()
                .map(mailNo -> new HashMap<String, String>() {{
                    put(FIELD, "mailNo" + mailNo);
                    put(FIELD_NAME, String.valueOf(mailNo));
                }}).collect(Collectors.toList());

        Field[] fields = entity.getDeclaredFields();

        int columnNum = (int) (mailNoList.size() + Arrays.stream(fields).filter(field -> field.isAnnotationPresent(PdfTableProp.class)).count());

        float[] fontSizes = new float[columnNum];
        boolean[] isBolds = new boolean[columnNum];
        String[] titles = new String[columnNum];
        String[] methodNames = new String[columnNum];
        TextAlignment[] tAligns = new TextAlignment[columnNum];
        VerticalAlignment[] vAligns = new VerticalAlignment[columnNum];
        HorizontalAlignment[] hAligns = new HorizontalAlignment[columnNum];
        UnitValue[] unitValues = new UnitValue[columnNum];
        int counter = 0;
        for (Field field : fields) {
            String fieldName = field.getName();
            if (field.isAnnotationPresent(PdfTableProp.class)) {
                PdfTableProp tmp = field.getAnnotation(PdfTableProp.class);
                methodNames[counter] = field.getName();
                titles[counter] = tmp.title();
                fontSizes[counter] = tmp.fontSize();
                isBolds[counter] = tmp.isBold();
                tAligns[counter] = tmp.textAlign();
                vAligns[counter] = tmp.vAlign();
                hAligns[counter] = tmp.hAlign();
                unitValues[counter] = UnitValue.createPercentValue(tmp.width());
                counter++;
            }
            if (StrUtil.equals(fieldName, "mailNo")) {
                for (Map<String, String> mailNoMap : mailNoMapList) {

                    methodNames[counter] = mailNoMap.get(FIELD);
                    titles[counter] = mailNoMap.get(FIELD_NAME);
                    fontSizes[counter] = 8.0f;
                    isBolds[counter] = false;
                    tAligns[counter] = TextAlignment.CENTER;
                    vAligns[counter] = VerticalAlignment.MIDDLE;
                    hAligns[counter] = HorizontalAlignment.CENTER;
                    unitValues[counter] = UnitValue.createPercentValue(5.0f);
                    counter++;
                }
            }
        }
        PdfTablePropInfo prop = new PdfTablePropInfo();
        prop.setColumn(counter);
        prop.setMethodList(methodNames);
        prop.setTitleList(titles);
        prop.setFontSizeList(fontSizes);
        prop.setIsBoldList(isBolds);
        prop.setTList(tAligns);
        prop.setVList(vAligns);
        prop.setHList(hAligns);
        prop.setColumnsWidth(unitValues);
        return prop;
    }

    /**
     * create a header for each page
     *
     * @param currentTime     印刷日時
     * @param scheduleDateStr 生産日/納品予定日
     * @param lineName        ライン
     * @param document        document
     * @param pdfFont         pdf font
     */
    private void renderTotalTableHead(String currentTime, String scheduleDateStr, String lineName, Document document, PdfFont pdfFont) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(28f));
        frameUnitValueList.add(UnitValue.createPercentValue(10f));
        frameUnitValueList.add(UnitValue.createPercentValue(35f));
        frameUnitValueList.add(UnitValue.createPercentValue(22f));
        Table totalHeaderTable = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);

        // 印刷日時
        Cell printTimeCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 8f, 1, 4);
        SimplePdfTableUtils.add(printTimeCellTotalPage, SimplePdfTableUtils.createParagraph(InstructionsConstants.PRINT_DATE_TIME + currentTime));
        SimplePdfTableUtils.setAlignment(printTimeCellTotalPage, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(printTimeCellTotalPage, Border.NO_BORDER);

        Cell currentDateCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 10f);
        SimplePdfTableUtils.add(currentDateCellTotalPage, SimplePdfTableUtils.createParagraph(scheduleDateStr + "ラベル発行リスト"));
        SimplePdfTableUtils.setAlignment(currentDateCellTotalPage, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(currentDateCellTotalPage, Border.NO_BORDER);

        Cell lineNameCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(lineNameCellTotalPage, SimplePdfTableUtils.createParagraph(lineName).setBold());
        SimplePdfTableUtils.setAlignment(lineNameCellTotalPage, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(lineNameCellTotalPage, Border.NO_BORDER);

        Cell titleCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(titleCellTotalPage, SimplePdfTableUtils.createParagraph("_____________枚回收_____").setUnderline().setBold());
        SimplePdfTableUtils.setAlignment(titleCellTotalPage, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(titleCellTotalPage, Border.NO_BORDER);

        SimplePdfTableUtils.addCell(totalHeaderTable, printTimeCellTotalPage, currentDateCellTotalPage, lineNameCellTotalPage, titleCellTotalPage);
        document.add(totalHeaderTable);
    }

    /**
     * get mail no
     *
     * @param centerId センターコード
     * @return mail no
     */
    private List<Integer> getMailNoList(Integer centerId) {
        CommonMailNoBo commonMailNoBo = new CommonMailNoBo();
        commonMailNoBo.setCenterId(centerId);
        List<MailListVo> mailList = masterService.getMailList(commonMailNoBo);
        return mailList.stream().map(MailListVo::getMailNo).collect(Collectors.toList());
    }

    /**
     * result handle
     *
     * @param records records from sql
     * @return result handle
     */
    private List<Map<String, Object>> resultHandle(List<Map<String, Object>> records) {
        Function<Object, String> formatFunction = localDate -> Objects.isNull(localDate) ?
                "" : StrUtil.replace(localDate.toString(), "-", "/");
        return records.stream().map(map -> {
            Map<String, Object> finalMap = new HashMap<>(map.size());
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String field = entry.getKey();
                // to camel case
                String cameCaseField = StrUtil.toCamelCase(field);
                finalMap.put(cameCaseField, entry.getValue());
            }

            Object finalCallCode = finalMap.get("finalCallCode");
            finalMap.put("finalCallCode", StrUtil.toStringOrNull(finalCallCode));

            Object tasteQy = finalMap.get("tasteQy");
            // case coalesce(prd.taste_qy, 0) when 0 then '' else '〇' end
            Integer tasteFlg = Objects.isNull(tasteQy) || StrUtil.equals(tasteQy.toString(), "0") ? 0 : 1;
            finalMap.remove("tasteQy");
            // 生産計画数
            finalMap.put("tasteFlg", tasteFlg);

            String scheduleDateStr = formatFunction.apply(finalMap.get("scheduleDate"));
            finalMap.put("scheduleDateStr", scheduleDateStr);
            finalMap.remove("scheduleDate");

            return finalMap;
        }).collect(Collectors.toList());
    }

    /**
     * get field and fieldName
     *
     * @param mailNoList mail no list
     * @return fieldName and aliasName
     */
    private List<Map<String, String>> getField(List<Integer> mailNoList) {
        return mailNoList.stream().sorted()
                .map(mailNo -> new HashMap<String, String>() {{
                    put(FIELD, "mailNo" + mailNo);
                    put(FIELD_NAME, String.valueOf(mailNo));
                }}).collect(Collectors.toList());
    }
}
