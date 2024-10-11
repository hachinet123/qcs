package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2023-12-26 10:31
 */
@Data
@ApiModel("DailyInventoryBo")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class DailyInventoryBo extends BaseEntityBo {

    private Integer centerId;

    /**
     * 倉庫名
     */
    @ApiModelProperty("倉庫名")
    private String warehouseName;

    /**
     * 原材料ID 名
     */
    @ApiModelProperty("原材料")
    private String itemIdName;

    /**
     * 規格 × 入数 ベンダ
     */
    @ApiModelProperty("規格 × 入数 ベンダ")
    private String specInnerCaseQtySupplier;

    /**
     * 論理在庫
     */
    @ApiModelProperty("論理在庫")
    private String qy;

    /**
     * 在庫金額 case_qy * case_cost + short_qy * short_cost
     */
    @ApiModelProperty("在庫金額 case_qy * case_cost + short_qy * short_cost")
    private BigDecimal am;

    /**
     * 倉庫ID
     */
    private Integer warehouseId;

    /**
     * JAN(原材料・副資材)
     */
    private String itemId;

    /**
     * 原材料名
     */
    private String itemName;

    /**
     * 規格分量
     */
    private Integer nSzName;

    /**
     * ユニット名
     */
    private String unitName;

    /**
     * 規格
     */
    private String specName;

    /**
     * 規格 × 入数
     */
    private String szNameInnerCaseQty;

    /**
     * 入数
     */
    private Integer innerCaseQty;

    /**
     * ベンダーCD
     */
    private Integer vendorId;

    /**
     * ベンダー名
     */
    private String supplierName;

    /**
     * 論理在庫数の整数部分:ケース
     */
    @ApiModelProperty("論理在庫数の整数部分:ケース")
    private Integer caseQy;

    /**
     * 論理在庫数の少数部分：袋or本
     */
    @ApiModelProperty("論理在庫数の少数部分：袋or本")
    private Integer shortQy;

    /**
     * カウント原価(ケース)
     */
    @ApiModelProperty("カウント原価(ケース)")
    private String caseCost;

    /**
     * カウント原価(袋or本)
     */
    @ApiModelProperty("カウント原価(袋or本)")
    private String shortCost;

    /**
     * 賞味期限1
     */
    @ApiModelProperty("賞味期限1")
    private LocalDate expTimeDate01;

    /**
     * 賞味期限2
     */
    @ApiModelProperty("賞味期限2")
    private LocalDate expTimeDate02;
}
