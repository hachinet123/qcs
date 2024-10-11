package com.tre.centralkitchen.domain.bo.common;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CommonMailNoBo extends BaseEntityBo {
    /**
     * センターID
     */
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    /**
     * ライン
     */
    @ApiModelProperty(value = "ラインID", required = true)
    private Integer lineId;
}
