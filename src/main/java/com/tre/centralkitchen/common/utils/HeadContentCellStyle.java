package com.tre.centralkitchen.common.utils;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;

import java.util.ArrayList;
import java.util.List;

public class HeadContentCellStyle {
    public static HorizontalCellStyleStrategy myHorizontalCellStyleStrategy() {

        WriteCellStyle headWriteCellStyle = new WriteCellStyle();

        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setBold(true);
        headWriteFont.setFontName("メイリオ");
        headWriteFont.setFontHeightInPoints((short) 11);
        headWriteCellStyle.setWriteFont(headWriteFont);
        setBorderStyle(headWriteCellStyle);
        List<WriteCellStyle> listCntWritCellSty = new ArrayList<>();

        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("メイリオ");
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE1.getIndex());

        contentWriteCellStyle.setWrapped(false);

        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        setBorderStyle(contentWriteCellStyle);
        listCntWritCellSty.add(contentWriteCellStyle);

        return new HorizontalCellStyleStrategy(headWriteCellStyle, listCntWritCellSty);
    }

    private static void setBorderStyle(WriteCellStyle contentWriteCellStyle) {
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentWriteCellStyle.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentWriteCellStyle.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        contentWriteCellStyle.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    }

}


