package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 10225441
 * @version 10116842
 */
@Data
public class DistributionInstructionVo {
    private String id;
    private String isClosed;
    private String totalCount;
    private String storeNameShort;
    @Alias("生産日")
    @ApiModelProperty("生産日")
    private String scheduleDate;
    @Alias("便")
    @ApiModelProperty("便")
    private String mailNo;
    @Alias("ライン")
    @ApiModelProperty("ライン")
    private String lineName;
    @Alias("作業グループ")
    @ApiModelProperty("作業グループ")
    private String workGroupName;
    @Alias("品番")
    @ApiModelProperty("品番")
    private String callCode;
    @Alias("商品名")
    @ApiModelProperty("商品名")
    private String itemName;
    @Alias("規格")
    @ApiModelProperty("規格")
    private String itemSpecs;
    @Alias("数量")
    @ApiModelProperty("数量")
    private String qty;
    @Alias("店舗コード")
    @ApiModelProperty("店舗コード")
    private String storeId;
}
