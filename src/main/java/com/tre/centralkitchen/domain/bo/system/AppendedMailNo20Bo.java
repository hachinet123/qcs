package com.tre.centralkitchen.domain.bo.system;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AppendedMailNo20Bo {

    @ApiModelProperty(value = "伝票番号")
    private String slipCode;

    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private String centerId;

    @ApiModelProperty(value = "JAN")
    private String itemId;

    @ApiModelProperty(value = "呼出品番")
    private String callCode;

    @ApiModelProperty(value = "店舗コード", required = true)
    @NotBlank(message = SysConstantInfo.STORE_INPUT_NOT_EMPTY)
    private String storeIds;

    @ApiModelProperty(value = "納品予定日", required = true)
    @NotBlank(message = SysConstantInfo.DLVSCHEDDATE_NOT_EMPTY)
    private String dlvschedDate;

    @ApiModelProperty(value = "区分", required = true)
    @NotBlank(message = SysConstantInfo.TYPE_EMPTY)
    private String type;

    @ApiModelProperty(value = "数量")
    @NumberFormat("#,##0")
    @NotBlank(message = SysConstantInfo.QUANTITY_EMPTY)
    private String quantity;

    @ApiModelProperty(value = "重量")
    @NumberFormat("#,##0.00")
    private String weight;
}
