package com.tre.centralkitchen.domain.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CenterLineVo {
    /**
     * ライン名
     */
    @ApiModelProperty("ライン名")
    private String lineName;
    /**
     * ラインID
     */
    @ApiModelProperty("ラインID")
    private Integer lineId;
    /**
     * センター名
     */
    @ApiModelProperty("センター名")
    private String centerName;
    /**
     * センターID
     */
    @ApiModelProperty("センターID")
    private Integer centerId;
}
