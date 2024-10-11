package com.tre.centralkitchen.common.utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.tre.centralkitchen.common.annotation.PdfTableProp;
import com.tre.centralkitchen.common.domain.PdfTablePropInfo;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>Simple Pdf Export Util</p>
 *
 * @author 10225441
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimplePdfTableUtils {
    /**
     * content type (default pdf).
     */
    private static final String CONTENT_TYPE = "application/pdf";
    /**
     * character encoding (default utf-8).
     */
    private static final String CHARACTER_ENCODING = "UTF-8";
    /**
     * content disposition key.
     */
    private static final String CONTENT_DISPOSITION = "Content-disposition";
    /**
     * content disposition value prefix.
     */
    private static final String CONTENT_DISPOSITION_PREFIX = "attachment;filename=";
    /**
     * content disposition value suffix.
     */
    private static final String CONTENT_DISPOSITION_SUFFIX = ".pdf";
    /**
     * error message.
     */
    private static final String RSP_ERROR_MESSAGE = "HttpServletResponse is null!";

    /**
     * <p>Get properties of pdf table.</p>
     *
     * @param clazz Bean's class
     * @return Pdf properties
     */
    public static PdfTablePropInfo getTableProp(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        int columnNum = 0;
        for (Field field : fields) {
            if (field.isAnnotationPresent(PdfTableProp.class)) {
                columnNum++;
            }
        }
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
        }
        PdfTablePropInfo prop = new PdfTablePropInfo();
        prop.setColumn(columnNum);
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

    public static PdfFont createFont(String fontPath) throws IOException {
        return PdfFontFactory.createFont(new ClassPathResource(fontPath).getPath() + ",0", PdfEncodings.IDENTITY_H);
    }

    public static Table createTable(UnitValue[] unitValues, boolean isFull) {
        Table table = new Table(unitValues);
        if (isFull) {
            table.setWidth(UnitValue.createPercentValue(100f));
            table.setHeight(UnitValue.createPercentValue(100f));
        }
        return table;
    }

    public static Paragraph createParagraph(PdfFont font, float fontSize, String text) {
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(font).setFontSize(fontSize);
        return paragraph;
    }

    public static Paragraph createParagraph(PdfFont font, float fontSize) {
        Paragraph paragraph = createParagraph();
        paragraph.setFont(font).setFontSize(fontSize);
        return paragraph;
    }

    public static Paragraph createParagraph() {
        return new Paragraph();
    }

    public static Paragraph createParagraph(String text) {
        return new Paragraph(text);
    }

    public static Cell createCell() {
        return createCell(1, 1);
    }

    public static Cell createCell(int row, int col) {
        return new Cell(row, col);
    }

    public static Cell createCell(PdfFont font, float fontSize) {
        return createCell(font, fontSize, 1, 1);
    }

    public static Cell createCell(PdfFont font, float fontSize, int row, int col) {
        Cell cell = createCell(font, row, col);
        cell.setFontSize(fontSize);
        return cell;
    }

    public static Cell createCell(PdfFont font, int row, int col) {
        Cell cell = createCell(row, col);
        cell.setFont(font);
        return cell;
    }

    public static void setSize(Cell cell, Float width, Float height) {
        if (width != null && width.compareTo(0f) > 0) {
            cell.setWidth(width);
        }
        if (height != null && height.compareTo(0f) > 0) {
            cell.setHeight(height);
        }
    }

    public static void setSize(Paragraph paragraph, Float width, Float height) {
        if (width != null && width.compareTo(0f) > 0) {
            paragraph.setWidth(width);
        }
        if (height != null && height.compareTo(0f) > 0) {
            paragraph.setHeight(height);
        }
    }

    public static void setSize(Paragraph paragraph, UnitValue width, Float height) {
        paragraph.setWidth(width);
        if (height != null && height.compareTo(0f) > 0) {
            paragraph.setHeight(height);
        }
    }

    public static void setSize(Paragraph paragraph, Float width, UnitValue height) {
        if (width != null && width.compareTo(0f) > 0) {
            paragraph.setWidth(width);
        }
        paragraph.setHeight(height);
    }

    public static void setSize(Paragraph paragraph, UnitValue width, UnitValue height) {
        paragraph.setWidth(width);
        paragraph.setHeight(height);
    }

    public static void setPadding(Paragraph paragraph, Float v, Float h) {
        if (v != null) {
            paragraph.setPaddingTop(v);
            paragraph.setPaddingBottom(v);
        }
        if (h != null) {
            paragraph.setPaddingLeft(h);
            paragraph.setPaddingRight(h);
        }
    }

    public static void setPadding(Paragraph paragraph, Float top, Float right, Float bottom, Float left) {
        paragraph.setPaddingTop(top);
        paragraph.setPaddingRight(right);
        paragraph.setPaddingBottom(bottom);
        paragraph.setPaddingLeft(left);
    }

    public static void setPadding(Cell cell, Float v, Float h) {
        if (v != null) {
            cell.setPaddingTop(v);
            cell.setPaddingBottom(v);
        }
        if (h != null) {
            cell.setPaddingLeft(h);
            cell.setPaddingRight(h);
        }
    }

    public static void setMargin(Paragraph paragraph, Float v, Float h) {
        if (v != null) {
            paragraph.setMarginTop(v);
            paragraph.setMarginBottom(v);
        }
        if (h != null) {
            paragraph.setMarginLeft(h);
            paragraph.setMarginRight(h);
        }
    }

    public static void setMargin(Paragraph paragraph, Float top, Float right, Float bottom, Float left) {
        paragraph.setMarginTop(top);
        paragraph.setMarginRight(right);
        paragraph.setMarginBottom(bottom);
        paragraph.setMarginLeft(left);
    }

    public static void setMargin(Cell cell, Float v, Float h) {
        if (v != null) {
            cell.setMarginTop(v);
            cell.setMarginBottom(v);
        }
        if (h != null) {
            cell.setMarginLeft(h);
            cell.setMarginRight(h);
        }
    }

    public static void setMargin(Cell cell, Float top, Float right, Float bottom, Float left) {
        cell.setMarginTop(top);
        cell.setMarginRight(right);
        cell.setMarginBottom(bottom);
        cell.setMarginLeft(left);
    }

    public static void setMargin(Table table, Float v, Float h) {
        if (v != null) {
            table.setMarginTop(v);
            table.setMarginBottom(v);
        }
        if (h != null) {
            table.setMarginLeft(h);
            table.setMarginRight(h);
        }
    }

    public static void setMargin(Table table, Float top, Float right, Float bottom, Float left) {
        table.setMarginTop(top);
        table.setMarginRight(right);
        table.setMarginBottom(bottom);
        table.setMarginLeft(left);
    }

    public static void setMargin(Document document, Float v, Float h) {
        if (v != null) {
            document.setTopMargin(v);
            document.setBottomMargin(v);
        }
        if (h != null) {
            document.setLeftMargin(h);
            document.setRightMargin(h);
        }
    }

    public static void setMargin(Document document, Float top, Float right, Float bottom, Float left) {
        document.setTopMargin(top);
        document.setRightMargin(right);
        document.setBottomMargin(bottom);
        document.setLeftMargin(left);
    }

    public static void setBorder(Cell cell, Border border) {
        cell.setBorder(border);
    }

    public static void setBorder(Cell cell, Border top, Border right, Border bottom, Border left) {
        cell.setBorderTop(top);
        cell.setBorderRight(right);
        cell.setBorderBottom(bottom);
        cell.setBorderLeft(left);
    }

    public static void setBorder(Paragraph paragraph, Border border) {
        paragraph.setBorder(border);
    }

    public static void setBorder(Paragraph paragraph, Border top, Border right, Border bottom, Border left) {
        paragraph.setBorderTop(top);
        paragraph.setBorderRight(right);
        paragraph.setBorderBottom(bottom);
        paragraph.setBorderLeft(left);
    }

    public static void setAlignment(Cell cell, TextAlignment ta, HorizontalAlignment ha, VerticalAlignment va) {
        cell.setTextAlignment(ta);
        cell.setHorizontalAlignment(ha);
        cell.setVerticalAlignment(va);
    }

    public static void setAlignment(Paragraph paragraph, TextAlignment ta, HorizontalAlignment ha, VerticalAlignment va) {
        paragraph.setTextAlignment(ta);
        paragraph.setHorizontalAlignment(ha);
        paragraph.setVerticalAlignment(va);
    }

    public static void nextPage(Document document) {
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
    }

    public static void setBackgroundColor(Cell cell, int r, int g, int b) {
        cell.setBackgroundColor(new DeviceRgb(r, g, b));
    }

    public static void setBackgroundColor(Paragraph paragraph, int r, int g, int b) {
        paragraph.setBackgroundColor(new DeviceRgb(r, g, b));
    }

    public static void setFontColor(Paragraph paragraph, int r, int g, int b) {
        paragraph.setFontColor(new DeviceRgb(r, g, b));
    }

    public static void add(Document document, IBlockElement... elements) {
        Arrays.stream(elements).forEach(document::add);
    }

    public static void add(Paragraph paragraph, Text... texts) {
        Arrays.stream(texts).forEach(paragraph::add);
    }

    public static void add(Cell cell, Paragraph... paragraphs) {
        Arrays.stream(paragraphs).forEach(cell::add);
    }

    public static void addCell(Table table, Cell... cells) {
        Arrays.stream(cells).forEach(table::addCell);
    }

    public static void addHeaderCell(Table table, Cell... cells) {
        Arrays.stream(cells).forEach(table::addHeaderCell);
    }

    public static void addFooterCell(Table table, Cell... cells) {
        Arrays.stream(cells).forEach(table::addFooterCell);
    }

    /**
     * <p>Get pdf file's name.</p>
     *
     * @param fileName Pdf file's name
     * @return Encode pdf file's name
     */
    public static String getFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("\\+", "%20");
    }

    /**
     * <p>Get pdf file's name with timestamp.</p>
     *
     * @param fileName    Pdf file's name.
     * @param datePattern Date's format(example: yyyyMMdd)
     * @return Encode pdf file's name
     */
    public static String getFileName(String fileName, String datePattern) {
        String currentDate = new SimpleDateFormat(datePattern).format(new Date());
        return getFileName(fileName + "_" + currentDate);
    }

    /**
     * <p>Set http response headers.</p>
     *
     * @param response Http servlet response
     * @param fileName Pdf file's name
     */
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        if (response == null) {
            throw new SysBusinessException(RSP_ERROR_MESSAGE);
        }
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_PREFIX + fileName + CONTENT_DISPOSITION_SUFFIX);
    }

    public static void renderPageNum(PdfDocument pdfDocument, Document document) {
        int numberOfPages = pdfDocument.getNumberOfPages();
        float pageWidth = document.getPdfDocument().getDefaultPageSize().getWidth();
        for (int i = 1; i <= numberOfPages; i++) {
            String text = String.format("%s / %s", i, numberOfPages);
            document.setFontSize(6f);
            document.showTextAligned(new Paragraph(text), pageWidth / 2, document.getBottomMargin() + 7, i, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
        }
    }
}
