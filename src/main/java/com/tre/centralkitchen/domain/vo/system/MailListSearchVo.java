package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MailListSearchVo {
    /**
     * センター名
     */
    @Alias("センター")
    @ApiModelProperty("センター名")
    private String centerName;

    /**
     * 便
     */
    @Alias("便")
    @ApiModelProperty("便")
    private String mail;

    /**
     * リードタイム
     */
    @Alias("リードタイム")
    @ApiModelProperty("リードタイム")
    private String leadTime;

    /**
     * 店舗数
     */
    @Alias("店舗数")
    @ApiModelProperty("店舗数")
    private String storeNum;

    /**
     * 説明
     */
    @Alias("説明")
    @ApiModelProperty("説明")
    private String description;

    /**
     * 備考
     */
    @Alias("備考")
    @ApiModelProperty("備考")
    private String memo;

    /**
     * 制約
     */
    @Alias("制約")
    @ApiModelProperty("制約")
    private String selGroup;
    /**
     * 便id
     */
    @ApiModelProperty("便id")
    private Integer mailNo;

}
