package com.tre.centralkitchen.common.domain;

import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.Data;

/**
 * @author 10225441
 */
@Data
public class PdfTablePropInfo {
    private String[] titleList;
    private String[] methodList;
    private TextAlignment[] tList;
    private VerticalAlignment[] vList;
    private HorizontalAlignment[] hList;
    private float[] fontSizeList;
    private boolean[] isBoldList;
    private int column;
    private UnitValue[] columnsWidth;
}
