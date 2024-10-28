package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qcs.mt_setting")
public class MtSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ユーザーID
     */
    @TableId
    @TableField("user_id")
    private Integer userId;

    /**
     * センター
     */
    @TableField("center_id")
    private Integer centerId;

    /**
     * セクションID
     */
    @TableField("section_id")
    private Integer sectionId;

    /**
     * 言語ID
     */
    @TableField("language_id")
    private Integer languageId;

    @TableField("freecolumn_1")
    private Integer freeColumn1;

    @TableField("freecolumn_2")
    private Integer freeColumn2;

    @TableField("freecolumn_3")
    private Integer freeColumn3;

    @TableField("freecolumn_4")
    private Integer freeColumn4;

    @TableField("freecolumn_5")
    private Integer freeColumn5;

    @TableField("freecolumn_6")
    private Integer freeColumn6;

    @TableField("freecolumn_7")
    private Integer freeColumn7;

    @TableField("freecolumn_8")
    private Integer freeColumn8;

    @TableField("freecolumn_9")
    private Integer freeColumn9;

}
