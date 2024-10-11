package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * ログインユーザーマスタ権限
 * </p>
 *
 * @author JDev
 * @since 2022-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.mt_userrole")
public class MtUserrole extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ログインコード(社員番号など)
     */
    private String code;

    /**
     * 他センタ権限
     */
    private Integer otherCenterAuthority;

    /**
     * 他ライン権限
     */
    private Integer otherLineAuthority;

    /**
     * 在庫メンテン
     */
    private Integer warehouseAuthority;

    /**
     * 商談承認（原材料）
     */
    @TableField("metamaterial_authority")
    private Integer metaMaterialAuthority;

    /**
     * 商談承認（中間品）
     */
    private Integer middleAuthority;

    /**
     * 商談承認（商品）
     */
    private Integer productAuthority;

    /**
     * マスタ管理
     */
    private Integer masterAuthority;

    /**
     * ユーザー管理
     */
    private Integer userAuthority;

    /**
     * 検食除外マスタメンテ
     */
    private Integer exceptAuthority;

    /**
     * システム管理者権限
     */
    private Integer systemAuthority;

    /**
     * レシピ権限
     */
    private Integer recipeAuthority;

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
