package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LinesVo {

    /**
     * ラインコード
     */
    @ApiModelProperty("linecd")
    private Integer linecd;

    /**
     * ライン名
     */
    @ApiModelProperty("linename")
    private String lineName;

}
