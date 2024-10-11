package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel("ListMtMailItemVo")
@EqualsAndHashCode(callSuper = false)
public class ListMtMailItemVo {
    private static final long serialVersionUID = 1L;
    /**
     * プロセスセンターID
     */
    @Alias("センター")
    @ApiModelProperty("プロセスセンターID")
    private String centerName;
    /**
     * ライン
     */
    @Alias("ライン")
    @ApiModelProperty("ライン")
    private String lineName;
    /**
     * JAN
     */
    @Alias("JAN")
    @ApiModelProperty("JAN")
    private String itemId;
    /**
     * 品番
     */
    @Alias("品番")
    @ApiModelProperty("品番")
    private String callCode;
    /**
     * 商品名
     */
    @Alias("商品名")
    @ApiModelProperty("商品名")
    private String itemName;
    /**
     * 店舗コード
     */
    @Alias("店舗コード")
    @ApiModelProperty("店舗コード")
    private Integer storeId;
    /**
     * 店舗名
     */
    @Alias("店舗名")
    @ApiModelProperty("店舗名")
    private String storeName;
    /**
     * 便
     */
    @Alias("便")
    @ApiModelProperty("便")
    private String mail;
    /**
     * 設定区分
     */
    @Alias("設定区分")
    @ApiModelProperty("設定区分")
    private String csFlg;
    /**
     * 便id
     */
    @ApiModelProperty("便id")
    private Integer mailNo;
    private String basics;
    private String cases;
}
