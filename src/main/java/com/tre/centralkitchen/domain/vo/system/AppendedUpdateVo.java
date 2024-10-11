package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "出庫データ追加修正")
@ExcelIgnoreUnannotated
public class AppendedUpdateVo implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "ID")
    private String id;

    @Alias("伝票番号")
    @ApiModelProperty(value = "伝票番号")
    @ExcelProperty(value = "伝票番号", index = 0)
    private Integer slipCode;

    @Alias("行No")
    @ApiModelProperty(value = "行番号")
    @ExcelProperty(value = "行番号", index = 1)
    private Integer lineNo;

    @Alias("納品予定日")
    @ApiModelProperty(value = "納品予定日")
    @ExcelProperty(value = "納品予定日", index = 2)
    private String dlvschedDateFmt;

    @Alias("店舗コード")
    @ApiModelProperty(value = "店舗コード")
    @ExcelProperty(value = "店舗コード", index = 3)
    private Integer storeId;

    @ApiModelProperty(value = "センター")
    @ExcelProperty(value = "センター", index = 5)
    private Integer centerId;

    @ApiModelProperty(value = "センター名")
    @ExcelProperty(value = "センター名", index = 6)
    private String centerName;

    @Alias("品番")
    @ApiModelProperty(value = "呼出品番")
    @ExcelProperty(value = "呼出品番", index = 7)
    private Integer callCode;

    @Alias("JAN")
    @ApiModelProperty(value = "JAN")
    @ExcelProperty(value = "JAN", index = 8)
    private String itemId;

    @Alias("商品名")
    @ApiModelProperty(value = "商品名")
    @ExcelProperty(value = "商品名", index = 9)
    private String itemName;

    @Alias("便")
    @ApiModelProperty(value = "便名")
    private String mail;

    @ApiModelProperty(value = "ライン")
    private Integer lineId;

    @Alias("ライン")
    @ApiModelProperty(value = "ライン名")
    private String lineName;

    @Alias("店舗名")
    @ApiModelProperty(value = "店舗名")
    @ExcelProperty(value = "店舗名", index = 4)
    private String storeName;

    @ApiModelProperty(value = "0/1/2")
    @ExcelProperty(value = "計量区分", index = 10)
    private Integer teikanTypeid;

    @ApiModelProperty(value = "計量区分ID-0:不定貫/1:定貫/2:定貫(計量あり)")
    @ExcelProperty(value = "計量区分名", index = 11)
    private String teikanTypeName;

    @ApiModelProperty(value = "便")
    @ExcelProperty(value = "便", index = 12)
    private Integer mailNo;

    @Alias("数量")
    @ApiModelProperty(value = "数量")
    @ExcelProperty(value = "数量", index = 13)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private Integer quantity;

    @Alias("重量")
    @ApiModelProperty(value = "重量")
    @ExcelProperty(value = "重量", index = 14)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal weight;

    @Alias("PC原価")
    @ApiModelProperty(value = "PC原価")
    @ExcelProperty(value = "PC原価", index = 15)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal costRcp;

    @Alias("店原価")
    @ApiModelProperty(value = "店原価")
    @ExcelProperty(value = "店原価", index = 16)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal cost;

    @Alias("店売価")
    @ApiModelProperty(value = "店売価")
    @ExcelProperty(value = "店売価", index = 17)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private Integer price;

    @ApiModelProperty(value = "産地")
    @ExcelProperty(value = "産地", index = 18)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private Integer placeId;

    @ApiModelProperty(value = "納品予定日")
    @JsonFormat(pattern = "yyyy/MM/dd", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date dlvschedDate;

    @Alias("PC原価金額")
    @ApiModelProperty(value = "PC原価金額")
    private BigDecimal costRcpAm;

    @Alias("店原価金額")
    @ApiModelProperty(value = "店原価金額")
    private BigDecimal costAm;

    @Alias("店売価金額")
    @ApiModelProperty(value = "店売価金額")
    private BigDecimal priceAm;

    @Alias("産地名")
    @ApiModelProperty(value = "産地名")
    @ExcelProperty(value = "産地名", index = 19)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String placeName;

    @Alias("ロットNo")
    @ApiModelProperty(value = "ロットNo")
    @ExcelProperty(value = "ロットNo", index = 20)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String lotNo;

    @ContentFontStyle(color = Font.COLOR_RED)
    @ExcelProperty(value = "エラーメッセージ", index = 21)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String errorMsg;
}
