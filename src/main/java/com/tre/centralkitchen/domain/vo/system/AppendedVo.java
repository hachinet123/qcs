package com.tre.centralkitchen.domain.vo.system;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "出庫データ追加修正")
@ExcelIgnoreUnannotated
public class AppendedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "伝票番号")
    @ExcelProperty(value = "伝票番号", index = 0)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer slipCode;

    @ApiModelProperty(value = "行番号")
    @ExcelProperty(value = "行番号", index = 1)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer lineNo;

    @ApiModelProperty(value = "納品予定日")
    @ExcelProperty(value = "納品予定日", index = 2)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String dlvschedDate;

    @ApiModelProperty(value = "店舗")
    @ExcelProperty(value = "店舗", index = 3)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer storeId;

    @ApiModelProperty(value = "センター")
    @ExcelProperty(value = "センター", index = 4)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer centerId;

    @ApiModelProperty(value = "呼出品番")
    @ExcelProperty(value = "呼出品番", index = 5)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer callCode;

    @ApiModelProperty(value = "JAN")
    @ExcelProperty(value = "JAN", index = 6)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String itemId;

    @ApiModelProperty(value = "0/1/2")
    @ExcelProperty(value = "計量区分", index = 7)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer teikanTypeid;

    @ApiModelProperty(value = "便")
    @ExcelProperty(value = "便", index = 8)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer mailNo;

    @ApiModelProperty(value = "産地")
    @ExcelProperty(value = "産地", index = 9)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer placeId;

    @ApiModelProperty(value = "ロットNo")
    @ExcelProperty(value = "ロットNo", index = 10)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String lotNo;

    @ApiModelProperty(value = "数量")
    @ExcelProperty(value = "数量", index = 11)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private Integer quantity;

    @ApiModelProperty(value = "重量")
    @ExcelProperty(value = "重量", index = 12)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal weight;

    @ApiModelProperty(value = "PC原価")
    @ExcelProperty(value = "PC原価", index = 13)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal costRcp;

    @ApiModelProperty(value = "店原価")
    @ExcelProperty(value = "店原価", index = 14)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal cost;

    @ApiModelProperty(value = "店売価")
    @ExcelProperty(value = "店売価", index = 15)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private Integer price;

    @ContentFontStyle(color = Font.COLOR_RED)
    @ExcelProperty(value = "エラーメッセージ", index = 16)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String errorMsg;
}
