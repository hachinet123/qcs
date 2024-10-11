package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MailBasicSettingVo {

    /**
     * センター
     */
    @ApiModelProperty("センター")
    private String centerId;

    /**
     * センター名
     */
    @ApiModelProperty("センター名")
    private String centerName;

    /**
     * 便
     */
    @ApiModelProperty("便")
    private String mail;

    /**
     * 店舗CD
     */
    @ApiModelProperty("店舗CD")
    private String storeId;

    /**
     * 店舗名
     */
    @ApiModelProperty("店舗名")
    private String storeName;

    /**
     * 地域
     */
    @ApiModelProperty("地域")
    private String attr;

    /**
     * 説明
     */
    @ApiModelProperty("説明")
    private String discript;
    /**
     * 都道府県
     */
    @ApiModelProperty("都道府県")
    private String stateName;
    /**
     * 選択グループ
     */
    @ApiModelProperty("選択グループ")
    private String selGroup;

    /**
     * 便id
     */
    @ApiModelProperty("便id")
    private Integer mailNo;

}
