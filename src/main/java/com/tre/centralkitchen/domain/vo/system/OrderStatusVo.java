package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.tre.centralkitchen.common.annotation.CsvColumn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Order status vo
 *
 * @date 2022-11-25
 */
@Data
@ApiModel("OrderStatusVo")
public class OrderStatusVo {

    @ApiModelProperty("センターコード")
    private Integer supplierCd;

    @ApiModelProperty("センター")
    @Alias("センター")
    @CsvColumn(position = 0, columnName = "センター")
    private String supplierName;

    @ApiModelProperty("便")
    @Alias("便")
    private String mailNo;

    @ApiModelProperty("表示の便")
    @CsvColumn(position = 1, columnName = "便")
    private String mailNoShow;

    @ApiModelProperty("ラインCD")
    private Integer lineId;

    @ApiModelProperty("ライン")
    @Alias("ライン")
    @CsvColumn(position = 2, columnName = "ライン")
    private String lineName;

    @ApiModelProperty("作業グループCD")
    private Integer workGroupId;

    @ApiModelProperty("作業グループ")
    @CsvColumn(position = 3, columnName = "作業グループ")
    @Alias("作業グループ")
    private String workGroupName;

    @ApiModelProperty("JAN")
    @CsvColumn(position = 4, columnName = "JAN")
    @Alias("JAN")
    private String jan;

    @ApiModelProperty("品番")
    @CsvColumn(position = 5, columnName = "品番")
    @Alias("品番")
    private Integer callCd;

    @ApiModelProperty("商品名")
    @CsvColumn(position = 6, columnName = "商品名")
    @Alias("商品名")
    private String productName;

    @Alias("規格")
    @CsvColumn(position = 7, columnName = "規格")
    @ApiModelProperty("規格")
    private String itemSpecs;

    @Alias("内容量")
    @CsvColumn(position = 8, columnName = "内容量")
    @ApiModelProperty("内容量")
    private String itemContent;

    @ApiModelProperty("受注数")
    @CsvColumn(position = 9, columnName = "受注数")
    @Alias("受注数")
    private Integer orderQty;

    @ApiModelProperty("納品予定日")
    @CsvColumn(position = 10, columnName = "納品予定日")
    @Alias("納品予定日")
    private String deliveryDate;

}
