package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 出庫伝票トラン
 * </p>
 *
 * @author JDev
 * @since 2022-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.tr_out_transbill")
public class TrOutTransbill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 伝票番号
     */
    private Integer slipCode;


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
     * 出庫先店舗ID
     */
    private Integer storeId;

    /**
     * 出庫日
     */
    private Date outDate;

    /**
     * 出庫総数
     */
    private Integer qyAm;

    /**
     * 出庫総重量
     */
    private BigDecimal weightAm;

    /**
     * レシピ金額
     */
    private BigDecimal rAm;

    /**
     * 原価金額
     */
    private BigDecimal oAm;

    /**
     * 売価金額
     */
    private Long sAm;

    private Integer departmentId;

    /**
     * 実績計量区分ID
     */
    private Integer wmTypeid;
}
