package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("ProducePlanBo")
public class ProducePlanBo extends BaseEntityBo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "センターコード", required = true)
    @NotNull(message = "センターコードは必要フィールド！")
    private Integer centerId;

    @ApiModelProperty("JAN")
    private String jan;

    @ApiModelProperty("呼出品番")
    private Integer callCd;

    @ApiModelProperty("商品名")
    private String productName;

    @ApiModelProperty("ラインCD")
    private String lineIdsCondition;

    private Integer[] lineIds;

}
