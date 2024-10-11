package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * @date 2022-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderStatusWorkGroupVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 作業グループID
     */
    @ApiModelProperty("作業グループID")
    private Integer id;

    /**
     * センターID
     */
    @ApiModelProperty("センターID")
    private Integer centerId;

    @ApiModelProperty("ライン")
    private Integer lineId;

    /**
     * 作業グループ名称
     */
    @ApiModelProperty("作業グループ名称")
    private String name;

    /**
     * 表示順
     */
    @ApiModelProperty("表示順")
    private Integer seq;
}
