package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 生産実績トラン
 * </p>
 *
 * @author JDev
 * @since 2022-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TrProducedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 納品予定日
     */
    @ApiModelProperty("納品予定日")
    private LocalDate dlvschedDate;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * 便番号
     */
    @ApiModelProperty("便番号")
    private Integer mailNo;

    /**
     * JAN
     */
    @ApiModelProperty("JAN")
    private String itemId;

    /**
     * 店舗ID
     */
    @ApiModelProperty("店舗ID")
    private Integer storeId;

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
     * 生産時点レシピ原価(PC原価)
     */
    @ApiModelProperty("生産時点レシピ原価(PC原価)")
    private BigDecimal costRcp;

    /**
     * 生産時点商品原価(店原価)
     */
    @ApiModelProperty("生産時点商品原価(店原価)")
    private BigDecimal costItem;

    /**
     * 生産時点店舗販売売価
     */
    @ApiModelProperty("生産時点店舗販売売価")
    private Integer price;

    /**
     * 産地ID
     */
    @ApiModelProperty("産地ID")
    private Integer placeId;

    /**
     * ロット番号
     */
    @ApiModelProperty("ロット番号")
    private String lotNo;

    /**
     * 生産実績重量
     */
    @ApiModelProperty("生産実績重量")
    private BigDecimal weight;

    /**
     * 機械番号
     */
    @ApiModelProperty("機械番号")
    private Integer machineNo;

    /**
     * 削除フラグ
     */
    @ApiModelProperty("削除フラグ")
    private Integer fDel;

    /**
     * 登録日
     */
    @ApiModelProperty("登録日")
    private LocalDate insDate;

    /**
     * 登録時刻
     */
    @ApiModelProperty("登録時刻")
    private LocalDateTime insTime;

    /**
     * 登録者ID
     */
    @ApiModelProperty("登録者ID")
    private Integer insUserId;

    /**
     * 登録機能ID
     */
    @ApiModelProperty("登録機能ID")
    private Integer insFuncId;

    /**
     * 登録操作ID
     */
    @ApiModelProperty("登録操作ID")
    private Integer insOpeId;

    /**
     * 更新日
     */
    @ApiModelProperty("更新日")
    private LocalDate updDate;

    /**
     * 更新時刻
     */
    @ApiModelProperty("更新時刻")
    private LocalDateTime updTime;

    /**
     * 更新者ID
     */
    @ApiModelProperty("更新者ID")
    private Integer updUserId;

    /**
     * 更新機能ID
     */
    @ApiModelProperty("更新機能ID")
    private Integer updFuncId;

    /**
     * 更新操作ID
     */
    @ApiModelProperty("更新操作ID")
    private Integer updOpeId;


}
