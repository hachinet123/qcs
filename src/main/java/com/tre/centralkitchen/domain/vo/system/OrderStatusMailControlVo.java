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
public class OrderStatusMailControlVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * センターID
     */
    @ApiModelProperty("センターID")
    private Integer centerId;

    /**
     * 便番号
     */
    @ApiModelProperty("便番号")
    private Integer mailNo;

    /**
     * リーダタイム
     */
    @ApiModelProperty("リーダタイム")
    private Integer leadtime;


    /**
     * 表示順
     */
    @ApiModelProperty("表示順")
    private Integer seq;
}
