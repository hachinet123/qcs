package com.tre.centralkitchen.domain.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 10225441
 */
@Data
public class WorkGroupListVo {
    /**
     * 作業グループID
     */
    @ApiModelProperty("作業グループID")
    private Integer id;
    /**
     * 作業グループ名
     */
    @ApiModelProperty("作業グループ名")
    private String name;
}
