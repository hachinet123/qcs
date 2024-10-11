package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UploadBo extends BaseEntityBo {

    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = "センターを空にすることはできません")
    private Integer centerId;
}
