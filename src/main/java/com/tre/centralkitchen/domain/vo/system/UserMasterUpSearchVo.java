package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserMasterUpSearchVo extends BaseEntityVo {

    /**
     * 社員番号
     */
    @ApiModelProperty(value = "社員番号")
    private String userId;

    /**
     * 社員名
     */
    @ApiModelProperty(value = "社員名")
    private String userName;

    /**
     * 備考
     */
    @ApiModelProperty(value = "備考")
    private String memo;
    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID")
    private String centerId;

    /**
     * ラインID
     */
    @ApiModelProperty(value = "ラインID")
    private String lineId;

    /**
     * 他センタ権限
     */
    @ApiModelProperty(value = "他センタ権限")
    private String otherCenterAuthority;

    /**
     * 他ライン権限
     */
    @ApiModelProperty(value = "他ライン権限")
    private String otherLineAuthority;

    /**
     * 在庫メンテン
     */
    @ApiModelProperty(value = "在庫メンテン")
    private String warehouseAuthority;

    /**
     * 商談承認（原材料）
     */
    @ApiModelProperty(value = "商談承認（原材料）")
    private String metaMaterialAuthority;

    /**
     * 商談承認（中間品）
     */
    @ApiModelProperty(value = "商談承認（中間品）")
    private String middleAuthority;

    /**
     * 商談承認（商品）
     */
    @ApiModelProperty(value = "商談承認（商品）")
    private String productAuthority;

    /**
     * マスタ管理
     */
    @ApiModelProperty(value = "マスタ管理")
    private String masterAuthority;

    /**
     * ユーザー管理
     */
    @ApiModelProperty(value = "ユーザー管理")
    private String userAuthority;

    /**
     * 検食除外マスタメンテナンス権限
     */
    @ApiModelProperty(value = "検食除外マスタメンテナンス権限")
    private String exceptAuthority;

    /**
     * システム管理
     */
    @ApiModelProperty(value = "システム管理")
    private String systemAuthority;

    /**
     * レシピ権限
     */
    @ApiModelProperty(value = "レシピ権限")
    private String recipeAuthority;

}
