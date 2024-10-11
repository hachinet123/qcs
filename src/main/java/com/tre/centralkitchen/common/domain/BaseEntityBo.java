package com.tre.centralkitchen.common.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Entity基本クラス
 *
 */

@Data
public class BaseEntityBo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 操作関数（作成）
     */
    @ApiModelProperty(value = "操作関数（作成）")
    @TableField(fill = FieldFill.INSERT)
    private Long insFuncId;

    /**
     * 操作機能（作成）
     */
    @ApiModelProperty(value = "操作機能（作成）")
    @TableField(fill = FieldFill.INSERT)
    private Long insOpeId;

    /**
     * 操作関数（更新）
     */
    @ApiModelProperty(value = "操作関数（更新）")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updFuncId;

    /**
     * 操作機能（更新）
     */
    @ApiModelProperty(value = "操作機能（更新）")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updOpeId;

}
