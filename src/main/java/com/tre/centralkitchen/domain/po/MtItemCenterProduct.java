package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品(加工品)センター別属性マスタ
 *
 * @author JDev
 * @since 2023-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "centralkitchen.mt_itemcenter_product")
public class MtItemCenterProduct extends BaseEntity {

    /**
     * プロセスセンターID
     */
    @TableField(value = "center_id")
    private Integer centerId;

    /**
     * 商品JAN
     */
    @TableField(value = "item_id")
    private String itemId;

    /**
     * 加工賃
     */
    @TableField(value = "pc_fee")
    private BigDecimal pcFee;

    /**
     * 作業グループID
     */
    @TableField(value = "wkgrp_id")
    private Integer wkgrpId;

    /**
     * コンテナ入数
     */
    @TableField(value = "container_unit")
    private Integer containerUnit;

    /**
     * 検食数
     */
    @TableField(value = "taste_qy")
    private Integer tasteQy;

    /**
     * 製造時生産性
     */
    @TableField(value = "productivity")
    private Integer productivity;

    /**
     * 製造時ラインスピード
     */
    @TableField(value = "line_speed")
    private Integer lineSpeed;

    /**
     * 予備項目1
     */
    @TableField(value = "freecolumn1")
    private String freecolumn1;

    /**
     * 予備項目2
     */
    @TableField(value = "freecolumn2")
    private String freecolumn2;

    /**
     * 予備項目3
     */
    @TableField(value = "freecolumn3")
    private String freecolumn3;

    /**
     * 予備項目4
     */
    @TableField(value = "freecolumn4")
    private String freecolumn4;

    /**
     * 予備項目5
     */
    @TableField(value = "freecolumn5")
    private String freecolumn5;

    /**
     * 予備項目6
     */
    @TableField(value = "freecolumn6")
    private String freecolumn6;

    /**
     * 予備項目7
     */
    @TableField(value = "freecolumn7")
    private String freecolumn7;

    /**
     * 予備項目8
     */
    @TableField(value = "freecolumn8")
    private String freecolumn8;

    /**
     * 予備項目9
     */
    @TableField(value = "freecolumn9")
    private String freecolumn9;

    /**
     * 予備項目10
     */
    @TableField(value = "freecolumn10")
    private String freecolumn10;

    /**
     * 備考
     */
    @TableField(value = "memo")
    private String memo;
}
