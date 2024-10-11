package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 出庫明細トラン
 * </p>
 *
 * @author JDev
 * @since 2022-12-15
 */
@Data
@ApiModel("TrOutTransitemVo")
@EqualsAndHashCode(callSuper = false)
public class TrOutTransitemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 伝票番号
     */
    @ApiModelProperty("伝票番号")
    private Integer slipCode;

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
     * 出庫先店舗ID
     */
    @ApiModelProperty("出庫先店舗ID")
    private Integer storeId;

    /**
     * 行番号
     */
    @ApiModelProperty("行番号")
    private Integer lineNo;

    /**
     * JAN
     */
    @ApiModelProperty("JAN")
    private String itemId;

    /**
     * 産地
     */
    @ApiModelProperty("産地")
    private Integer placeId;

    /**
     * ロット番号
     */
    @ApiModelProperty("ロット番号")
    private String lotNo;

    /**
     * 出庫数
     */
    @ApiModelProperty("出庫数")
    private Integer qy;

    /**
     * 出庫重量
     */
    @ApiModelProperty("出庫重量")
    private BigDecimal weightAm;

    /**
     * レシピ原価(PC原価)
     */
    @ApiModelProperty("レシピ原価(PC原価)")
    private BigDecimal costRcp;

    /**
     * 商品原価(店原価)
     */
    @ApiModelProperty("商品原価(店原価)")
    private BigDecimal cost;

    /**
     * 店舗販売売価
     */
    @ApiModelProperty("店舗販売売価")
    private Integer price;

    /**
     * レシピ金額
     */
    @ApiModelProperty("レシピ金額")
    private BigDecimal recipeAm;

    /**
     * 原価金額
     */
    @ApiModelProperty("原価金額")
    private BigDecimal orderAm;

    /**
     * 売価金額
     */
    @ApiModelProperty("売価金額")
    private Integer salesAm;

    /**
     * 商品計量区分ID
     */
    @ApiModelProperty("商品計量区分ID")
    private Integer wmTypeid;

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
    @ApiModelProperty("更新操作ID")
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
