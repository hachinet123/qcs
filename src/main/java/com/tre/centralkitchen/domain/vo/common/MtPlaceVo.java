package com.tre.centralkitchen.domain.vo.common;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 10225441
 */
@Data
public class MtPlaceVo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 産地ID
     */
    @ApiModelProperty("産地ID")
    private Integer id;

    /**
     * 産地名称
     */
    @ApiModelProperty("産地名称")
    private String name;

    /**
     * 産地名称カナ
     */
    @ApiModelProperty("産地名称カナ")
    private String nameK;

    /**
     * 産地略称
     */
    @ApiModelProperty("産地略称")
    private String nameS;

    /**
     * 表示順
     */
    @ApiModelProperty("表示順")
    private Integer seq;

    /**
     * 削除フラグ
     */
    @ApiModelProperty("削除フラグ")
    private Integer fDel;
}