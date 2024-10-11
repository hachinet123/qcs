package com.tre.centralkitchen.domain.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CenterListVo {
    /**
     * センターID
     */
    @ApiModelProperty("センターID")
    private Integer centerId;
    /**
     * センター名
     */
    @ApiModelProperty("センター名")
    private String centerName;
    private Integer vendorId;
}
