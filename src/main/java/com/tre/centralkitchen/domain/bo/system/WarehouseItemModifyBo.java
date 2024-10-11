package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WarehouseItemModifyBo extends BaseEntityBo {

    @ApiModelProperty(value = "プロセスセンターID" )
    private Integer centerId;

    @ApiModelProperty(value = "倉庫ID")
    private Integer warehouseId;

    @ApiModelProperty(value = "原材料JAN")
    private String itemId;

    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @ApiModelProperty(value = "在庫日")
    private LocalDate stockDate;

    @ApiModelProperty(value = "在庫数")
    private BigDecimal tStorkQy;


}
