package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 10225441
 * @version 10116842
 */
@Data
public class DistributionInstructionPoVo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    private String id;
    private String isClosed;
    @Alias("センター")
    @ApiModelProperty("センター")
    private String centerName;
    @Alias("生産日")
    @ApiModelProperty("生産日")
    private String scheduleDate;
    @Alias("便")
    @ApiModelProperty("便")
    private String mailNo;
    @ApiModelProperty(value = "ライン")
    private Integer lineId;
    @Alias("ライン")
    @ApiModelProperty("ライン")
    private String lineName;
    @ApiModelProperty("作業グループId")
    private Integer workGroupId;
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
    @Alias("店舗コード")
    @ApiModelProperty("店舗コード")
    private String storeId;
    @Alias("店舗名")
    @ApiModelProperty("店舗名")
    private String storeName;
    private String storeNameShort;
    @Alias("数量")
    @ApiModelProperty("数量")
    private String qty;
    private Integer seq;
    private String mailId;
    private String itemId;
}
