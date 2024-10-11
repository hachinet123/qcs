package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

@Data
@ApiModel(value = "ユーザー")
@EqualsAndHashCode(callSuper = false)
public class UserMasterBo extends BaseEntityBo {

    /**
     * 社員番号
     */
    @ApiModelProperty(value = "社員番号", required = true)
    @Size(max = 10, message = SysConstantInfo.USER_ID_MAX)
    private String userId;

    /**
     * 社員名
     */
    @ApiModelProperty(value = "社員名", required = true)
    @Size(max = 30, message = SysConstantInfo.USER_NAME_MAX)
    private String userName;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    private Integer centerId;

    /**
     * ラインID
     */
    @ApiModelProperty(value = "ラインID")
    private Integer lineId;

    /**
     * 備考
     */
    @ApiModelProperty(value = "備考")
    @Size(max = 50, message = SysConstantInfo.MEMO_MAX)
    private String memo;

    /**
     * 他センタ権限
     */
    @ApiModelProperty(value = "他センタ権限")
    private Integer otherCenterAuthority;
    /**
     * 他ライン権限
     */
    @ApiModelProperty(value = "他ライン権限")
    private Integer otherLineAuthority;

    /**
     * 在庫メンテン
     */
    @ApiModelProperty(value = "在庫メンテン")
    private Integer warehouseAuthority;

    /**
     * 商談承認（原材料）
     */
    @ApiModelProperty(value = "商談承認（原材料）")
    private Integer metaMaterialAuthority;

    /**
     * 商談承認（中間品）
     */
    @ApiModelProperty(value = "商談承認（中間品）")
    private Integer middleAuthority;

    /**
     * 商談承認（商品）
     */
    @ApiModelProperty(value = "商談承認（商品）")
    private Integer productAuthority;

    /**
     * マスタ管理
     */
    @ApiModelProperty(value = "マスタ管理")
    private Integer masterAuthority;

    /**
     * ユーザー管理
     */
    @ApiModelProperty(value = "ユーザー管理")
    private Integer userAuthority;

    /**
     * 検食除外マスタメンテナンス権限
     */
    @ApiModelProperty(value = "検食除外マスタメンテナンス権限")
    private Integer exceptAuthority;
    /**
     * システム管理
     */
    @ApiModelProperty(value = "システム管理")
    private Integer systemAuthority;
    /**
     * レシピ権限
     */
    @ApiModelProperty(value = "レシピ権限")
    private Integer recipeAuthority;

}
