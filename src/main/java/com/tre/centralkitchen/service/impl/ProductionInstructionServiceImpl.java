package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DoubleBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.tre.centralkitchen.common.constant.*;
import com.tre.centralkitchen.common.constant.business.InstructionsConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.PdfTablePropInfo;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.ReflectUtils;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.common.utils.SimplePdfTableUtils;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.bo.system.ProductionInstructionBo;
import com.tre.centralkitchen.domain.po.MtCenterLineAnyField;
import com.tre.centralkitchen.domain.vo.common.MailListVo;
import com.tre.centralkitchen.domain.vo.system.MtInstructAutoPrintVo;
import com.tre.centralkitchen.domain.vo.system.ProductionInstructionPoVo;
import com.tre.centralkitchen.domain.vo.system.ProductionInstructionTotalVo;
import com.tre.centralkitchen.domain.vo.system.ProductionInstructionVo;
import com.tre.centralkitchen.mapper.MtInstructAutoPrintMapper;
import com.tre.centralkitchen.mapper.ProductionInstructionMapper;
import com.tre.centralkitchen.service.AutoPrintService;
import com.tre.centralkitchen.service.ProductionInstructionService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 10225441
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductionInstructionServiceImpl implements ProductionInstructionService {
    private final MasterService masterService;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final AutoPrintService autoPrintService;
    private final ProductionInstructionMapper productionInstructionMapper;
    private final MtInstructAutoPrintMapper mtInstructAutoPrintMapper;

    @Override
    public TableDataInfo<ProductionInstructionPoVo> queryProductionInstruction(PageQuery pageParam, ProductionInstructionBo param) {
        Page<ProductionInstructionPoVo> res = getProductionInstructionPoPage(pageParam.build(), param);
        res.getRecords().stream().filter(Objects::nonNull).filter(tmp -> tmp.getBatchGroupName() == null).forEach(tmp -> tmp.setBatchGroupName(StringConstants.BLANK));
        return TableDataInfo.build(res);
    }


    private Page<ProductionInstructionPoVo> getProductionInstructionPoPage(Page<ProductionInstructionPoVo> pageParam, ProductionInstructionBo param) {
        checkQueryParameter(param);
        if (param.getDateType().equals(NumberConstants.NUM_INT_0)) {
            return productionInstructionMapper.selectProductionInstructionProdt(pageParam, param);
        } else if (param.getDateType().equals(NumberConstants.NUM_INT_1)) {
            return productionInstructionMapper.selectProductionInstructionSched(pageParam, param);
        } else {
            String error = StrFormatter.format(SysConstantInfo.PARAM_ERROR_MSG_WITH_PLACEHOLDER, InstructionsConstants.ATTR_DATE_TYPE, param.getDateType());
            log.error(error);
            throw new SysBusinessException(error, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    private List<ProductionInstructionPoVo> getProductionInstructionPoPage(ProductionInstructionBo param) {
        checkQueryParameter(param);
        if (param.getDateType().equals(NumberConstants.NUM_INT_0)) {
            return productionInstructionMapper.selectProductionInstructionProdt(param);
        } else if (param.getDateType().equals(NumberConstants.NUM_INT_1)) {
            return productionInstructionMapper.selectProductionInstructionSched(param);
        } else {
            String error = StrFormatter.format(SysConstantInfo.PARAM_ERROR_MSG_WITH_PLACEHOLDER, InstructionsConstants.ATTR_DATE_TYPE, param.getDateType());
            log.error(error);
            throw new SysBusinessException(error, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    private void checkQueryParameter(ProductionInstructionBo param) {
        if (!param.getLineId().isBlank()) {
            param.setLineIdList(Arrays.stream(param.getLineId().split(StringConstants.COMMA)).distinct().map(Integer::parseInt).collect(Collectors.toList()));
        } else {
            param.setLineIdList(new ArrayList<>());
        }
        if (!param.getMailNo().isBlank() && !checkMailAll(param)) {
            param.setMailNoAllFlag(false);
            param.setMailIdList(Arrays.stream(param.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList()));
        } else {
            param.setMailNoAllFlag(true);
            param.setMailIdList(new ArrayList<>());
        }
        if (!param.getWorkGroupId().isBlank()) {
            param.setWorkgroupIdList(Arrays.stream(param.getWorkGroupId().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList()));
        } else {
            param.setWorkgroupIdList(new ArrayList<>());
        }
    }

    private boolean checkMailAll(ProductionInstructionBo param) {
        CommonMailNoBo bo = new CommonMailNoBo();
        bo.setCenterId(param.getCenterId());
        List<MailListVo> mailListAllVos = masterService.getMailList(bo);
        List<Integer> mailNos = mailListAllVos.stream().map(MailListVo::getMailNo).filter(Objects::nonNull).collect(Collectors.toList());
        List<Integer> mailListPageVos = Arrays.stream(param.getMailNo().split(StringConstants.COMMA)).distinct().map(Integer::parseInt).collect(Collectors.toList());
        for (Integer mailNo : mailNos) {
            if (!mailListPageVos.contains(mailNo)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void downloadProductionInstructionCsv(PageQuery pageParam, ProductionInstructionBo param, HttpServletResponse response) {
        List<ProductionInstructionPoVo> res = getProductionInstructionPoPage(param);
        if (param.getDateType().equals(NumberConstants.NUM_INT_0)) {
            SimpleCsvTableUtils.easyCsvExport(response, InstructionsConstants.WEB_PAGE_TITLE_1, res, ProductionInstructionPoVo.class);
        } else if (param.getDateType().equals(NumberConstants.NUM_INT_1)) {
            String fileName = SimpleCsvTableUtils.getFileName(InstructionsConstants.WEB_PAGE_TITLE_1, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
            String[] headers = SimpleCsvTableUtils.getHeaders(InstructionsConstants.CSV_HEADER_1, StringConstants.COMMA);
            SimpleCsvTableUtils.printBeansToRespStream(response, fileName, res, ProductionInstructionPoVo.class, headers);
        }
    }

    @Override
    public void downloadProductionInstructionPdf(ProductionInstructionBo param, HttpServletResponse response) {
        List<ProductionInstructionPoVo> poList = getProductionInstructionPoPage(param);
        if (poList.isEmpty()) {
            throw new SysBusinessException(SysConstantInfo.NO_DATA_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.NO_DATA_ERROR_CODE);
        }
        if (param.getTotalFlag() == 1) {
            poList = getAllPoVos(poList);
        } else {
            poList = getPoVos(poList);
        }
        // scheduleDate group
        Map<String, List<ProductionInstructionPoVo>> map = poList.stream().collect(Collectors.groupingBy(ProductionInstructionPoVo::getScheduleDate));
        LocalDateTime curDateTime = LocalDateTime.now();
        String currentDateStr = curDateTime.format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_02_SEPARATOR));
        String currentDateStrShort = curDateTime.format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));
        String fileName = SimplePdfTableUtils.getFileName(InstructionsConstants.WEB_PAGE_TITLE_1, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
        try (OutputStream out = response.getOutputStream(); PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out))) {
            SimplePdfTableUtils.setResponseHeader(response, fileName);
            PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH);
            Document document = new Document(pdfDocument, PageSize.A4, false);
            SimplePdfTableUtils.setMargin(document, 15f, 15f);
            if (map.isEmpty()) {
                SimplePdfTableUtils.nextPage(document);
            } else {
                AtomicInteger counter = new AtomicInteger(map.size() - 1);
                map.forEach((key, value) -> {
                    renderDetailTablePage(value, currentDateStr, pdfFont, document, new ArrayList<>(), param);
                    SimplePdfTableUtils.nextPage(document);
                    renderTotalTablePage(value, currentDateStrShort, pdfFont, document, param);
                    if (counter.getAndDecrement() != NumberConstants.NUM_INT_0) {
                        SimplePdfTableUtils.nextPage(document);
                    }
                });
                SimplePdfTableUtils.renderPageNum(pdfDocument, document);
            }
            document.flush();
            document.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SysBusinessException();
        }
    }

    @Override
    public void printProductionInstructionPdf(Integer centerId) throws IOException {
        List<MtCenterLineAnyField> mtCenterLineAnyFields = mtInstructAutoPrintMapper.selectLineList(centerId);
        if (mtCenterLineAnyFields.isEmpty()) {
            log.info("NO DATA:" + SysConstantInfo.PRINT_LINE_ERROR);
            return;
        }

        List<MtInstructAutoPrintVo> mtInstructAutoPrints = mtInstructAutoPrintMapper.selectAllList(centerId, 1);
        if (mtInstructAutoPrints.isEmpty()) {
            log.info("NO DATA:" + SysConstantInfo.PRINT_WORKGROUP_ERROR);
            return;
        }
        List<Integer> lineIds = mtCenterLineAnyFields.stream()
                .map(MtCenterLineAnyField::getLineId).collect(Collectors.toList());
        List<Short> workGroupIds = mtInstructAutoPrints.stream()
                .map(MtInstructAutoPrintVo::getWorkGroupId).collect(Collectors.toList());

        ProductionInstructionBo bo = new ProductionInstructionBo();
        bo.setCenterId(centerId);
        bo.setMailNo("");
        bo.setLineId(lineIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        bo.setWorkGroupId(workGroupIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        bo.setDateType(0);
        bo.setStDate(DateUtil.format(DateUtil.date(), FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01));
        bo.setEdDate(DateUtil.format(DateUtil.date(), FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01));
//        bo.setStDate("2024/01/09");
//        bo.setEdDate("2024/01/09");

        bo.setTotalFlag(1);
        List<ProductionInstructionPoVo> poListAll = getProductionInstructionPoPage(bo);

        bo.setTotalFlag(0);
        List<ProductionInstructionPoVo> poList = getProductionInstructionPoPage(bo);
        if (poListAll.isEmpty() || poList.isEmpty()) {
            log.info("NO DATA:" + centerId);
            return;
        }
        List<Integer> lineAll = mtCenterLineAnyFields.stream().filter(a -> Objects.equals(a.getParamVal1(), "1")).map(MtCenterLineAnyField::getLineId).collect(Collectors.toList());
        List<ProductionInstructionPoVo> poListLineAll = poListAll.stream().filter(a -> lineAll.contains(a.getLineId())).collect(Collectors.toList());

        List<Integer> line = mtCenterLineAnyFields.stream().filter(a -> Objects.equals(a.getParamVal1(), "2")).map(MtCenterLineAnyField::getLineId).collect(Collectors.toList());
        List<ProductionInstructionPoVo> poListLine = poList.stream().filter(a -> line.contains(a.getLineId())).collect(Collectors.toList());

        poListLineAll = getAllPoVos(poListLineAll);
        poListLine = getPoVos(poListLine);

        LocalDateTime curDateTime = LocalDateTime.now();
        String currentDateStr = curDateTime.format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_02_SEPARATOR));
        String currentDateStrShort = curDateTime.format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));
        String fileName = InstructionsConstants.WEB_PAGE_FILE_1 + "_" + new SimpleDateFormat(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR).format(new Date()) + "_" + centerId + ".pdf";
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(simpleFmtTableUtils.getTempFilePath(fileName).toFile()));
        try (Document document = new Document(pdfDocument, PageSize.A4, false)) {
            SimplePdfTableUtils.setMargin(document, 15f, 15f);
            PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH);

            if (!poListLineAll.isEmpty()) {
                bo.setTotalFlag(1);
                renderDetailTablePage(poListLineAll, currentDateStr, pdfFont, document, mtInstructAutoPrints, bo);
                SimplePdfTableUtils.nextPage(document);
                renderTotalTablePage(poListLineAll, currentDateStrShort, pdfFont, document, bo);
                if (!poListLine.isEmpty()) {
                    SimplePdfTableUtils.nextPage(document);
                }
            }
            if (!poListLine.isEmpty()) {
                bo.setTotalFlag(0);
                renderDetailTablePage(poListLine, currentDateStr, pdfFont, document, mtInstructAutoPrints, bo);
                SimplePdfTableUtils.nextPage(document);
                renderTotalTablePage(poListLine, currentDateStrShort, pdfFont, document, bo);
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
        }

    }

    @NotNull
    private static List<ProductionInstructionPoVo> getAllPoVos(List<ProductionInstructionPoVo> poList) {
        Map<String, List<ProductionInstructionPoVo>> mapList = poList.stream().collect(Collectors.groupingBy(u -> u.getScheduleDate() + "|" + u.getLineId() + "|" + u.getWorkGroupId() + "|" + u.getCallCode(), LinkedHashMap::new, Collectors.toList()));
        return totalFun(mapList).stream().sorted(Comparator.comparing(ProductionInstructionPoVo::getScheduleDate).thenComparing(ProductionInstructionPoVo::getLineId).thenComparing(ProductionInstructionPoVo::getWorkGroupId)).collect(Collectors.toList());
    }

    @NotNull
    private static List<ProductionInstructionPoVo> getPoVos(List<ProductionInstructionPoVo> poList) {
        Map<String, List<ProductionInstructionPoVo>> mapList = poList.stream().collect(Collectors.groupingBy(u -> u.getScheduleDate() + "|" + u.getLineId() + "|" + u.getWorkGroupId() + "|" + u.getCallCode() + "|" + u.getMailId(), LinkedHashMap::new, Collectors.toList()));
        return totalFun(mapList).stream().sorted(Comparator.comparing(ProductionInstructionPoVo::getScheduleDate).thenComparing(ProductionInstructionPoVo::getLineId).thenComparing(ProductionInstructionPoVo::getWorkGroupId).thenComparing(ProductionInstructionPoVo::getMailId)).collect(Collectors.toList());
    }

    private static List<ProductionInstructionPoVo> totalFun(Map<String, List<ProductionInstructionPoVo>> mapList) {
        List<ProductionInstructionPoVo> listNew = new ArrayList<>();
        for (String groupKey : mapList.keySet()) {
            List<ProductionInstructionPoVo> list = mapList.get(groupKey);
            Integer totalCount = list.stream().reduce(0, (sum, poVo) -> sum + poVo.getQty(), Integer::sum);
            list.get(0).setQty(totalCount);
            listNew.add(list.get(0));
        }
        return listNew;
    }

    private void renderTotalTablePage(List<ProductionInstructionPoVo> beanList, String currentDateStr, PdfFont pdfFont, Document document, ProductionInstructionBo bo) {
        List<ProductionInstructionTotalVo> boList = getProductionInstructionTotalData(beanList);
        PdfTablePropInfo prop = SimplePdfTableUtils.getTableProp(ProductionInstructionTotalVo.class);
        Map<Integer, List<ProductionInstructionTotalVo>> map = boList.stream().collect(Collectors.groupingBy(ProductionInstructionTotalVo::getLineId, LinkedHashMap::new, Collectors.toList()));
        AtomicInteger counter = new AtomicInteger(map.size() - 1);
        map.forEach((key, value) -> {
            List<UnitValue> tableUnitValueList = new ArrayList<>();
            if (bo.getTotalFlag() == 1) {
                tableUnitValueList.add(UnitValue.createPercentValue(20f));
                tableUnitValueList.add(UnitValue.createPercentValue(30f));
                tableUnitValueList.add(UnitValue.createPercentValue(30f));
                tableUnitValueList.add(UnitValue.createPercentValue(20f));
            } else {
                tableUnitValueList.add(UnitValue.createPercentValue(15f));
                tableUnitValueList.addAll(Arrays.asList(prop.getColumnsWidth()));
                tableUnitValueList.add(UnitValue.createPercentValue(15f));
            }
            Table totalTable = SimplePdfTableUtils.createTable(tableUnitValueList.toArray(UnitValue[]::new), true);
            renderTotalTableHead(currentDateStr, pdfFont, totalTable, value, bo);
            for (ProductionInstructionTotalVo productionInstructionTotalVo : value) {
                renderTotalTableBody(pdfFont, totalTable, productionInstructionTotalVo, bo);
            }
            document.add(new Div().add(totalTable));
            if (counter.getAndDecrement() != NumberConstants.NUM_INT_0) {
                SimplePdfTableUtils.nextPage(document);
            }
        });
    }

    private void renderTotalTableBody(PdfFont pdfFont, Table totalTable, ProductionInstructionTotalVo productionInstructionTotalVo, ProductionInstructionBo bo) {

        Cell placeholderCell05 = SimplePdfTableUtils.createCell();
        SimplePdfTableUtils.setBorder(placeholderCell05, Border.NO_BORDER);

        Cell workGroupCellTotalPageChild = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(workGroupCellTotalPageChild, SimplePdfTableUtils.createParagraph(productionInstructionTotalVo.getWorkGroupName()));
        SimplePdfTableUtils.setAlignment(workGroupCellTotalPageChild, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);

        Cell mailNoCellTotalPageChild = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(mailNoCellTotalPageChild, SimplePdfTableUtils.createParagraph(productionInstructionTotalVo.getMailNo()));
        SimplePdfTableUtils.setAlignment(mailNoCellTotalPageChild, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);

        Cell totalCountCellTotalPageChild = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(totalCountCellTotalPageChild, SimplePdfTableUtils.createParagraph(productionInstructionTotalVo.getQty()));
        SimplePdfTableUtils.setAlignment(totalCountCellTotalPageChild, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);

        Cell placeholderCell06 = SimplePdfTableUtils.createCell();
        SimplePdfTableUtils.setBorder(placeholderCell06, Border.NO_BORDER);
        if (bo.getTotalFlag() == 1) {
            SimplePdfTableUtils.addCell(totalTable, placeholderCell05, workGroupCellTotalPageChild, totalCountCellTotalPageChild, placeholderCell06);
        } else {
            SimplePdfTableUtils.addCell(totalTable, placeholderCell05, workGroupCellTotalPageChild, mailNoCellTotalPageChild, totalCountCellTotalPageChild, placeholderCell06);
        }
    }

    private void renderTotalTableHead(String currentDateStr, PdfFont pdfFont, Table totalTable, List<ProductionInstructionTotalVo> boList, ProductionInstructionBo bo) {
        ProductionInstructionTotalVo basicInfo = boList.get(NumberConstants.NUM_INT_0);
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(5f));
        frameUnitValueList.add(UnitValue.createPercentValue(5f));
        frameUnitValueList.add(UnitValue.createPercentValue(45f));
        frameUnitValueList.add(UnitValue.createPercentValue(45f));
        frameUnitValueList.add(UnitValue.createPercentValue(5f));
        frameUnitValueList.add(UnitValue.createPercentValue(5f));
        Table totalHeaderTable = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);

        Cell printTimeCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 6);
        SimplePdfTableUtils.add(printTimeCellTotalPage, SimplePdfTableUtils.createParagraph(InstructionsConstants.PRINT_DATE_TIME + currentDateStr));
        SimplePdfTableUtils.setAlignment(printTimeCellTotalPage, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(printTimeCellTotalPage, Border.NO_BORDER);


        Cell bigTitleCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 42f, 1, 6);
        SimplePdfTableUtils.add(bigTitleCellTotalPage, SimplePdfTableUtils.createParagraph(InstructionsConstants.PDF_PAGE_TITLE_1));
        SimplePdfTableUtils.setAlignment(bigTitleCellTotalPage, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setSize(bigTitleCellTotalPage, null, 84f);
        SimplePdfTableUtils.setBorder(bigTitleCellTotalPage, Border.NO_BORDER);

        Cell placeholderCell01 = SimplePdfTableUtils.createCell(1, 2);
        SimplePdfTableUtils.setPadding(placeholderCell01, 20f, null);
        SimplePdfTableUtils.setBorder(placeholderCell01, Border.NO_BORDER);

        Cell lineNameCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 18f, 1, 1);
        Paragraph lineText = new Paragraph();
        SimplePdfTableUtils.add(lineText, new Text(InstructionsConstants.LINE_WITH_COLON).setFontSize(14f), new Text(basicInfo.getLineName()).setFontSize(28f));
        SimplePdfTableUtils.add(lineNameCellTotalPage, lineText);
        SimplePdfTableUtils.setAlignment(lineNameCellTotalPage, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setPadding(lineNameCellTotalPage, 20f, null);
        SimplePdfTableUtils.setBorder(lineNameCellTotalPage, Border.NO_BORDER);

        Cell bigTotalCountCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 18f, 1, 1);
        Paragraph totalCountText = new Paragraph();
        SimplePdfTableUtils.add(totalCountText, new Text(InstructionsConstants.TOTAL_WITH_COLON).setFontSize(14f), new Text(basicInfo.getTotalCount()).setFontSize(28f));
        SimplePdfTableUtils.add(bigTotalCountCellTotalPage, totalCountText);
        SimplePdfTableUtils.setAlignment(bigTotalCountCellTotalPage, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setPadding(bigTotalCountCellTotalPage, 20f, null);
        SimplePdfTableUtils.setBorder(bigTotalCountCellTotalPage, Border.NO_BORDER);

        Cell placeholderCell02 = SimplePdfTableUtils.createCell(1, 2);
        SimplePdfTableUtils.setPadding(placeholderCell02, 20f, null);
        SimplePdfTableUtils.setBorder(placeholderCell02, Border.NO_BORDER);

        Cell printMailCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 6);
        Paragraph mailText = new Paragraph();
        SimplePdfTableUtils.add(mailText, new Text((bo.getMailNoAllFlag() ? "全" : sortMailNo(bo.getMailNo())) + "便"));
        SimplePdfTableUtils.add(printMailCellTotalPage, mailText);
        SimplePdfTableUtils.setAlignment(printMailCellTotalPage, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setPadding(mailText, 10f, 115f, 0f, 0f);
        SimplePdfTableUtils.setBorder(printMailCellTotalPage, Border.NO_BORDER);

        Cell headerCellTotalPage = SimplePdfTableUtils.createCell(1, 5);
        if (bo.getTotalFlag() == 1) {
            SimplePdfTableUtils.addCell(totalHeaderTable, printTimeCellTotalPage, placeholderCell01, lineNameCellTotalPage, bigTotalCountCellTotalPage, placeholderCell02, printMailCellTotalPage);
        } else {
            SimplePdfTableUtils.addCell(totalHeaderTable, printTimeCellTotalPage, placeholderCell01, lineNameCellTotalPage, bigTotalCountCellTotalPage, placeholderCell02);
        }


        headerCellTotalPage.add(totalHeaderTable);
        SimplePdfTableUtils.setBorder(headerCellTotalPage, Border.NO_BORDER);
        Cell placeholderCell03 = SimplePdfTableUtils.createCell();
        SimplePdfTableUtils.setBorder(placeholderCell03, Border.NO_BORDER);

        Cell workGroupCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(workGroupCellTotalPage, SimplePdfTableUtils.createParagraph(InstructionsConstants.FIELD_WORK_GROUP_ID));
        SimplePdfTableUtils.setAlignment(workGroupCellTotalPage, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBackgroundColor(workGroupCellTotalPage, 219, 219, 219);

        Cell mailNoCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(mailNoCellTotalPage, SimplePdfTableUtils.createParagraph(InstructionsConstants.FIELD_MAIL_NO));
        SimplePdfTableUtils.setAlignment(mailNoCellTotalPage, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBackgroundColor(mailNoCellTotalPage, 219, 219, 219);

        Cell totalCountCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(totalCountCellTotalPage, SimplePdfTableUtils.createParagraph(InstructionsConstants.FIELD_TOTAL));
        SimplePdfTableUtils.setAlignment(totalCountCellTotalPage, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBackgroundColor(totalCountCellTotalPage, 219, 219, 219);

        Cell placeholderCell04 = SimplePdfTableUtils.createCell();
        SimplePdfTableUtils.setBorder(placeholderCell04, Border.NO_BORDER);

        if (bo.getTotalFlag() == 1) {
            SimplePdfTableUtils.addHeaderCell(totalTable, bigTitleCellTotalPage, headerCellTotalPage, placeholderCell03, workGroupCellTotalPage, totalCountCellTotalPage, placeholderCell04);
        } else {
            SimplePdfTableUtils.addHeaderCell(totalTable, bigTitleCellTotalPage, headerCellTotalPage, placeholderCell03, workGroupCellTotalPage, mailNoCellTotalPage, totalCountCellTotalPage, placeholderCell04);
        }

    }

    private void renderDetailTablePage(List<ProductionInstructionPoVo> beanList, String currentDateStr, PdfFont pdfFont,
                                       Document document, List<MtInstructAutoPrintVo> mtInstructAutoPrintVos, ProductionInstructionBo bo) {
        List<ProductionInstructionVo> list = getProductionInstructionData(beanList);
        List<ProductionInstructionTotalVo> productionInstructionTotalData = getProductionInstructionTotalData(beanList);
        PdfTablePropInfo prop = SimplePdfTableUtils.getTableProp(ProductionInstructionVo.class);
        String id = StringConstants.BLANK;
        if (list.isEmpty()) {
            return;
        }
        int counter = 1;
        boolean isFirst = true;
        Table detailTable = SimplePdfTableUtils.createTable(prop.getColumnsWidth(), true);
        detailTable.setExtendBottomRow(true);
        detailTable.setExtendBottomRowOnSplit(true);
        int page = 1;
        for (int k = 0; k <= list.size(); k++) {
            int finalK = k - 1;
            if (k == list.size()) {
                List<MtInstructAutoPrintVo> filter = mtInstructAutoPrintVos.stream().filter(a ->
                        Objects.equals(list.get(finalK).getCenterId(), a.getCenterId())
                                && Objects.equals(list.get(finalK).getWorkGroupId(), Integer.valueOf(a.getWorkGroupId()))
                ).collect(Collectors.toList());
                if (!filter.isEmpty()) {
                    page = filter.get(0).getQy();
                } else {
                    page = 1;
                }
                for (int i = 1; i < page; i++) {
                    SimplePdfTableUtils.add(document, new Div().add(detailTable));
                    SimplePdfTableUtils.nextPage(document);
                }
                break;
            }
            ProductionInstructionVo tmp = list.get(k);
            if (id.equals(tmp.getId())) {
                if (counter % 34 == 1) {
                    setTableHead(pdfFont, prop, detailTable);
                }
                counter = renderDetailTableBody(pdfFont, detailTable, tmp, prop, counter);
                if (k == list.size() - 1 || (k < list.size() - 1 && !list.get(k + 1).getId().equals(tmp.getId()))) {
                    boolean flag = lastWkSumPage(pdfFont, detailTable, tmp, counter, productionInstructionTotalData);
                    if (!flag && counter % 35 == 0) {
                        setTableHead(pdfFont, prop, detailTable);
                    }
                }
            } else {
                if (!isFirst) {
                    for (int i = 0; i < page; i++) {
                        SimplePdfTableUtils.add(document, new Div().add(detailTable));
                        SimplePdfTableUtils.nextPage(document);
                    }
                    counter = 1;
                }
                isFirst = false;
                id = tmp.getId();
                detailTable = SimplePdfTableUtils.createTable(prop.getColumnsWidth(), true);
                renderDetailTableHead(currentDateStr, pdfFont, detailTable, tmp, bo);
                setTableHead(pdfFont, prop, detailTable);
                counter = renderDetailTableBody(pdfFont, detailTable, tmp, prop, counter);
                if (k == list.size() - 1 || (k < list.size() - 1 && !list.get(k + 1).getId().equals(tmp.getId()))) {
                    boolean flag = lastWkSumPage(pdfFont, detailTable, tmp, counter, productionInstructionTotalData);
                    if (!flag && counter % 35 == 0) {
                        setTableHead(pdfFont, prop, detailTable);
                    }
                }
                List<MtInstructAutoPrintVo> filter = mtInstructAutoPrintVos.stream().filter(a ->
                        Objects.equals(tmp.getCenterId(), a.getCenterId()) && Objects.equals(tmp.getWorkGroupId(), Integer.valueOf(a.getWorkGroupId()))
                ).collect(Collectors.toList());
                if (!filter.isEmpty()) {
                    page = filter.get(0).getQy();
                }
            }
        }
        SimplePdfTableUtils.add(document, new Div().add(detailTable));
    }

    private static void setTableHead(PdfFont pdfFont, PdfTablePropInfo prop, Table detailTable) {
        for (int i = 0; i < prop.getColumn(); i++) {
            Cell cell = SimplePdfTableUtils.createCell(pdfFont, 11f);
            SimplePdfTableUtils.add(cell, SimplePdfTableUtils.createParagraph(prop.getTitleList()[i]).setBold());
            SimplePdfTableUtils.setAlignment(cell, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
            SimplePdfTableUtils.setBorder(cell, new SolidBorder(0.2f));
            SimplePdfTableUtils.setSize(cell, null, 20f);
            SimplePdfTableUtils.setBackgroundColor(cell, 219, 219, 219);
            SimplePdfTableUtils.addCell(detailTable, cell);
        }
    }

    private boolean lastWkSumPage(PdfFont pdfFont, Table detailTable, ProductionInstructionVo vo, Integer counter, List<ProductionInstructionTotalVo> productionInstructionTotalData) {
        if (counter > 33) {
            if (counter % 34 > 32 || counter % 34 <= 1) {
                renderDetailTableFoot(pdfFont, detailTable, vo, productionInstructionTotalData);
                return true;
            } else {
                renderDetailTableFoot(pdfFont, detailTable, vo, productionInstructionTotalData);
            }
        } else {
            renderDetailTableFoot(pdfFont, detailTable, vo, productionInstructionTotalData);
        }
        return false;
    }

    private void renderDetailTableFoot(PdfFont pdfFont, Table detailTable, ProductionInstructionVo vo, List<ProductionInstructionTotalVo> productionInstructionTotalData) {
        List<ProductionInstructionTotalVo> productionInstructionTotalVos = productionInstructionTotalData.stream().filter(a -> a.getId().equals(vo.getId())).collect(Collectors.toList());
        String totalCount = productionInstructionTotalVos.get(0).getTotalCount();
        String qty = productionInstructionTotalVos.get(0).getQty();
        Cell emCell = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 10);
        emCell.setBorder(Border.NO_BORDER).setPadding(5f);

        Cell workGroupNameCell = SimplePdfTableUtils.createCell(pdfFont, 11f, 1, 3);
        SimplePdfTableUtils.add(workGroupNameCell, SimplePdfTableUtils.createParagraph("ライン別合計数"));
        SimplePdfTableUtils.setAlignment(workGroupNameCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);

        Cell workGroupNameCell2 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 1);
        SimplePdfTableUtils.add(workGroupNameCell2, SimplePdfTableUtils.createParagraph(totalCount));
        SimplePdfTableUtils.setAlignment(workGroupNameCell2, TextAlignment.RIGHT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);

        Cell productDateCell = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 1);

        BigDecimal va = new BigDecimal(qty.replaceAll(",", ""));
        BigDecimal vb = new BigDecimal(totalCount.replaceAll(",", ""));
        BigDecimal decide = va.divide(vb, 4, RoundingMode.HALF_UP);
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);

        SimplePdfTableUtils.add(productDateCell, SimplePdfTableUtils.createParagraph(percent.format(decide.doubleValue())));
        SimplePdfTableUtils.setAlignment(productDateCell, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);

        Cell mailNameCell = SimplePdfTableUtils.createCell(pdfFont, 11f, 1, 3);
        SimplePdfTableUtils.add(mailNameCell, SimplePdfTableUtils.createParagraph("作業グループ別合計数"));
        SimplePdfTableUtils.setAlignment(mailNameCell, TextAlignment.LEFT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);

        Cell mailNameCell2 = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);

        SimplePdfTableUtils.add(mailNameCell2, SimplePdfTableUtils.createParagraph(qty));
        SimplePdfTableUtils.setAlignment(mailNameCell2, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);

        SimplePdfTableUtils.addCell(detailTable, emCell, workGroupNameCell, workGroupNameCell2, productDateCell, mailNameCell, mailNameCell2);
    }

    private int renderDetailTableBody(PdfFont pdfFont, Table detailTable, ProductionInstructionVo tmp, PdfTablePropInfo prop, int counter) {
        tmp.setNo(String.valueOf(counter));
        for (int i = 0; i < prop.getColumn(); i++) {
            Cell cell = SimplePdfTableUtils.createCell(pdfFont, prop.getFontSizeList()[i]);
            Object o = ReflectUtils.invokeGet(tmp, prop.getMethodList()[i]);
            if (o instanceof Integer) {
                o = NumberUtil.decimalFormat(FormatConstants.THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL, Integer.parseInt(o.toString()));
            }
            SimplePdfTableUtils.add(cell, SimplePdfTableUtils.createParagraph(o == null ? StringConstants.BLANK : o.toString()));
            if (prop.getIsBoldList()[i]) {
                cell.setBold();
            }
            SimplePdfTableUtils.setAlignment(cell, prop.getTList()[i], prop.getHList()[i], prop.getVList()[i]);
            SimplePdfTableUtils.setBorder(cell, new SolidBorder(0.2f));
            SimplePdfTableUtils.setSize(cell, null, 20f);
            if (counter % 34 % 2 == 0) {
                SimplePdfTableUtils.setBackgroundColor(cell, 219, 219, 219);
            }
            SimplePdfTableUtils.setPadding(cell, 0f, 2f);
            SimplePdfTableUtils.setMargin(cell, 0f, 0f);
            detailTable.addCell(cell);
        }
        return ++counter;
    }

    private void renderDetailTableHead(String currentDateStr, PdfFont pdfFont, Table detailTable, ProductionInstructionVo tmp, ProductionInstructionBo bo) {
        Cell companyNameCell = SimplePdfTableUtils.createCell(pdfFont, 8f, 1, 7);
        SimplePdfTableUtils.setAlignment(companyNameCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(companyNameCell, Border.NO_BORDER);
        Cell printTimeCell = SimplePdfTableUtils.createCell(pdfFont, 8f, 1, 3);
        SimplePdfTableUtils.add(printTimeCell, SimplePdfTableUtils.createParagraph(InstructionsConstants.PRINT_DATE_TIME + currentDateStr));
        SimplePdfTableUtils.setAlignment(printTimeCell, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(printTimeCell, Border.NO_BORDER);

        Cell bigTitleCell = SimplePdfTableUtils.createCell(pdfFont, 16f, 1, 7);
        SimplePdfTableUtils.add(bigTitleCell, SimplePdfTableUtils.createParagraph(InstructionsConstants.PDF_PAGE_TITLE_1));
        SimplePdfTableUtils.setAlignment(bigTitleCell, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(bigTitleCell, new DoubleBorder(2f), Border.NO_BORDER, new DoubleBorder(2f), Border.NO_BORDER);

        Cell lineNameCell = SimplePdfTableUtils.createCell(pdfFont, 10f, 1, 3);
        SimplePdfTableUtils.add(lineNameCell, SimplePdfTableUtils.createParagraph(StringConstants.LINE_2 + tmp.getLineName()));
        SimplePdfTableUtils.setAlignment(lineNameCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(lineNameCell, new DoubleBorder(2f), Border.NO_BORDER, new DoubleBorder(2f), Border.NO_BORDER);

        Cell workGroupNameCell = SimplePdfTableUtils.createCell(pdfFont, 11f, 1, bo.getTotalFlag() == 1 ? 4 : 5);
        SimplePdfTableUtils.add(workGroupNameCell, SimplePdfTableUtils.createParagraph(InstructionsConstants.WORK_GROUP_WITH_COLON + tmp.getWorkGroupName()).setBold());
        SimplePdfTableUtils.setAlignment(workGroupNameCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(workGroupNameCell, Border.NO_BORDER);

        Cell productDateCell = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 3);
        SimplePdfTableUtils.add(productDateCell, SimplePdfTableUtils.createParagraph(InstructionsConstants.PRODUCT_DATE_TIME + tmp.getScheduleDate()).setBold());
        SimplePdfTableUtils.setAlignment(productDateCell, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(productDateCell, Border.NO_BORDER);

        Cell mailNameCell;
        if (bo.getTotalFlag() == 1) {
            mailNameCell = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 3);
            SimplePdfTableUtils.add(mailNameCell, SimplePdfTableUtils.createParagraph((bo.getMailNoAllFlag() ? "全" : sortMailNo(bo.getMailNo())) + "便").setBold());
        } else {
            mailNameCell = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 2);
            SimplePdfTableUtils.add(mailNameCell, SimplePdfTableUtils.createParagraph(tmp.getMailNo()).setBold());
        }
        SimplePdfTableUtils.setAlignment(mailNameCell, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(mailNameCell, Border.NO_BORDER);
        SimplePdfTableUtils.addHeaderCell(detailTable, companyNameCell, printTimeCell, bigTitleCell, lineNameCell, workGroupNameCell, productDateCell, mailNameCell);
    }

    private List<ProductionInstructionVo> getProductionInstructionData(List<ProductionInstructionPoVo> beanList) {
        List<ProductionInstructionVo> list = BeanUtil.copyToList(beanList, ProductionInstructionVo.class);
        list.forEach(tmp -> {
            tmp.setCheckbox(StringConstants.CHECKBOX_JP);
        });
        return list;
    }

    private List<ProductionInstructionTotalVo> getProductionInstructionTotalData(List<ProductionInstructionPoVo> beanList) {
        List<ProductionInstructionTotalVo> boList = BeanUtil.copyToList(beanList, ProductionInstructionTotalVo.class);
        Map<String, List<ProductionInstructionTotalVo>> map = boList.stream().collect(Collectors.groupingBy(ProductionInstructionTotalVo::getId, LinkedHashMap::new, Collectors.toList()));
        List<ProductionInstructionTotalVo> list = new ArrayList<>();
        map.forEach((key, value) -> {
            Integer totalCount = value.stream().reduce(0, (sum, bo) -> sum + Integer.parseInt(bo.getQty()), Integer::sum);
            ProductionInstructionTotalVo totalBo = new ProductionInstructionTotalVo();
            ProductionInstructionTotalVo basicInfo = value.get(NumberConstants.NUM_INT_0);
            totalBo.setId(key);
            totalBo.setScheduleDate(basicInfo.getScheduleDate());
            totalBo.setLineName(basicInfo.getLineName());
            totalBo.setLineId(basicInfo.getLineId());
            totalBo.setWorkGroupId(basicInfo.getWorkGroupId());
            totalBo.setWorkGroupName(basicInfo.getWorkGroupName());
            totalBo.setMailNo(basicInfo.getMailNo());
            totalBo.setQty(totalCount.toString());
            list.add(totalBo);
        });
        list.forEach(tmp -> {
            Integer totalCount = boList.stream().filter(a -> a.getLineId().equals(tmp.getLineId()))
                    .reduce(0, (sum, bo) -> sum + Integer.parseInt(bo.getQty()), Integer::sum);
            tmp.setTotalCount(NumberUtil.decimalFormat(FormatConstants.THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL, totalCount));
            tmp.setQty(NumberUtil.decimalFormat(FormatConstants.THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL, Integer.parseInt(tmp.getQty())));
        });
        return list;
    }

    private String sortMailNo(String mailNo) {
        String[] mailNoArr = mailNo.split(",");
        int[] a1 = new int[mailNoArr.length];
        for (int i = 0; i < mailNoArr.length; i++) {
            a1[i] = Integer.parseInt(mailNoArr[i]);
        }
        Arrays.sort(a1);
        StringBuilder s3 = new StringBuilder();
        for (int i = 0; i < a1.length; i++) {
            if (i == a1.length - 1) {
                s3.append(a1[i]);
            } else {
                s3.append(a1[i]).append(",");
            }
        }
        return s3.toString();
    }
}
