package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MtUserroleVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ログインコード(社員番号など)
     */
    @ApiModelProperty("ログインコード(社員番号など)")
    private String code;

    /**
     * 他センタ権限
     */
    @ApiModelProperty("他センタ権限")
    private Integer otherCenterAuthority;

    /**
     * 他ライン権限
     */
    @ApiModelProperty("他ライン権限")
    private Integer otherLineAuthority;

    /**
     * 在庫メンテン
     */
    @ApiModelProperty("在庫メンテン")
    private Integer warehouseAuthority;

    /**
     * 商談承認（原材料）
     */
    @ApiModelProperty("商談承認（原材料）")
    private Integer metamaterialAuthority;

    /**
     * 商談承認（中間品）
     */
    @ApiModelProperty("商談承認（中間品）")
    private Integer middleAuthority;

    /**
     * 商談承認（商品）
     */
    @ApiModelProperty("商談承認（商品）")
    private Integer productAuthority;

    /**
     * マスタ管理
     */
    @ApiModelProperty("マスタ管理")
    private Integer masterAuthority;

    /**
     * ユーザー管理
     */
    @ApiModelProperty("ユーザー管理")
    private Integer userAuthority;

    /**
     * 検食除外マスタメンテ
     */
    @ApiModelProperty("検食除外マスタメンテ")
    private Integer exceptAuthority;

    /**
     * システム管理者権限
     */
    @ApiModelProperty("システム管理者権限")
    private Integer systemAuthority;

    /**
     * レシピ権限
     */
    @ApiModelProperty("レシピ権限")
    private Integer recipeAuthority;

    /**
     * フリー項目1
     */
    @ApiModelProperty("フリー項目1")
    private Integer freecolumn1;

    /**
     * フリー項目2
     */
    @ApiModelProperty("フリー項目2")
    private Integer freecolumn2;

    /**
     * フリー項目3
     */
    @ApiModelProperty("フリー項目3")
    private Integer freecolumn3;

    /**
     * フリー項目4
     */
    @ApiModelProperty("フリー項目4")
    private Integer freecolumn4;

    /**
     * フリー項目5
     */
    @ApiModelProperty("フリー項目5")
    private Integer freecolumn5;
}
