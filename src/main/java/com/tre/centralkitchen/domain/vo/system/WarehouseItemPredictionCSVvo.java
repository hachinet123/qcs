package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WarehouseItemPredictionCSVvo {

    @Alias("倉庫")
    @ApiModelProperty(value = "倉庫")
    private String warehouseName;

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

//    @ApiModelProperty(value = "在庫数")
//    private Integer tStorkQy;
//
//    @ApiModelProperty(value = "在庫日")
//    private LocalDate stockDate;

    @Alias("ベンダーCD")
    @ApiModelProperty(value = "ベンダーCD")
    private Integer vendorId;

    @Alias("ベンダー名")
    @ApiModelProperty(value = "ベンダー名")
    private String supplierName;

    @Alias("廃盤フラグ")
    @ApiModelProperty(value ="廃盤フラグ")
    private Integer fActive;


    @Alias("在庫日")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate1;

    @Alias("納品")
    @ApiModelProperty(value = "納品数")
    private Integer dlvsched1;

    @Alias("生産")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy1;

    @Alias("在庫数")
    @ApiModelProperty(value = "在庫数")
    private Integer tStorkQy1;

    @Alias("在庫日2")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate2;

    @Alias("納品2")
    @ApiModelProperty(value = "納品数")
    private Integer dlvsched2;

    @Alias("生産2")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy2;

    @Alias("在庫数2")
    @ApiModelProperty(value = "在庫数")
    private Integer tStorkQy2;

    @Alias("在庫日3")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate3;

    @Alias("納品3")
    @ApiModelProperty(value = "納品数")
    private Integer dlvsched3;

    @Alias("生産3")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy3;

    @Alias("在庫数3")
    @ApiModelProperty(value = "在庫数")
    private Integer tStorkQy3;

    @Alias("在庫日4")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate4;

    @Alias("納品4")
    @ApiModelProperty(value = "納品数")
    private Integer dlvsched4;

    @Alias("生産4")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy4;

    @Alias("在庫数4")
    @ApiModelProperty(value = "在庫数")
    private Integer tStorkQy4;

    @Alias("在庫日5")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate5;

    @Alias("納品5")
    @ApiModelProperty(value = "納品数")
    private Integer dlvsched5;

    @Alias("生産5")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy5;

    @Alias("在庫数5")
    @ApiModelProperty(value = "在庫数")
    private Integer tStorkQy5;

    @Alias("在庫日6")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate6;

    @Alias("納品6")
    @ApiModelProperty(value = "納品数")
    private Integer dlvsched6;

    @Alias("生産6")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy6;

    @Alias("在庫数6")
    @ApiModelProperty(value = "在庫数")
    private Integer tStorkQy6;

    @Alias("在庫日7")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate7;

    @Alias("納品7")
    @ApiModelProperty(value = "納品数")
    private Integer dlvsched7;

    @Alias("生産7")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy7;

    @Alias("在庫数7")
    @ApiModelProperty(value = "在庫数")
    private Integer tStorkQy7;
}
