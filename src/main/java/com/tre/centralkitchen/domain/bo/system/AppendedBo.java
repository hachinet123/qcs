package com.tre.centralkitchen.domain.bo.system;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ExcelIgnoreUnannotated
public class AppendedBo extends BaseEntityBo {

    @ApiModelProperty(value = "伝票番号")
    @NotNull(message = SysConstantInfo.SLIP_CODE_NOT_EMPTY)
    @ExcelProperty(value = "伝票番号", index = 0)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String slipCode;

    @ApiModelProperty(value = "行番号")
    @NotNull(message = SysConstantInfo.LINENO_NOT_EMPTY)
    @ExcelProperty(value = "行番号", index = 1)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String lineNo;

    @ApiModelProperty(value = "納品予定日")
    @NotBlank(message = SysConstantInfo.DLVSCHEDDATE_NOT_EMPTY)
    @ExcelProperty(value = "納品予定日", index = 2)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String dlvschedDate;

    @ApiModelProperty(value = "店舗コード")
    @NotNull(message = SysConstantInfo.STORE_INPUT_NOT_EMPTY)
    @ExcelProperty(value = "店舗", index = 3)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String storeId;

    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    @ExcelProperty(value = "センター", index = 4)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String centerId;

    @ExcelProperty(value = "呼出品番", index = 5)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String callCode;

    @ApiModelProperty(value = "JAN")
    @NotBlank(message = SysConstantInfo.JAN_NOT_EMPTY)
    @ExcelProperty(value = "JAN", index = 6)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String itemId;

    @ApiModelProperty(value = "計量区分ID-0:不定貫/1:定貫/2:定貫(計量あり)")
//    @ExcelProperty(value = "計量区分", index = 7)
//    @ContentStyle(locked = BooleanEnum.FALSE)
    private String teikanTypeid;

    @ApiModelProperty(value = "便")
    @NotNull(message = SysConstantInfo.MAIL_NO_NOT_EMPTY)
    @ExcelProperty(value = "便", index = 7)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String mailNo;

    @ApiModelProperty(value = "産地コード")
    @ExcelProperty(value = "産地", index = 13)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String placeId;

    @ApiModelProperty(value = "ロットNo")
    @ExcelProperty(value = "ロットNo", index = 14)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String lotNo;

    @ApiModelProperty(value = "数量")
    @ExcelProperty(value = "数量", index = 8)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private String quantity;

    @ApiModelProperty(value = "重量")
    @ExcelProperty(value = "重量", index = 9)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private String weight;

    @ApiModelProperty(value = "PC原価")
    @ExcelProperty(value = "PC原価", index = 10)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private String costRcp;

    @ApiModelProperty(value = "店原価")
    @ExcelProperty(value = "店原価", index = 11)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private String cost;

    @ApiModelProperty(value = "店売価")
    @ExcelProperty(value = "店売価", index = 12)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private String price;

    @ApiModelProperty(value = "部門CD")
    private Integer departmentId;

    @ApiModelProperty(value = "地域ID")
    private Integer areaId;

    @ContentFontStyle(color = Font.COLOR_RED)
    @ExcelProperty(value = "エラーメッセージ", index = 15)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String errorMsg;

    @ApiModelProperty(value = "ライン")
    private Integer lineId;
}
