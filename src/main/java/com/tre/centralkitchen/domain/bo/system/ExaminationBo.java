package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 商品(加工品)センター別属性マスタ
 *
 * @author JDev
 * @since 2023-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExaminationBo extends BaseEntityBo {

    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    @ApiModelProperty(value = "JAN")
    private String itemId;
}
