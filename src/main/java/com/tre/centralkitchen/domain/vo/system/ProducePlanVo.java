package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * ProducePlanVo
 *
 * @date 2022-11-25
 */
@Data
@ApiModel("ProducePlanVo")
public class ProducePlanVo {

    @ApiModelProperty("センターコード")
    private Integer supplierCd;

    @ApiModelProperty("店コード")
    private String BranchCD;

    @ApiModelProperty("便")
    private String mailNo;

    @ApiModelProperty("JAN")
    private String jan;

    @ApiModelProperty("受注数")
    private Integer orderQty;

    @ApiModelProperty("納品予定日")
    private String deliveryDate;

    private Integer userCd;
}
