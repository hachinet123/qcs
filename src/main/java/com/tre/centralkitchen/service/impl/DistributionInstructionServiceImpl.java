package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.http.HttpStatus;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import com.tre.centralkitchen.common.constant.*;
import com.tre.centralkitchen.common.constant.business.InstructionsConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.ReflectUtils;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.common.utils.SimplePdfTableUtils;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.bo.system.DistributionInstructionBo;
import com.tre.centralkitchen.domain.vo.common.LineListVo;
import com.tre.centralkitchen.domain.vo.common.MailListVo;
import com.tre.centralkitchen.domain.vo.system.DistributionInstructionPoVo;
import com.tre.centralkitchen.domain.vo.system.DistributionInstructionVo;
import com.tre.centralkitchen.domain.vo.system.MtInstructAutoPrintVo;
import com.tre.centralkitchen.mapper.DistributionInstructionMapper;
import com.tre.centralkitchen.mapper.MtInstructAutoPrintMapper;
import com.tre.centralkitchen.service.AutoPrintService;
import com.tre.centralkitchen.service.DistributionInstructionService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 10225441
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DistributionInstructionServiceImpl implements DistributionInstructionService {
    private final DistributionInstructionMapper distributionInstructionMapper;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final AutoPrintService autoPrintService;
    private final MasterService masterService;
    private final MtInstructAutoPrintMapper mtInstructAutoPrintMapper;

    @Override
    public TableDataInfo<DistributionInstructionPoVo> queryDistributionInstruction(PageQuery pageParam, DistributionInstructionBo param) {
        param.setIsClosed(NumberConstants.NUM_INT_0);
        checkQueryParameter(param);
        if (param.getDateType().equals(NumberConstants.NUM_INT_0)) {
            return TableDataInfo.build(distributionInstructionMapper.selectDistributionInstructionProdt(pageParam.build(), param));
        } else if (param.getDateType().equals(NumberConstants.NUM_INT_1)) {
            return TableDataInfo.build(distributionInstructionMapper.selectDistributionInstructionSched(pageParam.build(), param));
        } else {
            String error = StrFormatter.format(SysConstantInfo.PARAM_ERROR_MSG_WITH_PLACEHOLDER, InstructionsConstants.ATTR_DATE_TYPE, param.getDateType());
            log.error(error);
            throw new SysBusinessException(error, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    private void checkQueryParameter(DistributionInstructionBo param) {
        if (!param.getLineId().isBlank()) {
            param.setLineIdList(Arrays.stream(param.getLineId().split(StringConstants.COMMA)).distinct().map(Integer::parseInt).collect(Collectors.toList()));
        } else {
            param.setLineIdList(new ArrayList<>());
        }
        if (!param.getMailNo().isBlank()) {
            param.setMailIdList(Arrays.stream(param.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList()));
        } else {
            param.setMailIdList(new ArrayList<>());
        }
        if (!param.getWorkGroupId().isBlank()) {
            param.setWorkgroupIdList(Arrays.stream(param.getWorkGroupId().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList()));
        } else {
            param.setWorkgroupIdList(new ArrayList<>());
        }
    }

    @Override
    public void downloadDistributionInstructionCsv(DistributionInstructionBo param, HttpServletResponse response) {
        param.setIsClosed(NumberConstants.NUM_INT_0);
        List<DistributionInstructionPoVo> res;
        checkQueryParameter(param);
        if (param.getDateType().equals(NumberConstants.NUM_INT_0)) {
            res = distributionInstructionMapper.selectDistributionInstructionProdt(param);
        } else if (param.getDateType().equals(NumberConstants.NUM_INT_1)) {
            res = distributionInstructionMapper.selectDistributionInstructionSched(param);
        } else {
            String error = StrFormatter.format(SysConstantInfo.PARAM_ERROR_MSG_WITH_PLACEHOLDER, InstructionsConstants.ATTR_DATE_TYPE, param.getDateType());
            throw new SysBusinessException(error, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
        if (param.getDateType().equals(NumberConstants.NUM_INT_0)) {
            SimpleCsvTableUtils.easyCsvExport(response, InstructionsConstants.WEB_PAGE_TITLE_2, res, DistributionInstructionPoVo.class);
        } else if (param.getDateType().equals(NumberConstants.NUM_INT_1)) {
            String[] headers = SimpleCsvTableUtils.getHeaders(InstructionsConstants.CSV_HEADER_2, StringConstants.COMMA);
            String fileName = SimpleCsvTableUtils.getFileName(InstructionsConstants.WEB_PAGE_TITLE_2, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
            SimpleCsvTableUtils.printBeansToRespStream(response, fileName, res, DistributionInstructionPoVo.class, headers);
        }
    }

    @Override
    public void downloadDistributionInstructionPdf(DistributionInstructionBo bo, HttpServletResponse response) {
        checkQueryParameter(bo);
        List<DistributionInstructionPoVo> res;
        if (bo.getDateType().equals(NumberConstants.NUM_INT_0)) {
            res = distributionInstructionMapper.selectDistributionInstructionProdtPdf(bo);
        } else if (bo.getDateType().equals(NumberConstants.NUM_INT_1)) {
            res = distributionInstructionMapper.selectDistributionInstructionSchedPdf(bo);
        } else {
            String error = StrFormatter.format(SysConstantInfo.PARAM_ERROR_MSG_WITH_PLACEHOLDER, InstructionsConstants.ATTR_DATE_TYPE, bo.getDateType());
            throw new SysBusinessException(error, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
        if (res.isEmpty()) {
            throw new SysBusinessException(SysConstantInfo.NO_DATA_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.NO_DATA_ERROR_CODE);
        }
        List<DistributionInstructionPoVo> storeList = distributionInstructionMapper.selectStoreListWitchIsExceptClosed(bo.getCenterId(), bo.getIsClosed() == NumberConstants.NUM_INT_1);
        Map<String, List<DistributionInstructionPoVo>> map = res.stream().collect(Collectors.groupingBy(DistributionInstructionPoVo::getId, LinkedHashMap::new, Collectors.toList()));
        String currentDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));
        String fileName = SimplePdfTableUtils.getFileName(InstructionsConstants.WEB_PAGE_TITLE_2, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
        PageSize pageSize = new PageSize(842, 595);
        try (OutputStream out = response.getOutputStream()) {
            SimplePdfTableUtils.setResponseHeader(response, fileName);
            PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH_BK);
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out));
            Document document = new Document(pdfDocument, pageSize);
            if (!map.isEmpty()) {
                map.forEach((key, value) -> {
                    String tmpId = StringConstants.BLANK;
                    List<DistributionInstructionPoVo> storeListNew = new ArrayList<>();
                    if (!value.isEmpty()) {
                        String tmpMailNo = value.get(0).getMailNo();
                        tmpId = bo.getCenterId() + StringConstants.LINE_1 + tmpMailNo.substring(0, tmpMailNo.length() - 1);
                        storeListNew = storeList.stream().filter(a -> Objects.equals(a.getLineId(), value.get(0).getLineId())).collect(Collectors.toList());
                    }
                    List<DistributionInstructionPoVo> tmpList = getAllStoreDataList(tmpId, value, storeListNew);
                    if (!tmpList.isEmpty()) {
                        renderTablePage(tmpList, currentDateStr, pdfFont, document);
                    }
                });
            }
            document.flush();
            document.close();
        } catch (Exception e) {
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    @Override
    public void printDistributionInstructionPdf(Integer centerId, Integer isClosed) throws IOException {
        List<MtInstructAutoPrintVo> mtInstructAutoPrints = mtInstructAutoPrintMapper.selectAllList(centerId, 2);
        if (mtInstructAutoPrints.isEmpty()) {
            log.info("NO DATA:" + SysConstantInfo.PRINT_WORKGROUP_ERROR);
            return;
        }

        CommonMailNoBo commonMailNoBo = new CommonMailNoBo();
        commonMailNoBo.setCenterId(centerId);
        List<MailListVo> mailListAllVos = masterService.getMailList(commonMailNoBo);
        List<Integer> mailNos = mailListAllVos.stream().map(MailListVo::getMailNo).filter(Objects::nonNull).collect(Collectors.toList());
        List<Integer> lineIds = masterService.getLineAllList().stream().map(LineListVo::getLineId).collect(Collectors.toList());
        List<Short> workIds = mtInstructAutoPrints.stream()
                .map(MtInstructAutoPrintVo::getWorkGroupId).collect(Collectors.toList());

        DistributionInstructionBo bo = new DistributionInstructionBo();
        bo.setCenterId(centerId);
        bo.setIsClosed(isClosed);
        bo.setMailNo(StringUtils.join(mailNos, ","));
        bo.setLineId(StringUtils.join(lineIds, ","));
        bo.setWorkGroupId(StringUtils.join(workIds, ","));
        bo.setStDate(DateUtil.format(DateUtil.date(), FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01));
        bo.setEdDate(DateUtil.format(DateUtil.date(), FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01));
//        bo.setMailNo("4");
//        bo.setLineId("25");
//        bo.setWorkGroupId("40,41");
//        bo.setStDate("2023/11/08");
//        bo.setEdDate("2023/11/08");

        checkQueryParameter(bo);
        List<DistributionInstructionPoVo> res = distributionInstructionMapper.selectDistributionInstructionProdtPdf(bo);
        if (res.isEmpty()) {
            log.info("NO DATA:" + centerId);
        } else {
            List<DistributionInstructionPoVo> storeList = distributionInstructionMapper.selectStoreListWitchIsExceptClosed(bo.getCenterId(), bo.getIsClosed() == NumberConstants.NUM_INT_1);
            Map<String, List<DistributionInstructionPoVo>> map = res.stream().collect(Collectors.groupingBy(DistributionInstructionPoVo::getId, LinkedHashMap::new, Collectors.toList()));
            String currentDateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));
            String fileName = InstructionsConstants.WEB_PAGE_FILE_2 + "_" + new SimpleDateFormat(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR).format(new Date()) + "_" + centerId + ".pdf";
            PageSize pageSize = new PageSize(842, 595);
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(simpleFmtTableUtils.getTempFilePath(fileName).toFile()));
            try (Document document = new Document(pdfDocument, pageSize)) {
                PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH_BK);
                if (!map.isEmpty()) {
                    map.forEach((key, value) -> {
                        int page;
                        List<MtInstructAutoPrintVo> filter = mtInstructAutoPrints.stream().filter(a ->
                                Objects.equals(centerId, a.getCenterId())
                                        && Objects.equals(value.get(0).getWorkGroupId(), Integer.valueOf(a.getWorkGroupId()))
                        ).collect(Collectors.toList());
                        if (!filter.isEmpty()) {
                            page = filter.get(0).getQy();
                            String tmpId = StringConstants.BLANK;
                            List<DistributionInstructionPoVo> storeListNew = new ArrayList<>();
                            if (!value.isEmpty()) {
                                String tmpMailNo = value.get(0).getMailNo();
                                tmpId = bo.getCenterId() + StringConstants.LINE_1 + tmpMailNo.substring(0, tmpMailNo.length() - 1);
                                storeListNew = storeList.stream().filter(a -> Objects.equals(a.getLineId(), value.get(0).getLineId())).collect(Collectors.toList());
                            }
                            List<DistributionInstructionPoVo> tmpList = getAllStoreDataList(tmpId, value, storeListNew);
                            if (!tmpList.isEmpty()) {
                                for (int i = 0; i < page; i++) {
                                    renderTablePage(tmpList, currentDateStr, pdfFont, document);
                                }
                            }
                        }
                    });
                }
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
    }

    private List<DistributionInstructionPoVo> getAllStoreDataList(String id, List<DistributionInstructionPoVo> beanList, List<DistributionInstructionPoVo> storeList) {
        Map<String, List<DistributionInstructionPoVo>> map = beanList.stream().collect(Collectors.groupingBy(DistributionInstructionPoVo::getStoreId));
        DistributionInstructionPoVo basicPo = beanList.get(NumberConstants.NUM_INT_0);
        List<DistributionInstructionPoVo> newList = new ArrayList<>();
        List<String> fieldNameList = ReflectUtils.getFieldNameList(DistributionInstructionPoVo.class);
        fieldNameList.remove(SysConstants.SERIAL_VERSION_UID);
        Map<String, List<DistributionInstructionPoVo>> storeTypeMap = storeList.stream().collect(Collectors.groupingBy(DistributionInstructionPoVo::getId));
        List<DistributionInstructionPoVo> storeTypeList = storeTypeMap.get(id);
        if (storeTypeList != null && !storeTypeList.isEmpty()) {
            storeTypeList.stream().sorted(Comparator.comparingInt(DistributionInstructionPoVo::getSeq)).forEach(store -> {
                if (map.containsKey(store.getStoreId())) {
                    newList.add(map.get(store.getStoreId()).get(NumberConstants.NUM_INT_0));
                } else {
                    DistributionInstructionPoVo po = new DistributionInstructionPoVo();
                    fieldNameList.forEach(field -> ReflectUtils.invokeSet(po, field, ReflectUtils.invokeGet(basicPo, field)));
                    po.setStoreId(store.getStoreId());
                    po.setStoreName(store.getStoreName());
                    po.setStoreNameShort(store.getStoreNameShort());
                    po.setIsClosed(store.getIsClosed());
                    po.setQty(StringConstants.BLANK);
                    po.setId(store.getId());
                    newList.add(po);
                }
            });
        }
        return newList;
    }

    private void renderTablePage(List<DistributionInstructionPoVo> beanList, String currentDateStr, PdfFont pdfFont, Document document) {
        List<DistributionInstructionVo> boList = getDistributionInstructionData(beanList);
        int unitsLength = 20;
        int cellsNumber = 180;
        UnitValue[] unitValues = new UnitValue[unitsLength];
        for (int i = 0; i < unitsLength; i++) {
            unitValues[i] = UnitValue.createPercentValue(5f);
        }
        DistributionInstructionVo basicInfo = boList.get(NumberConstants.NUM_INT_0);
        Table detailTable = SimplePdfTableUtils.createTable(unitValues, true);
        detailTable.setExtendBottomRow(true);
        detailTable.setExtendBottomRowOnSplit(true);

        Cell companyNameCell = SimplePdfTableUtils.createCell(pdfFont, 8f, 1, 5);
        Paragraph maxStoreNumberParagraph = SimplePdfTableUtils.createParagraph(InstructionsConstants.MAX_STORE_NUM);
        SimplePdfTableUtils.setFontColor(maxStoreNumberParagraph, 191, 191, 191);
        SimplePdfTableUtils.add(companyNameCell, maxStoreNumberParagraph);
        SimplePdfTableUtils.setAlignment(companyNameCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
        SimplePdfTableUtils.setBorder(companyNameCell, Border.NO_BORDER);

        Cell bigTitleCell = SimplePdfTableUtils.createCell(pdfFont, 11f, 1, 10);
        Paragraph titleParagraph = SimplePdfTableUtils.createParagraph(InstructionsConstants.PDF_PAGE_TITLE_2);
        titleParagraph.setBold().setPaddingTop(0f).setMarginTop(0f);
        SimplePdfTableUtils.add(bigTitleCell, titleParagraph);
        SimplePdfTableUtils.setAlignment(bigTitleCell, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(bigTitleCell, Border.NO_BORDER);

        Cell printTimeCell = SimplePdfTableUtils.createCell(pdfFont, 8f, 1, 5);
        SimplePdfTableUtils.add(printTimeCell, SimplePdfTableUtils.createParagraph(InstructionsConstants.PRINT_DATE_TIME + currentDateStr));
        SimplePdfTableUtils.setAlignment(printTimeCell, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.TOP);
        SimplePdfTableUtils.setBorder(printTimeCell, Border.NO_BORDER);

        Cell productDateCell = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 5);
        Paragraph scheduleDateParagraph = SimplePdfTableUtils.createParagraph(InstructionsConstants.SCHEDULE_DATE_TIME + basicInfo.getScheduleDate());
        scheduleDateParagraph.setPaddingTop(0f).setMarginTop(0f);
        SimplePdfTableUtils.add(productDateCell, scheduleDateParagraph);
        SimplePdfTableUtils.setAlignment(productDateCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
        SimplePdfTableUtils.setBorder(productDateCell, Border.NO_BORDER);

        Cell mailNameCell = SimplePdfTableUtils.createCell(pdfFont, 12f, 1, 11);
        Paragraph mailNoParagraph = SimplePdfTableUtils.createParagraph(basicInfo.getMailNo());
        mailNoParagraph.setPaddingTop(0f).setMarginTop(0f);
        SimplePdfTableUtils.add(mailNameCell, mailNoParagraph);
        SimplePdfTableUtils.setAlignment(mailNameCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
        SimplePdfTableUtils.setBorder(mailNameCell, Border.NO_BORDER);

        Cell workGroupNameCell = SimplePdfTableUtils.createCell(pdfFont, 11f, 1, 4);
        Paragraph totalParagraph = new Paragraph(InstructionsConstants.TOTAL_WITH_COLON + basicInfo.getTotalCount());
        totalParagraph.setPaddingTop(0f).setMarginTop(0f);
        SimplePdfTableUtils.setBorder(totalParagraph, Border.NO_BORDER, Border.NO_BORDER, new SolidBorder(2f), Border.NO_BORDER);
        SimplePdfTableUtils.add(workGroupNameCell, totalParagraph);
        SimplePdfTableUtils.setAlignment(workGroupNameCell, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.TOP);
        SimplePdfTableUtils.setBorder(workGroupNameCell, Border.NO_BORDER);

        Cell infoCell = SimplePdfTableUtils.createCell(pdfFont, 1, 20);
        Text text00 = new Text(InstructionsConstants.LINE_WITH_COLON).setFontSize(9f);
        Text text01 = new Text(basicInfo.getLineName() + StringConstants.SPACE_2).setFontSize(15f);
        Text text02 = new Text(InstructionsConstants.WORK_GROUP_WITH_COLON).setFontSize(9f);
        Text text03 = new Text(basicInfo.getWorkGroupName() + StringConstants.SPACE_2).setFontSize(15f);
        Text text04 = new Text(InstructionsConstants.CALL_CODE_WITH_COLON).setFontSize(9f);
        Text text05 = new Text(basicInfo.getCallCode() + StringConstants.SPACE_2).setFontSize(15f);
        Text text06 = new Text(InstructionsConstants.ITEM_NAME_WITH_COLON).setFontSize(9f);
        Text text07 = new Text(basicInfo.getItemName() + StringConstants.SPACE_2).setFontSize(15f);
        Text text08 = new Text(InstructionsConstants.ITEM_SPEC_WITH_COLON).setFontSize(9f);
        Text text09 = new Text(basicInfo.getItemSpecs()).setFontSize(10f);
        Paragraph subTitleParagraph = SimplePdfTableUtils.createParagraph();
        SimplePdfTableUtils.add(subTitleParagraph, text00, text01, text02, text03, text04, text05, text06, text07, text08, text09);
        SimplePdfTableUtils.add(infoCell, subTitleParagraph);
        SimplePdfTableUtils.setAlignment(infoCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(infoCell, Border.NO_BORDER);

        SimplePdfTableUtils.addHeaderCell(detailTable, companyNameCell, bigTitleCell, printTimeCell, productDateCell, mailNameCell, workGroupNameCell, infoCell);
        int count = 1;
        for (DistributionInstructionVo bo : boList) {
            count = renderTableDataCell(pdfFont, detailTable, bo, count);
        }
        int redundancy = 0;
        if (boList.size() % cellsNumber > 0) {
            redundancy = boList.size() + cellsNumber - boList.size() % cellsNumber;
        }
        renderTablePlaceholderCell(pdfFont, detailTable, boList.size() + 1, redundancy);
        SimplePdfTableUtils.setMargin(detailTable, 0f, 0f);
        SimplePdfTableUtils.setMargin(document, 15f, 15f, 15f, 15f);
        document.add(new Div().add(detailTable).setBorder(new SolidBorder(1f)));
    }

    private int renderTableDataCell(PdfFont pdfFont, Table detailTable, DistributionInstructionVo bo, int count) {
        Cell cell = new Cell();
        Paragraph storeIdParagraph = SimplePdfTableUtils.createParagraph(pdfFont, 12f, bo.getStoreId());
        SimplePdfTableUtils.setFontColor(storeIdParagraph, 255, 255, 255);
        if (NumberConstants.NUM_1.equals(bo.getIsClosed())) {
            SimplePdfTableUtils.setBackgroundColor(storeIdParagraph, 166, 166, 166);
            SolidBorder solidBorder = new SolidBorder(1f);
            solidBorder.setColor(new DeviceRgb(166, 166, 166));
            SimplePdfTableUtils.setBorder(storeIdParagraph, solidBorder);
        } else {
            SimplePdfTableUtils.setBackgroundColor(storeIdParagraph, 0, 0, 0);
            SolidBorder solidBorder = new SolidBorder(1f);
            solidBorder.setColor(new DeviceRgb(0, 0, 0));
            SimplePdfTableUtils.setBorder(storeIdParagraph, solidBorder);
        }
        SimplePdfTableUtils.setAlignment(storeIdParagraph, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setPadding(storeIdParagraph, 0f, 0f);
        SimplePdfTableUtils.setMargin(storeIdParagraph, 0f, 0f);
        SimplePdfTableUtils.setSize(storeIdParagraph, UnitValue.createPercentValue(100f), 17f);
        SimplePdfTableUtils.add(cell, storeIdParagraph);

        Paragraph storeNameParagraph = SimplePdfTableUtils.createParagraph(pdfFont, 8f);
        if (NumberConstants.NUM_1.equals(bo.getIsClosed())) {
            SimplePdfTableUtils.add(storeNameParagraph, new Text(InstructionsConstants.CLOSED_STORE));
            SimplePdfTableUtils.setBackgroundColor(storeNameParagraph, 166, 166, 166);
            SolidBorder solidBorder = new SolidBorder(1f);
            solidBorder.setColor(new DeviceRgb(166, 166, 166));
            SimplePdfTableUtils.setBorder(storeNameParagraph, solidBorder);
        } else {
            SimplePdfTableUtils.add(storeNameParagraph, new Text(bo.getStoreNameShort()));
            SimplePdfTableUtils.setBackgroundColor(storeNameParagraph, 0, 0, 0);
            SolidBorder solidBorder = new SolidBorder(1f);
            solidBorder.setColor(new DeviceRgb(0, 0, 0));
            SimplePdfTableUtils.setBorder(storeNameParagraph, solidBorder);
        }
        SimplePdfTableUtils.setFontColor(storeNameParagraph, 255, 255, 255);
        SimplePdfTableUtils.setAlignment(storeNameParagraph, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.TOP);
        SimplePdfTableUtils.setPadding(storeNameParagraph, 0f, 0f);
        SimplePdfTableUtils.setMargin(storeNameParagraph, 0f, 0f);
        SimplePdfTableUtils.setSize(storeNameParagraph, UnitValue.createPercentValue(100f), 14.4f);
        SimplePdfTableUtils.add(cell, storeNameParagraph);

        Paragraph qtyParagraph = SimplePdfTableUtils.createParagraph(pdfFont, 12f, bo.getQty());
        SimplePdfTableUtils.setBackgroundColor(qtyParagraph, 255, 255, 255);
        SimplePdfTableUtils.setFontColor(qtyParagraph, 0, 0, 0);
        SimplePdfTableUtils.setAlignment(qtyParagraph, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setPadding(qtyParagraph, 0f, 2f);
        SimplePdfTableUtils.setMargin(qtyParagraph, 0f, 0f);
        SimplePdfTableUtils.setSize(qtyParagraph, UnitValue.createPercentValue(100f), 17f);
        SimplePdfTableUtils.setBorder(qtyParagraph, new DottedBorder(0.3f));
        SimplePdfTableUtils.add(cell, qtyParagraph);

        SimplePdfTableUtils.setPadding(cell, 0f, 0f);
        SimplePdfTableUtils.setMargin(cell, 0f, 0f);
        SimplePdfTableUtils.setBorder(cell, Border.NO_BORDER);
        if (count % NumberConstants.NUM_INT_5 == NumberConstants.NUM_INT_0) {
            SolidBorder border = new SolidBorder(2f);
            border.setColor(new DeviceRgb(255, 255, 255));
            cell.setBorderRight(border);
        }
        if (count % NumberConstants.NUM_INT_5 == NumberConstants.NUM_INT_1) {
            SolidBorder border = new SolidBorder(2f);
            border.setColor(new DeviceRgb(255, 255, 255));
            cell.setBorderLeft(border);
        }
        detailTable.addCell(cell);
        return ++count;
    }

    private void renderTablePlaceholderCell(PdfFont pdfFont, Table detailTable, int dataSize, int redundancy) {
        for (int i = dataSize; i <= redundancy; i++) {
            Cell placeholderCell00 = SimplePdfTableUtils.createCell();
            Paragraph placeholderParagraph01 = SimplePdfTableUtils.createParagraph(pdfFont, 12f);
            SimplePdfTableUtils.setBorder(placeholderParagraph01, new SolidBorder(1f));
            SimplePdfTableUtils.setBackgroundColor(placeholderParagraph01, 0, 0, 0);
            SimplePdfTableUtils.setFontColor(placeholderParagraph01, 255, 255, 255);
            SimplePdfTableUtils.setAlignment(placeholderParagraph01, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.TOP);
            SimplePdfTableUtils.setPadding(placeholderParagraph01, 0f, 0f);
            SimplePdfTableUtils.setMargin(placeholderParagraph01, 0f, 0f);
            SimplePdfTableUtils.setSize(placeholderParagraph01, UnitValue.createPercentValue(100f), 17f);
            SimplePdfTableUtils.add(placeholderCell00, placeholderParagraph01);

            Paragraph placeholderParagraph02 = SimplePdfTableUtils.createParagraph(pdfFont, 8f);
            SimplePdfTableUtils.setBorder(placeholderParagraph02, new SolidBorder(1f));
            SimplePdfTableUtils.setBackgroundColor(placeholderParagraph02, 0, 0, 0);
            SimplePdfTableUtils.setFontColor(placeholderParagraph02, 255, 255, 255);
            SimplePdfTableUtils.setAlignment(placeholderParagraph02, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.TOP);
            SimplePdfTableUtils.setPadding(placeholderParagraph02, 0f, 0f);
            SimplePdfTableUtils.setMargin(placeholderParagraph02, 0f, 0f);
            SimplePdfTableUtils.setSize(placeholderParagraph02, UnitValue.createPercentValue(100f), 14.4f);
            SimplePdfTableUtils.add(placeholderCell00, placeholderParagraph02);

            Paragraph placeholderParagraph03 = SimplePdfTableUtils.createParagraph(pdfFont, 12f);
            SimplePdfTableUtils.setBorder(placeholderParagraph03, new DottedBorder(0.3f));
            SimplePdfTableUtils.setBackgroundColor(placeholderParagraph03, 255, 255, 255);
            SimplePdfTableUtils.setFontColor(placeholderParagraph03, 0, 0, 0);
            SimplePdfTableUtils.setAlignment(placeholderParagraph03, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
            SimplePdfTableUtils.setPadding(placeholderParagraph03, 0f, 0f);
            SimplePdfTableUtils.setMargin(placeholderParagraph03, 0f, 0f);
            SimplePdfTableUtils.setSize(placeholderParagraph03, UnitValue.createPercentValue(100f), 17f);
            SimplePdfTableUtils.add(placeholderCell00, placeholderParagraph03);

            SimplePdfTableUtils.setPadding(placeholderCell00, 0f, 0f);
            SimplePdfTableUtils.setMargin(placeholderCell00, 0f, 0f);
            SimplePdfTableUtils.setBorder(placeholderCell00, Border.NO_BORDER);
            if (i % NumberConstants.NUM_INT_5 == NumberConstants.NUM_INT_0) {
                SolidBorder border = new SolidBorder(2f);
                border.setColor(new DeviceRgb(255, 255, 255));
                placeholderCell00.setBorderRight(border);
            }
            if (i % NumberConstants.NUM_INT_5 == NumberConstants.NUM_INT_1) {
                SolidBorder border = new SolidBorder(2f);
                border.setColor(new DeviceRgb(255, 255, 255));
                placeholderCell00.setBorderLeft(border);
            }
            SimplePdfTableUtils.addCell(detailTable, placeholderCell00);
        }
    }

    private List<DistributionInstructionVo> getDistributionInstructionData(List<DistributionInstructionPoVo> beanList) {
        List<DistributionInstructionVo> list = BeanUtil.copyToList(beanList, DistributionInstructionVo.class);
        Integer totalCount = list.stream().reduce(0, (sum, bo) -> sum + Integer.parseInt(StringUtils.isBlank(bo.getQty()) ? NumberConstants.NUM_0 : bo.getQty()), Integer::sum);
        list.forEach(tmp -> {
            tmp.setTotalCount(NumberUtil.decimalFormat(FormatConstants.THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL, totalCount));
            tmp.setQty(StringUtils.isBlank(tmp.getQty()) ? StringConstants.BLANK : NumberUtil.decimalFormat(FormatConstants.THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL, Integer.parseInt(tmp.getQty())));
        });
        return list;
    }
}
