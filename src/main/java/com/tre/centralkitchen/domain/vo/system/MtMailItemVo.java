package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.Font;

/**
 * 便設定個別マスタ
 */
@Data
@ApiModel("MtMailItemVo")
@EqualsAndHashCode(callSuper = false)
@ExcelIgnoreUnannotated
public class MtMailItemVo extends BaseEntityVo {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = {"↓必須", "センターコード"}, index = 0)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String centerId;
    /**
     * JAN
     */
    @Alias("JAN")
    @ExcelProperty(value = {"↓必須", "JAN"}, index = 1)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @ApiModelProperty("JAN")
    private String itemId;
    /**
     * 品番
     */
    @Alias("品番")
    @ApiModelProperty("品番")
    private String callCode;
    /**
     * 商品名
     */
    @Alias("商品名")
    @ApiModelProperty("商品名")
    @ExcelProperty(value = {"", "商品名"}, index = 2)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String itemName;
    /**
     * 店舗ID
     */
    @Alias("店舗コード")
    @ApiModelProperty("店舗コード")
    @ExcelProperty(value = {"↓必須", "店舗コード"}, index = 3)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String storeId;
    /**
     * 店舗名
     */
    @Alias("店舗名")
    @ApiModelProperty("店舗名")
    @ExcelProperty(value = {"", "店舗名"}, index = 4)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String storeName;
    /**
     * 便
     */
    @Alias("便")
    @ApiModelProperty("便")
    private String mail;
    /**
     * 便id
     */
    @ExcelProperty(value = {"↓必須", "便番号"}, index = 5)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @ApiModelProperty("便id")
    private String mailNo;
    /**
     * 備考
     */
    @Alias("備考")
    @ApiModelProperty("備考")
    @ExcelProperty(value = {"", "備考"}, index = 6)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String memo;
    @ContentFontStyle(color = Font.COLOR_RED)
    @ExcelProperty(value = {"", "エラーメッセージ"}, index = 7)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String errorMsg;
    private String beforeMailNo;
    private String id;
    private String ids;
    private String name;


}
