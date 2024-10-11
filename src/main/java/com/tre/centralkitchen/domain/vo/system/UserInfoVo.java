package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 社員番号
     */
    @ApiModelProperty("社員番号")
    private String code;
    /**
     * 社員名称
     */
    @ApiModelProperty("社員名称")
    private String employeeName;
    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private String centerId;
    /**
     * ラインID
     */
    @ApiModelProperty("ラインID")
    private String lineId;
    /**
     * 他センタ権限
     */
    @ApiModelProperty("他センタ権限")
    private String otherCenterAuthority;
    /**
     * 他ライン権限
     */
    @ApiModelProperty("他ライン権限")
    private String otherLineAuthority;
    /**
     * 在庫メンテン
     */
    @ApiModelProperty("在庫メンテン")
    private String warehouseAuthority;
    /**
     * 商談承認（原材料）
     */
    @ApiModelProperty("商談承認（原材料）")
    private String metamaterialAuthority;
    /**
     * 商談承認（中間品）
     */
    @ApiModelProperty("商談承認（中間品）")
    private String middleAuthority;
    /**
     * 商談承認（商品）
     */
    @ApiModelProperty("商談承認（商品）")
    private String productAuthority;
    /**
     * マスタ管理
     */
    @ApiModelProperty("マスタ管理")
    private String masterAuthority;
    /**
     * ユーザー管理
     */
    @ApiModelProperty("ユーザー管理")
    private String userAuthority;
    /**
     * 検食除外マスタメンテ
     */
    @ApiModelProperty("検食除外マスタメンテ")
    private String exceptAuthority;
    /**
     * システム管理者権限
     */
    @ApiModelProperty("システム管理者権限")
    private String systemAuthority;
    /**
     * レシピ権限
     */
    @ApiModelProperty("レシピ権限")
    private String recipeAuthority;
}
