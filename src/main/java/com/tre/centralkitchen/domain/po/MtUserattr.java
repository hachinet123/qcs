package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * ログインユーザーマスタ所属
 * </p>
 *
 * @author JDev
 * @since 2022-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.mt_userattr")
public class MtUserattr extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ログインコード(社員番号など)
     */
    private String code;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * ラインID
     */
    private Integer lineId;

    /**
     * フリー項目1
     */
    @TableField("freecolumn_1")
    private Integer freecolumn1;

    /**
     * フリー項目2
     */
    @TableField("freecolumn_2")
    private Integer freecolumn2;

    /**
     * フリー項目3
     */
    @TableField("freecolumn_3")
    private Integer freecolumn3;

    /**
     * フリー項目4
     */
    @TableField("freecolumn_4")
    private Integer freecolumn4;

    /**
     * フリー項目5
     */
    @TableField("freecolumn_5")
    private Integer freecolumn5;

}
