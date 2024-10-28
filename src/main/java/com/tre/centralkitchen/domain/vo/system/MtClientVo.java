package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MtClientVo {
    /**
     * 得意先ID
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 得意先名
     */
    @ApiModelProperty("name")
    private String name;
}
