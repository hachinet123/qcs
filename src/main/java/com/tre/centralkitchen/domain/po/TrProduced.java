package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

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
@TableName("centralkitchen.tr_produced")
public class TrProduced extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 納品予定日
     */
    private Date dlvschedDate;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * 便番号
     */
    private Integer mailNo;

    /**
     * JAN
     */
    private String itemId;

    /**
     * 店舗ID
     */
    private Integer storeId;

    /**
     * 生産指示数
     */
    private Integer instQy;

    /**
     * 生産実績数
     */
    private Integer actQy;

    /**
     * 生産時点レシピ原価(PC原価)
     */
    private BigDecimal costRcp;

    /**
     * 生産時点商品原価(店原価)
     */
    private BigDecimal costItem;

    /**
     * 生産時点店舗販売売価
     */
    private Integer price;

    /**
     * 産地ID
     */
    private Integer placeId;

    /**
     * ロット番号
     */
    private String lotNo;

    /**
     * 生産実績重量
     */
    private BigDecimal weight;

    /**
     * 機械番号
     */
    private Integer machineNo;
}
