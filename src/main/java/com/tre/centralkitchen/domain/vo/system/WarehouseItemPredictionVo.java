package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class WarehouseItemPredictionVo extends BaseEntityVo {

    private Integer id;

    @ApiModelProperty(value = "センターID")
    private Integer centerId;

    @ApiModelProperty(value = "センター")
    private String centerName;

    @Alias("倉庫")
    @ApiModelProperty(value = "倉庫")
    private String warehouseName;

    @ApiModelProperty(value = "倉庫ID")
    private Integer warehouseId;

    @Alias("原材料JAN")
    @ApiModelProperty(value = "原材料JAN")
    private String itemId;

    @Alias("原材料名")
    @ApiModelProperty(value = "原材料名")
    private String itemName;

    @Alias("安全在庫")
    @ApiModelProperty(value = "安全在庫")
    private Integer safetyStockQy;

    @Alias("賞味期限")
    @ApiModelProperty(value = "賞味期限")
    private Integer expTime;

    @Alias("単位")
    @ApiModelProperty(value = "単位")
    private String unit;

    @ApiModelProperty(value = "在庫数")
    private BigDecimal tStorkQy;

    @ApiModelProperty(value = "在庫日")
    private LocalDate stockDate;

    @Alias("ベンダーCD")
    @ApiModelProperty(value = "ベンダーCD")
    private Integer vendorId;

    @Alias("ベンダー名")
    @ApiModelProperty(value = "ベンダー名")
    private String supplierName;

    @Alias("廃盤フラグ")
    @ApiModelProperty(value ="廃盤フラグ")
    private Integer fActive;

    private String vendorName;

    @Alias("")
    private List<WarehouseItemDateVo> WarehouseItemDate;

}
