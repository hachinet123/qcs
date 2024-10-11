package com.tre.centralkitchen.domain.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LineListVo {
    /**
     * ラインID
     */
    @ApiModelProperty("ラインID")
    private Integer lineId;
    /**
     * ライン名
     */
    @ApiModelProperty("ライン名")
    private String lineName;
}
