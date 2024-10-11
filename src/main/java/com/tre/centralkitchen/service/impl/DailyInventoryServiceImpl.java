package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
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
import com.tre.centralkitchen.common.constant.FileTypeConstants;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.constant.business.InstructionsConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.PdfTablePropInfo;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.excel.GetResultListReadListener;
import com.tre.centralkitchen.common.utils.FileUtils;
import com.tre.centralkitchen.common.utils.HeadContentCellStyle;
import com.tre.centralkitchen.common.utils.ReflectUtils;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.common.utils.SimplePdfTableUtils;
import com.tre.centralkitchen.domain.bo.system.DailyInventoryBo;
import com.tre.centralkitchen.domain.bo.system.DailyInventorySearchBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.po.TrInventory;
import com.tre.centralkitchen.domain.po.TrStock;
import com.tre.centralkitchen.domain.vo.common.CenterListVo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.system.DailyInventoryVo;
import com.tre.centralkitchen.mapper.DailyInventoryMapper;
import com.tre.centralkitchen.mapper.DailyInventoryUpdateInventoryMapper;
import com.tre.centralkitchen.service.DailyInventoryService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2023-12-22 9:24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyInventoryServiceImpl implements DailyInventoryService {

    private static final String PDF_NAME = "在庫_日次棚卸";

    private final DailyInventoryMapper dailyInventoryMapper;
    private final DailyInventoryUpdateInventoryMapper dailyInventoryUpdateInventoryMapper;
    private final TokenTakeApart tokenTakeApart;
    private final MasterService masterService;
    private final SimpleFmtTableUtils simpleFmtTableUtils;

    /**
     * 在庫_日次棚卸の検索
     *
     * @param pageParam Paging parameter
     * @param param     Query parameter
     * @return Json object of data list and total data
     */
    @Override
    public TableDataInfo<DailyInventoryVo> search(PageQuery pageParam, DailyInventorySearchBo param) {
        LocalDate stockDate = LocalDate.now();
        Page<DailyInventoryVo> page = dailyInventoryMapper.queryList(param, stockDate, pageParam.build());
        // result handle
        List<DailyInventoryVo> dataList = this.resultHandle(page.getRecords(), param.getCenterId());
        return TableDataInfo.build(dataList);
    }

    /**
     * 在庫_日次棚卸の棚卸登録
     *
     * @param dataList DailyInventoryBo List
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(List<DailyInventoryBo> dataList) {
        LocalDate stockDate = LocalDate.now();
        // update 在庫トラン
        this.updateTrStock(dataList, stockDate);
        this.insertOrUpdateInventory(dataList, stockDate);
    }

    /**
     * 日次棚卸のPDF出力
     *
     * @param param    Query parameter
     * @param response response
     */
    @Override
    public void downloadDailyInventoryPdf(DailyInventorySearchBo param, HttpServletResponse response) {
        LocalDate stockDate = LocalDate.now();
        List<DailyInventoryVo> dataList = dailyInventoryMapper.queryList(param, stockDate);
        if (CollUtil.isEmpty(dataList)) {
            throw new SysBusinessException(SysConstantInfo.NO_DATA_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.NO_DATA_ERROR_CODE);
        }
        // result handle
        dataList = this.resultHandle(dataList, param.getCenterId());

        PdfTablePropInfo prop = SimplePdfTableUtils.getTableProp(DailyInventoryVo.class);

        Map<Integer, List<DailyInventoryVo>> groupMap = dataList.stream().collect(Collectors.groupingBy(DailyInventoryVo::getWarehouseId));
        List<Integer> warehouseIdList = groupMap.keySet().stream().sorted().collect(Collectors.toList());
        LocalDateTime curDateTime = LocalDateTime.now();
        String currentTime = curDateTime.format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_12_WITH_02_SEPARATOR));
        String fileName = SimplePdfTableUtils.getFileName(PDF_NAME, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);

        int warehouseIdSize = warehouseIdList.size();
        try (OutputStream out = response.getOutputStream(); PdfDocument pdfDocument = new PdfDocument(new PdfWriter(out))) {
            SimplePdfTableUtils.setResponseHeader(response, fileName);
            PdfFont pdfFont = SimplePdfTableUtils.createFont(SysConstants.PDF_FONT_PATH);
            Document document = new Document(pdfDocument, PageSize.A4, false);
            SimplePdfTableUtils.setMargin(document, 15f, 15f);

            for (int i = 0; i < warehouseIdSize; i++) {
                Integer warehouseId = warehouseIdList.get(i);
                List<DailyInventoryVo> groupList = groupMap.get(warehouseId);
                if (i != 0) {
                    SimplePdfTableUtils.nextPage(document);
                }
                // generate tables for each group separately
                this.renderTablePage(groupList, prop, pdfFont, document, currentTime);
            }
            SimplePdfTableUtils.renderPageNum(pdfDocument, document);
            document.flush();
            document.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SysBusinessException();
        }
    }

    /**
     * 日次棚卸のCSV出力
     *
     * @param param    Query parameter
     * @param response response
     */
    @Override
    public void downloadCsvOutput(DailyInventorySearchBo param, HttpServletResponse response) {
        LocalDate stockDate = LocalDate.now();
        List<DailyInventoryVo> dataList = dailyInventoryMapper.queryList(param, stockDate);
        // result handle
        dataList = this.resultHandle(dataList, param.getCenterId());
        SimpleCsvTableUtils.easyCsvExport(response, PDF_NAME, dataList, DailyInventoryVo.class);
    }

    /**
     * 日次棚卸のExcel出力
     *
     * @param param    Query parameter
     * @param response response
     */
    @Override
    public void downloadExcelOutput(DailyInventorySearchBo param, HttpServletResponse response) throws IOException {
        LocalDate stockDate = LocalDate.now();
        List<DailyInventoryVo> dataList = dailyInventoryMapper.queryList(param, stockDate);
        // result handle
        dataList = this.resultHandle(dataList, param.getCenterId());

        // download fmt data file
        this.downloadFmtDataFile(dataList, response);
    }

    /**
     * 日次棚卸のFMT取込
     *
     * @param bo       UploadBo
     * @param file     excel
     * @param response response
     * @return File Back Error
     * @throws Exception Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileBackErrorVo fmtImport(UploadBo bo, MultipartFile file, HttpServletResponse response) throws Exception {
        if (!FileUtils.checkFileType(file)) {
            throw new SysBusinessException(SysConstantInfo.FILE_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_TYPE_ERROR_CODE);
        }
        String fileName = file.getOriginalFilename();
        if (!StrUtil.contains(fileName, SysConstants.DAILY_INVENTORY_FILE_NAME)) {
            throw new SysBusinessException(SysConstantInfo.FILE_FMT_NAME_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_FMT_NAME_ERROR_CODE);
        }

        List<DailyInventoryVo> excelDataList = new ArrayList<>();
        GetResultListReadListener<DailyInventoryVo> listener = new GetResultListReadListener<>(excelDataList);
        EasyExcelFactory.read(file.getInputStream(), DailyInventoryVo.class, listener).sheet().headRowNumber(2).doRead();
        if (CollUtil.isEmpty(excelDataList)) {
            throw new SysBusinessException(SysConstantInfo.FILE_DATA_EMPTY_MSG);
        }
        // excel data check
        FileBackErrorVo fileBackErrorVo = this.checkUpdateFileData(excelDataList);
        // contains any errors and does not perform import
        if (StrUtil.isNotEmpty(fileBackErrorVo.getFileError())) {
            return fileBackErrorVo;
        }
        // DailyInventoryVo to DailyInventoryBo
        List<DailyInventoryBo> dataList = this.conversion(excelDataList, bo);
        // 在庫_日次棚卸の棚卸登録
        this.update(dataList);

        return fileBackErrorVo;
    }

    /**
     * download fmt data file
     *
     * @param dataList data list
     * @param response response
     * @throws IOException IOException
     */
    private void downloadFmtDataFile(List<DailyInventoryVo> dataList, HttpServletResponse response) throws IOException {
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = SysConstants.DAILY_INVENTORY_FILE_NAME + "_" + curTime;
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), DailyInventoryVo.class)
                .withTemplate(simpleFmtTableUtils.getFmtFileInputStream("dailyInventory"))
                .registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                )
                .needHead(false).sheet().doWrite(dataList);
        simpleFmtTableUtils.downloadTempFile(response, fileName, FileTypeConstants.CONTENT_TYPE_XLSX);
    }

    /**
     * download error data file
     *
     * @param resList error data list
     * @return error file name
     */
    private String downloadErrorFileUpdate(List<DailyInventoryVo> resList) {
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = StrFormatter.format(SysConstantInfo.FMT_FILE_NAME_WITH_TIME_AND_ERROR_MSG,
                SysConstants.DAILY_INVENTORY_FILE_NAME, curTime, FileTypeConstants.XLSX);
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), DailyInventoryVo.class)
                .withTemplate(simpleFmtTableUtils.getFmtFileInputStream("dailyInventory"))
                .registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                ).needHead(false).sheet().doWrite(resList);
        return fileName;
    }

    /**
     * generate tables for each group separately
     *
     * @param dataList    each group record
     * @param prop        header parameter settings
     * @param pdfFont     pdf font
     * @param document    document
     * @param currentTime 印刷日時
     */
    private void renderTablePage(List<DailyInventoryVo> dataList,
                                 PdfTablePropInfo prop,
                                 PdfFont pdfFont,
                                 Document document,
                                 String currentTime) {
        String warehouseName = dataList.get(0).getWarehouseName();
        // 35 records per page
        List<List<DailyInventoryVo>> splitList = CollUtil.split(dataList, 35);
        int size = splitList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                SimplePdfTableUtils.nextPage(document);
            }
            List<DailyInventoryVo> mapList = splitList.get(i);
            Table detailTable = SimplePdfTableUtils.createTable(prop.getColumnsWidth(), true);
            // create a header for each page
            this.renderTotalTableHead(currentTime, warehouseName, document, pdfFont);

            // create header per page
            this.renderDetailTableHead(prop, pdfFont, detailTable);

            for (DailyInventoryVo data : mapList) {
                // table add content
                this.renderDetailTableBody(prop, pdfFont, data, detailTable);
            }
            SimplePdfTableUtils.add(document, new Div().add(detailTable));
        }
    }

    /**
     * create a header for each page
     *
     * @param currentTime   印刷日時
     * @param warehouseName 倉庫名
     * @param document      document
     * @param pdfFont       pdf font
     */
    private void renderTotalTableHead(String currentTime, String warehouseName, Document document, PdfFont pdfFont) {
        List<UnitValue> frameUnitValueList = new ArrayList<>();
        frameUnitValueList.add(UnitValue.createPercentValue(15f));
        frameUnitValueList.add(UnitValue.createPercentValue(10f));
        frameUnitValueList.add(UnitValue.createPercentValue(35f));
        frameUnitValueList.add(UnitValue.createPercentValue(22f));
        Table totalHeaderTable = SimplePdfTableUtils.createTable(frameUnitValueList.toArray(UnitValue[]::new), true);

        // 印刷日時
        Cell printTimeCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 8f, 1, 4);
        SimplePdfTableUtils.add(printTimeCellTotalPage, SimplePdfTableUtils.createParagraph(InstructionsConstants.PRINT_DATE_TIME + currentTime));
        SimplePdfTableUtils.setAlignment(printTimeCellTotalPage, TextAlignment.RIGHT, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(printTimeCellTotalPage, Border.NO_BORDER);

        Cell warehouseNameCellTotalPage = SimplePdfTableUtils.createCell(pdfFont, 12f);
        SimplePdfTableUtils.add(warehouseNameCellTotalPage, SimplePdfTableUtils.createParagraph(warehouseName));
        SimplePdfTableUtils.setAlignment(warehouseNameCellTotalPage, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(warehouseNameCellTotalPage, Border.NO_BORDER);

        Cell textCell = SimplePdfTableUtils.createCell(pdfFont, 10f, 1, 3);
        textCell.setFontColor(new DeviceRgb(255, 0, 0));
        SimplePdfTableUtils.add(textCell, SimplePdfTableUtils.createParagraph("賞味期限が3種類以上あるときは、直近の日付を記入してください。"));
        SimplePdfTableUtils.setAlignment(textCell, TextAlignment.LEFT, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM);
        SimplePdfTableUtils.setBorder(textCell, Border.NO_BORDER);

        SimplePdfTableUtils.addCell(totalHeaderTable, printTimeCellTotalPage, warehouseNameCellTotalPage, textCell);
        document.add(totalHeaderTable);
    }

    /**
     * create header per page
     *
     * @param prop        header parameter settings
     * @param pdfFont     pdf font
     * @param detailTable the table where the header is located
     */
    private void renderDetailTableHead(PdfTablePropInfo prop, PdfFont pdfFont, Table detailTable) {
        // [JAN, 原材料名, 規格, 入数, ベンダ, ベンダー名, ケース, 袋or本, ケース, 袋or本, 賞味期限1, 賞味期限2]
        List<String> firstLevelHeaderList = new ArrayList<>(Arrays.asList(prop.getTitleList()));
        List<String> firstLevelMergeHeaderList = Arrays.asList("論理在庫", "在庫カウント数");
        List<String> secondLevelHeaderList = Arrays.asList("ケース", "袋or本");
        firstLevelHeaderList.removeAll(Arrays.asList("ケース", "袋or本"));
        int index = firstLevelHeaderList.indexOf("賞味期限1");
        firstLevelHeaderList.addAll(index, firstLevelMergeHeaderList);

        for (String title : firstLevelHeaderList) {
            int rowSpan = 2;
            int colSpan = 1;
            if (firstLevelMergeHeaderList.contains(title)) {
                rowSpan = 1;
                colSpan = 2;
            }
            Cell cell = SimplePdfTableUtils.createCell(pdfFont, 10f, rowSpan, colSpan);
            this.setDetailTableHeadStyle(detailTable, title, cell);
        }

        for (String header : firstLevelMergeHeaderList) {
            for (String title : secondLevelHeaderList) {
                Cell cell = SimplePdfTableUtils.createCell(pdfFont, 10f);
                this.setDetailTableHeadStyle(detailTable, title, cell);
            }
        }
    }

    /**
     * set detail table head style
     *
     * @param detailTable detail table
     * @param title       title
     * @param cell        cell
     */
    private void setDetailTableHeadStyle(Table detailTable, String title, Cell cell) {
        SimplePdfTableUtils.add(cell, SimplePdfTableUtils.createParagraph(title));
        SimplePdfTableUtils.setAlignment(cell, TextAlignment.CENTER, HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE);
        SimplePdfTableUtils.setBorder(cell, new SolidBorder(0.2f));
        SimplePdfTableUtils.setSize(cell, null, 20f);
        SimplePdfTableUtils.setBackgroundColor(cell, 230, 230, 230);
        SimplePdfTableUtils.addCell(detailTable, cell);
    }

    /**
     * table add content
     *
     * @param prop        header parameter settings
     * @param pdfFont     pdf font
     * @param data        record per row
     * @param detailTable the table where the row is located
     */
    private void renderDetailTableBody(PdfTablePropInfo prop, PdfFont pdfFont, DailyInventoryVo data, Table detailTable) {
        for (int i = 0; i < prop.getColumn(); i++) {
            Cell cell = SimplePdfTableUtils.createCell(pdfFont, prop.getFontSizeList()[i]);

            Object o = ReflectUtils.invokeGet(data, prop.getMethodList()[i]);
            if (o instanceof Integer || o instanceof Long) {
                o = NumberUtil.decimalFormat(FormatConstants.THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL, Long.parseLong(o.toString()));
            }
            SimplePdfTableUtils.add(cell, SimplePdfTableUtils.createParagraph(o == null ? StringConstants.BLANK : o.toString()));
            if (prop.getIsBoldList()[i]) {
                cell.setBold();
            }
            SimplePdfTableUtils.setAlignment(cell, prop.getTList()[i], prop.getHList()[i], prop.getVList()[i]);
            SimplePdfTableUtils.setBorder(cell, new SolidBorder(0.2f));
            SimplePdfTableUtils.setSize(cell, null, 20f);
            SimplePdfTableUtils.setPadding(cell, 0f, 2f);
            SimplePdfTableUtils.setMargin(cell, 0f, 0f);
            detailTable.addCell(cell);
        }

    }

    /**
     * result handle
     *
     * @param dataList DailyInventorySearchVo List
     * @param centerId プロセスセンターID
     */
    private List<DailyInventoryVo> resultHandle(List<DailyInventoryVo> dataList, Integer centerId) {
        List<CenterListVo> centerListVoList = masterService.getCenterList();
        String centerName = centerListVoList.stream()
                .filter(center -> NumberUtil.equals(centerId, center.getCenterId()))
                .map(CenterListVo::getCenterName)
                .findFirst()
                .orElse("");
        Function<LocalDate, String> formatFunction = localDate -> {
            if (Objects.isNull(localDate)) {
                return null;
            }
            LocalDateTime time = localDate.atTime(0, 0, 0);
            return DateUtil.format(time, FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01);
        };
        return dataList.stream().map(data -> {
            DailyInventoryVo.DailyInventoryVoBuilder builder = data.toBuilder();

            builder.centerName(centerName)
                    // 倉庫名
                    .warehouseName(data.getWarehouseName())
                    .centerId(centerId)
                    // 論理在庫
                    .qy(data.getQy())
                    // カウント原価(ケース)
                    .caseCost(data.getCaseCost())
            ;

            // id = key(centerId + lineId + warehouseId + itemId + vendor_id)
            List<Object> keyList = Arrays.asList(data.getCenterId(), data.getLineId(),
                    data.getWarehouseId(), data.getItemId(), data.getVendorId());
            String id = keyList.stream().map(String::valueOf).collect(Collectors.joining("-"));
            builder.id(id);

            // 規格 = 規格分量 + ‘ ’ + ユニット名
            String specName = data.getNSzName() + " " + data.getUnitName();
            builder.specName(specName);

            // ベンダー名
            String supplierName = StrUtil.replace(data.getSupplierName(), "株式会社", "");
            supplierName = StrUtil.trim(supplierName);
            builder.supplierName(supplierName);

            BigDecimal qy = Objects.isNull(data.getQy()) ? new BigDecimal(0) : new BigDecimal(data.getQy());
            // 論理在庫数の整数部分:ケース
            int caseQy = qy.intValue();
            builder.caseQy(caseQy);

            BigDecimal innerCaseQty = Objects.isNull(data.getInnerCaseQty()) ? new BigDecimal(0) : new BigDecimal(data.getInnerCaseQty());
            // 論理在庫数の少数部分：袋or本  論理在庫数の少数部分×商品入数
            BigDecimal shortQy = qy.subtract(new BigDecimal(caseQy)).multiply(innerCaseQty);
            // 4.95 -> 5 ， 4.2 -> 2
            shortQy = NumberUtil.round(shortQy, 0);
            builder.shortQy(shortQy.intValue());

            innerCaseQty = Objects.isNull(data.getInnerCaseQty()) || data.getInnerCaseQty() == 0
                    ? new BigDecimal(1) : new BigDecimal(data.getInnerCaseQty());
            BigDecimal caseCost = Objects.isNull(data.getCaseCost()) ? new BigDecimal(0) : new BigDecimal(data.getCaseCost());
            // カウント原価(袋or本) = カウント原価(ケース) / 入数
            BigDecimal shortCost = NumberUtil.div(caseCost, innerCaseQty, 2);
            builder.shortCost(shortCost.toString());

            // 在庫金額case_qy*case_cost + short_qy*short_cost
            BigDecimal am = new BigDecimal(caseQy).multiply(caseCost)
                    .add(
                            shortQy.multiply(shortCost)
                    );
            am = NumberUtil.round(am, 2);
            builder.am(am.toString());

            // 賞味期限
            builder.expTimeDate01Str(formatFunction.apply(data.getExpTimeDate01()));
            builder.expTimeDate02Str(formatFunction.apply(data.getExpTimeDate02()));
            return builder.build();
        }).collect(Collectors.toList());
    }

    /**
     * 在庫トラン
     *
     * @param dataList  data list
     * @param stockDate 在庫日
     */
    private void updateTrStock(List<DailyInventoryBo> dataList, LocalDate stockDate) {
        List<TrStock> trStockList = dataList.stream()
                .filter(data -> Objects.nonNull(data.getCaseQy()) || Objects.nonNull(data.getShortQy()))
                .map(data -> {
                    TrStock trStock = BeanUtil.toBean(data, TrStock.class);

                    BigDecimal qy = this.calculateQy(data);
                    BigDecimal caseCost = new BigDecimal(data.getCaseCost());
                    BigDecimal am = qy.multiply(caseCost);

                    return trStock.toBuilder()
                            .stockDate(stockDate)
                            .centerId(data.getCenterId())
                            .warehouseId(data.getWarehouseId())
                            .itemId(data.getItemId())
                            .cost(caseCost)
                            .qy(qy)
                            .am(am)
                            .build();
                }).collect(Collectors.toList());
        if (CollUtil.isEmpty(trStockList)) {
            return;
        }
        dailyInventoryMapper.updateByQueryWrapper(trStockList, trStock ->
                new LambdaUpdateWrapper<TrStock>()
                        .eq(TrStock::getStockDate, trStock.getStockDate())
                        .eq(TrStock::getCenterId, trStock.getCenterId())
                        .eq(TrStock::getWarehouseId, trStock.getWarehouseId())
                        .eq(TrStock::getItemId, trStock.getItemId())
        );
    }

    /**
     * insert or update 棚卸トラン
     *
     * @param dataList  data list
     * @param stockDate 在庫日
     */
    private void insertOrUpdateInventory(List<DailyInventoryBo> dataList, LocalDate stockDate) {
        if (CollUtil.isEmpty(dataList)) {
            return;
        }
        Integer countUserId = Integer.parseInt(tokenTakeApart.takeDecryptedUserId());
        List<TrInventory> inventoryList = dataList.stream()
                .map(data -> {
                    TrInventory trInventory = BeanUtil.toBean(data, TrInventory.class);

                    BigDecimal qy = this.calculateQy(data);

                    return trInventory.toBuilder()
                            .invDate(stockDate)
                            .centerId(data.getCenterId())
                            .warehouseId(data.getWarehouseId())
                            .itemId(data.getItemId())
                            .caseQy(data.getCaseQy())
                            .innerQy(data.getShortQy())
                            .qy(qy)
                            .exptime01Date(data.getExpTimeDate01())
                            .exptime02Date(data.getExpTimeDate02())
                            .countUserId(countUserId)
                            .build();
                }).collect(Collectors.toList());
        dailyInventoryUpdateInventoryMapper.insertOrUpdateByQueryWrapper(inventoryList, trInventory ->
                new LambdaUpdateWrapper<TrInventory>()
                        .eq(TrInventory::getInvDate, trInventory.getInvDate())
                        .eq(TrInventory::getCenterId, trInventory.getCenterId())
                        .eq(TrInventory::getWarehouseId, trInventory.getWarehouseId())
                        .eq(TrInventory::getItemId, trInventory.getItemId()));
    }

    private BigDecimal calculateQy(DailyInventoryBo bo) {
        BigDecimal innerCaseQty = Objects.isNull(bo.getInnerCaseQty()) || bo.getInnerCaseQty() == 0
                ? new BigDecimal(1) : new BigDecimal(bo.getInnerCaseQty());
        BigDecimal castQy = Objects.isNull(bo.getCaseQy()) ? new BigDecimal(0) : new BigDecimal(bo.getCaseQy());
        BigDecimal shortQy = Objects.isNull(bo.getShortQy()) ? new BigDecimal(0) : new BigDecimal(bo.getShortQy());
        return castQy.add(NumberUtil.div(shortQy, innerCaseQty, 2));
    }

    /**
     * DailyInventoryVo to DailyInventoryBo
     *
     * @param excelDataList excel data
     * @param bo            upload param
     * @return DailyInventoryBo
     */
    private List<DailyInventoryBo> conversion(List<DailyInventoryVo> excelDataList, UploadBo bo) {
        Function<String, LocalDate> strToLocalDate = str -> {
            if (StrUtil.isEmpty(str)) {
                return null;
            }
            return DateUtil.parseDate(str).toLocalDateTime().toLocalDate();
        };

        return excelDataList.stream()
                .map(exd -> {
                    DailyInventoryBo dailyInventoryBo = BeanUtil.toBean(bo, DailyInventoryBo.class);
                    Integer caseQy = Objects.isNull(exd.getCaseQyTemp()) ? exd.getCaseQy() : exd.getCaseQyTemp();
                    Integer shortQy = Objects.isNull(exd.getShortQyTemp()) ? exd.getShortQy() : exd.getShortQyTemp();
                    return dailyInventoryBo.toBuilder()
                            .centerId(bo.getCenterId())
                            .warehouseId(exd.getWarehouseId())
                            .itemId(exd.getItemId())
                            .innerCaseQty(exd.getInnerCaseQty())
                            .caseQy(caseQy)
                            .shortQy(shortQy)
                            .caseCost(exd.getCaseCost())
                            .expTimeDate01(strToLocalDate.apply(exd.getExpTimeDate01Str()))
                            .expTimeDate02(strToLocalDate.apply(exd.getExpTimeDate02Str()))
                            .build();
                }).collect(Collectors.toList());
    }

    /**
     * data check
     *
     * @param excelDataList excel data list
     * @return check result
     */
    private FileBackErrorVo checkUpdateFileData(List<DailyInventoryVo> excelDataList) {
        FileBackErrorVo fileBackErrorVo = new FileBackErrorVo();
        // start check
        Set<Boolean> checkResult = new HashSet<>();
        Set<Boolean> otherFlag = new HashSet<>();

        Predicate<Integer> numPredicate = num -> {
            if (Objects.nonNull(num)) {
                return num < 0 || num > 9999;
            }
            return false;
        };

        Predicate<String> datePredicate = dateStr -> {
            if (Objects.nonNull(dateStr)) {
                return !dateStr.matches(FormatConstants.REGEX_PATTERN_DATE);
            }
            return false;
        };

        for (DailyInventoryVo exd : excelDataList) {
            // format
            HashSet<String> formatList = this.checkFormat(exd, numPredicate, datePredicate);
            if (!formatList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                exd.setErrorMsg(StrFormatter.format(SysConstantInfo.INCORRECT, Joiner.on("、").join(formatList)));
            }
        }
        if (checkResult.contains(true)) {
            fileBackErrorVo.setFileError(this.downloadErrorFileUpdate(excelDataList));
        }
        fileBackErrorVo.setOtherFlag(otherFlag.contains(true));
        fileBackErrorVo.setPriceFlag(false);
        fileBackErrorVo.setDateFlag(false);
        return fileBackErrorVo;
    }

    /**
     * check format
     *
     * @param exd           excel data
     * @param numPredicate  num check format
     * @param datePredicate date check format
     * @return check result
     */
    private HashSet<String> checkFormat(DailyInventoryVo exd, Predicate<Integer> numPredicate, Predicate<String> datePredicate) {
        HashSet<String> list = new HashSet<>();

        if (numPredicate.test(exd.getCaseQyTemp())) {
            list.add("論理在庫数の整数部分:ケース");
        }
        if (numPredicate.test(exd.getShortQyTemp())) {
            list.add("論理在庫数の少数部分：袋or本");
        }

        if (datePredicate.test(exd.getExpTimeDate01Str())) {
            list.add("賞味期限1");
        }
        if (datePredicate.test(exd.getExpTimeDate02Str())) {
            list.add("賞味期限2");
        }
        return list;
    }
}
