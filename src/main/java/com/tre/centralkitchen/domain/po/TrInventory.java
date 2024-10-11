package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 棚卸トラン
 * </p>
 *
 * @author 10253955
 * @since 2023-12-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tr_inventory", schema = "centralkitchen")
public class TrInventory extends BaseEntity {

    /**
     * 棚卸日
     */
    private LocalDate invDate;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * 倉庫ID
     */
    private Integer warehouseId;

    /**
     * JAN(原材料・副資材)
     */
    private String itemId;

    /**
     * カウント数（ケース）
     */
    private Integer caseQy;

    /**
     * カウント数（袋本）
     */
    private Integer innerQy;

    /**
     * 在庫数
     */
    private BigDecimal qy;

    /**
     * 消費期限1
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDate exptime01Date;

    /**
     * 消費期限2
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private LocalDate exptime02Date;

    /**
     * 消費期限3
     */
    private LocalDate exptime03Date;

    /**
     * 消費期限4
     */
    private LocalDate exptime04Date;

    /**
     * 消費期限5
     */
    private LocalDate exptime05Date;

    /**
     * カウント者ID
     */
    private Integer countUserId;


}
