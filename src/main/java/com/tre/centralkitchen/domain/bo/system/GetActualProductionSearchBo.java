package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("GetActualProductionSearchBo")
public class GetActualProductionSearchBo extends BaseEntityBo {
    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    private Integer centerId;

    /**
     * 便番号
     */
    @ApiModelProperty(value = "便番号", required = true)
    private String mailNo;

}
