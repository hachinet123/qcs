package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IssueCheckDataVo {
    /**
     * プロセスセンターName
     */
    @Alias("センター")
    @ApiModelProperty("プロセスセンターName")
    private String centerName;
    /**
     * 便
     */
    @Alias("便")
    @ApiModelProperty("便")
    private String mail;
    /**
     * ライン
     */
    @Alias("ライン")
    @ApiModelProperty("ライン")
    private String lineName;
    /**
     * JAN
     */
    @Alias("JAN")
    @ApiModelProperty("JAN")
    private String itemId;
    /**
     * 品番
     */
    @Alias("品番")
    @ApiModelProperty("品番")
    private Integer callCode;
    /**
     * 商品名
     */
    @Alias("商品名")
    @ApiModelProperty("商品名")
    private String itemName;
    /**
     * 店舗コード
     */
    @Alias("店舗コード")
    @ApiModelProperty("店舗コード")
    private Integer storeId;
    /**
     * 店舗名
     */
    @Alias("店舗名")
    @ApiModelProperty("店舗名")
    private String storeName;
    /**
     * 発注数
     */
    @Alias("発注数")
    @ApiModelProperty("発注数")
    private Integer orderNumber;
    /**
     * 納品数
     */
    @Alias("納品数")
    @ApiModelProperty("納品数")
    private Integer deliveryNumber;

    @Alias("重量")
    @ApiModelProperty(value = "重量")
    @NumberFormat("#,##0.00")
    private BigDecimal weightAm;

    @ApiModelProperty(value = "0/1/2")
    private Integer teikanTypeid;

    @Alias("PC原価")
    @ApiModelProperty(value = "PC原価")
    @NumberFormat("#,##0.00")
    private BigDecimal costRcp;

    @Alias("店原価")
    @ApiModelProperty(value = "店原価")
    @NumberFormat("#,##0.00")
    private BigDecimal cost;

    @Alias("店売価")
    @ApiModelProperty(value = "店売価")
    @NumberFormat("#,##0")
    private Integer price;

    @Alias("PC原価金額")
    @ApiModelProperty(value = "PC原価金額")
    private BigDecimal costRcpAm;

    @Alias("店原価金額")
    @ApiModelProperty(value = "店原価金額")
    private BigDecimal costAm;

    @Alias("店売価金額")
    @ApiModelProperty(value = "店売価金額")
    private BigDecimal priceAm;

    /**
     * 納品予定日
     */
    @Alias("納品予定日")
    @ApiModelProperty("納品予定日")
    private String dlvschedDate;
    /**
     * 産地
     */
    @Alias("産地名")
    @ApiModelProperty("産地名")
    private String placeName;
    /**
     * ロットNo
     */
    @Alias("ロットNo")
    @ApiModelProperty("ロットNo")
    private String lotNo;
    /**
     * 便id
     */
    @ApiModelProperty("便id")
    private Integer mailNo;

}
