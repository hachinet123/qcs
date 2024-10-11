package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.itextpdf.layout.property.TextAlignment;
import com.tre.centralkitchen.common.annotation.PdfTableProp;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Font;

import java.time.LocalDate;

/**
 * <p>
 * 在庫_日次棚卸
 * </p>
 *
 * @author 10253955
 * @since 2023-12-22 9:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ExcelIgnoreUnannotated
public class DailyInventoryVo {

    private String id;

    private Integer centerId;

    /**
     * センター
     */
    @Alias("センター")
    private String centerName;

    /**
     * 倉庫ID
     */
    @ExcelProperty(value = "倉庫ID", index = 0)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private Integer warehouseId;

    /**
     * 倉庫名
     */
    @Alias("倉庫名")
    @ApiModelProperty("倉庫名")
    @ExcelProperty(value = "倉庫", index = 1)
    private String warehouseName;

    /**
     * JAN(原材料・副資材)
     */
    @Alias("JAN(原材料・副資材)")
    @PdfTableProp(title = "JAN", width = 6.0f, fontSize = 8f, textAlign = TextAlignment.LEFT)
    @ExcelProperty(value = "JAN", index = 2)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private String itemId;

    /**
     * 原材料名
     */
    @Alias("原材料名")
    @PdfTableProp(title = "原材料名", width = 8.0f, fontSize = 8f, textAlign = TextAlignment.LEFT)
    @ExcelProperty(value = "原材料名", index = 3)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private String itemName;

    /**
     * 規格分量
     */
    @TableField("n_szname")
    private Integer nSzName;

    /**
     * ユニット名
     */
    private String unitName;

    /**
     * 規格
     */
    @Alias("規格")
    @PdfTableProp(title = "規格", width = 4.0f, fontSize = 8f, textAlign = TextAlignment.LEFT)
    @ExcelProperty(value = "規格", index = 4)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private String specName;

    /**
     * 入数
     */
    @Alias("入数")
    @PdfTableProp(title = "入数", width = 3.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    @ExcelProperty(value = "入数", index = 5)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private Integer innerCaseQty;

    /**
     * ベンダーCD
     */
    @Alias("ベンダ")
    @PdfTableProp(title = "ベンダ", width = 4.0f, fontSize = 8f, textAlign = TextAlignment.LEFT)
    @ExcelProperty(value = "ベンダ", index = 6)
    @ContentStyle(locked = BooleanEnum.TRUE, horizontalAlignment = HorizontalAlignmentEnum.RIGHT)
    private String vendorId;

    /**
     * ベンダー名
     */
    @Alias("ベンダー名")
    @PdfTableProp(title = "ベンダー名", width = 6.0f, fontSize = 8f, textAlign = TextAlignment.LEFT)
    @ExcelProperty(value = "ベンダー名", index = 7)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private String supplierName;

    /**
     * 論理在庫
     */
    @Alias("論理在庫")
    @ApiModelProperty("論理在庫")
    private String qy;

    /**
     * 論理在庫数の整数部分:ケース
     */
    @Alias("在庫カウント数")
    @ApiModelProperty("論理在庫数の整数部分:ケース")
    @PdfTableProp(title = "ケース", width = 4.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    @ExcelProperty(value = "ケース", index = 9)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private Integer caseQy;

    /**
     * 論理在庫数の少数部分：袋or本
     */
    @Alias("在庫カウント数(袋or本)")
    @ApiModelProperty("論理在庫数の少数部分：袋or本")
    @PdfTableProp(title = "袋or本", width = 4.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    @ExcelProperty(value = "袋or本", index = 10)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private Integer shortQy;

    /**
     * 論理在庫数の整数部分:ケース
     */
    @ApiModelProperty("論理在庫数の整数部分:ケース")
    @PdfTableProp(title = "ケース", width = 4.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    @ExcelProperty(value = "ケース", index = 11)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer caseQyTemp;

    /**
     * 論理在庫数の少数部分：袋or本
     */
    @PdfTableProp(title = "袋or本", width = 4.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    @ExcelProperty(value = "袋or本", index = 12)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer shortQyTemp;

    /**
     * カウント原価(ケース)
     */
    @Alias("カウント原価(ケース)")
    @ApiModelProperty("カウント原価(ケース)")
    @ExcelProperty(value = "基準店原単価", index = 8)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private String caseCost;

    /**
     * カウント原価(袋or本)
     */
    @Alias("カウント原価(袋or本)")
    @ApiModelProperty("カウント原価(袋or本)")
    private String shortCost;

    /**
     * 在庫金額 case_qy * case_cost + short_qy * short_cost
     */
    @Alias("在庫金額")
    @ApiModelProperty("在庫金額 case_qy * case_cost + short_qy * short_cost")
    private String am;

    /**
     * 賞味期限1
     */
    @ApiModelProperty("賞味期限1")
    private LocalDate expTimeDate01;

    /**
     * 賞味期限2
     */
    @ApiModelProperty("賞味期限2")
    private LocalDate expTimeDate02;

    /**
     * 賞味期限1
     */
    @Alias("賞味期限1")
    @ApiModelProperty("賞味期限1")
    @PdfTableProp(title = "賞味期限1", width = 5.5f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    @ExcelProperty(value = "消費期限1", index = 13)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String expTimeDate01Str;

    /**
     * グループCD
     */
    private Integer lineId;

    /**
     * 賞味期限2
     */
    @Alias("賞味期限2")
    @ApiModelProperty("賞味期限2")
    @PdfTableProp(title = "賞味期限2", width = 5.5f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    @ExcelProperty(value = "消費期限2", index = 14)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String expTimeDate02Str;

    @ContentFontStyle(color = Font.COLOR_RED)
    @ExcelProperty(value = "エラーメッセージ", index = 15)
    @ContentStyle(locked = BooleanEnum.TRUE)
    private String errorMsg;
}
