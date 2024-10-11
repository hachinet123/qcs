package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WareHouseItemBo extends BaseEntityBo {


    @ApiModelProperty(value = "センター" )
    private Integer centerId;

    @ApiModelProperty(value = "原材料JAN" )
    private String itemId;

    @ApiModelProperty(value = "保管倉庫" )
    private Integer warehouseId;

    @ApiModelProperty(value = "表示順" )
    private Integer seq;

    @ApiModelProperty(value = "備考" )
    private String memo;







}
