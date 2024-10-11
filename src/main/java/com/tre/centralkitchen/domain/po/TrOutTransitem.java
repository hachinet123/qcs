package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 出庫明細トラン
 * </p>
 *
 * @author JDev
 * @since 2022-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.tr_out_transitem")
public class TrOutTransitem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 伝票番号
     */
    @ApiModelProperty(value = "伝票番号")
    @TableField(value = "slip_code")
    private Integer slipCode;

    /**
     * 納品予定日
     */
    @ApiModelProperty(value = "納品予定日")
    @TableField(value = "dlvsched_date")
    private Date dlvschedDate;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID")
    @TableField(value = "center_id")
    private Integer centerId;

    /**
     * 便番号
     */
    @ApiModelProperty(value = "便番号")
    @TableField(value = "mail_no")
    private Integer mailNo;

    /**
     * 出庫先店舗ID
     */
    @ApiModelProperty(value = "出庫先店舗ID")
    @TableField(value = "store_id")
    private Integer storeId;

    /**
     * 行番号
     */
    @ApiModelProperty(value = "行番号")
    @TableField(value = "line_no")
    private Integer lineNo;

    /**
     * JAN
     */
    @ApiModelProperty(value = "JAN")
    @TableField(value = "item_id")
    private String itemId;

    /**
     * 産地
     */
    @ApiModelProperty(value = "産地")
    @TableField(value = "place_id")
    private Integer placeId;

    /**
     * ロット番号
     */
    @ApiModelProperty(value = "ロット番号")
    @TableField(value = "lot_no")
    private String lotNo;

    /**
     * 出庫数
     */
    @ApiModelProperty(value = "出庫数")
    @TableField(value = "qy")
    private Integer qy;

    /**
     * 出庫重量
     */
    @ApiModelProperty(value = "出庫重量")
    @TableField(value = "weight_am")
    private BigDecimal weightAm;

    /**
     * レシピ原価(PC原価)
     */
    @ApiModelProperty(value = "レシピ原価(PC原価)")
    @TableField(value = "cost_rcp")
    private BigDecimal costRcp;

    /**
     * 商品原価(店原価)
     */
    @ApiModelProperty(value = "商品原価(店原価)")
    @TableField(value = "cost")
    private BigDecimal cost;

    /**
     * 店舗販売売価
     */
    @ApiModelProperty(value = "店舗販売売価")
    @TableField(value = "price")
    private Integer price;

    /**
     * レシピ金額
     */
    @ApiModelProperty(value = "レシピ金額")
    @TableField(value = "recipe_am")
    private BigDecimal recipeAm;

    /**
     * 原価金額
     */
    @ApiModelProperty(value = "原価金額")
    @TableField(value = "order_am")
    private BigDecimal orderAm;

    /**
     * 売価金額
     */
    @ApiModelProperty(value = "売価金額")
    @TableField(value = "sales_am")
    private Long salesAm;

    /**
     * 商品計量区分ID
     */
    @ApiModelProperty(value = "商品計量区分ID")
    @TableField(value = "wm_typeid")
    private Integer wmTypeid;

}
