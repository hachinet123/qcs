package com.tre.centralkitchen.common.annotation;

import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.lang.annotation.*;

/**
 * @author 10225441
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PdfTableProp {
    /**
     * String of table's column header.
     *
     * @return string value
     */
    String title();

    /**
     * Float of percent cell width.
     *
     * @return float value
     */
    float width();

    /**
     * Float of font's size ;
     * <p>The font size value is similar to actual font size.</p>
     *
     * @return float value
     */
    float fontSize() default 11f;

    /**
     * Boolean of the font is bold ;
     * <p>The font size value is similar to actual font size.</p>
     *
     * @return float value
     */
    boolean isBold() default false;

    /**
     * Alignment status of text.
     *
     * @return TextAlignment value
     */
    TextAlignment textAlign() default TextAlignment.CENTER;

    /**
     * Vertical alignment status of element.
     *
     * @return VerticalAlignment value
     */
    VerticalAlignment vAlign() default VerticalAlignment.MIDDLE;

    /**
     * Horizontal alignment status of element.
     *
     * @return HorizontalAlignment value
     */
    HorizontalAlignment hAlign() default HorizontalAlignment.CENTER;
}
