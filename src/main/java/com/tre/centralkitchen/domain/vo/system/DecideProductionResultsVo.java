package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2022-11-17
 */
@Data
@ApiModel("DecideProductionResultsVo")
public class DecideProductionResultsVo {

    /**
     * 伝票NO
     */
    @ApiModelProperty("伝票NO")
    private Integer slipNo;

    /**
     * 行NO
     */
    @ApiModelProperty("行NO")
    private Integer lineNo;

    /**
     * 納品予定日
     */
    @ApiModelProperty("納品予定日")
    private Date dlvschedDate;

    /**
     * 便
     */
    @ApiModelProperty("便")
    private Integer mailNo;

    /**
     * 店CD
     */
    @ApiModelProperty("店CD")
    private Integer storeId;

    /**
     * 商品CD
     */
    @ApiModelProperty("商品CD")
    private String itemId;

    /**
     * 産地名
     */
    @ApiModelProperty("産地名")
    private Integer placeId;

    /**
     * ロットNo
     */
    @ApiModelProperty("ロットNo")
    private String lotNo;

    /**
     * 機械番号
     */
    @ApiModelProperty("機械番号")
    private Integer machineNo;

    /**
     * 商品PC原単価
     */
    @ApiModelProperty("商品PC原単価")
    private Double costRcp;

    /**
     * 商品店原単価
     */
    @ApiModelProperty("商品店原単価")
    private Double costItem;

    /**
     * 商品店売価
     */
    @ApiModelProperty("商品店売価")
    private Integer price;

    /**
     * 生産指示数
     */
    @ApiModelProperty("生産指示数")
    private Integer instQy;

    /**
     * 生産実績数
     */
    @ApiModelProperty("生産実績数")
    private Integer actQy;

    /**
     * 生産実績重量
     */
    @ApiModelProperty("生産実績重量")
    private Double weight;

    /**
     * 部門CD
     */
    @ApiModelProperty("部門CD")
    private Integer departmentId;

}
