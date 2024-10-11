package com.tre.centralkitchen.domain.vo.system;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.tre.centralkitchen.common.constant.business.FixAmountResultModifyConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.usermodel.Font;

import java.math.BigDecimal;
import java.util.List;

@ExcelIgnoreUnannotated
@Data
public class FixAmountResultModifyVo {
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_CENTER_ID)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_CENTER_ID
            , index = 0)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String centerId;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_CALL_CODE)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_CALL_CODE
            , index = 1)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String callCode;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_ITEM_ID)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_ITEM_ID
            , index = 2)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String itemId;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_MAIL_NO)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_MAIL_NO
            , index = 3)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String mailNo;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_STORE_ID)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_STORE_ID
            , index = 4)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String storeId;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_PLACE_ID)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_PLACE_ID
            , index = 5)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String placeId;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_BATCH_GROUP_NO)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_BATCH_GROUP_NO
            , index = 6)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String lotNo;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_QTY)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_QTY
            , index = 7)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private Integer qty;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_WEIGHT)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_WEIGHT
            , index = 8)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal weight;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_PC_PRICE)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_PC_PRICE
            , index = 9)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal pcPrice;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_ITEM_PRICE)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_ITEM_PRICE
            , index = 10)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0.00")
    private BigDecimal itemPrice;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_PRICE)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_PRICE
            , index = 11)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @NumberFormat("#,##0")
    private Integer price;
    @ApiModelProperty(FixAmountResultModifyConstants.EXCEL_COL_ERROR_MSG)
    @ExcelProperty(value = FixAmountResultModifyConstants.EXCEL_COL_ERROR_MSG
            , index = 12)
    @ContentFontStyle(color = Font.COLOR_RED)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String errorMsg;
    private List<Integer> errorPrice;

    @ApiModelProperty(value = "地域ID")
    private Integer areaId;
}
