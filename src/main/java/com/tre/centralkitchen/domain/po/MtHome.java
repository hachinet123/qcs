package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "centralkitchen.mt_home")
public class MtHome extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 管理id
     */
    @ApiModelProperty(value = "管理id")
    @TableField(value = "id")
    private Integer id;

    /**
     * タイトル
     */
    @ApiModelProperty(value = "タイトル")
    @TableField(value = "title")
    private String title;

    /**
     * 表示内容
     */
    @ApiModelProperty(value = "表示内容")
    @TableField(value = "contents")
    private String contents;

    @ApiModelProperty(value = "ソート")
    @TableField(value = "seq")
    private String seq;
}
