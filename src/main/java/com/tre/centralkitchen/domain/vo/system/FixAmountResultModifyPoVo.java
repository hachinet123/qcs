package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 10225441
 * @version 10116842
 */
@Data
@ApiModel(value = "定額指示修正")
public class FixAmountResultModifyPoVo implements Serializable {
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = 1L;
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private String id;
    @ApiModelProperty(hidden = true)
    private String centerId;
    @ApiModelProperty(hidden = true)
    private String teikanTypeId;
    @ApiModelProperty(value = "納品予定日")
    private String scheduleDate;
    @Alias("品番")
    @ApiModelProperty(value = "品番")
    private String callCode;
    @Alias("JAN")
    @ApiModelProperty(value = "JAN")
    private String itemId;
    @Alias("商品名")
    @ApiModelProperty(value = "商品名")
    private String itemName;
    @Alias("便")
    @ApiModelProperty(value = "便")
    private String mailNo;
    private String mailId;
    private String lineId;
    @Alias("ライン")
    @ApiModelProperty(value = "ライン")
    private String lineName;
    @Alias("店舗コード")
    private String storeId;
    @ApiModelProperty(value = "CD:店舗")
    private String storeName;
    @Alias("店舗名")
    @ApiModelProperty(value = "店舗名")
    private String storeNameAbbr;
    private String placeId;
    @ApiModelProperty(value = "ロットグループNo")
    private String batchGroupNo;
    @Alias("数量")
    @ApiModelProperty(value = "数量")
    private String qty;
    @Alias("重量")
    @ApiModelProperty(value = "重量")
    private String weight;
    @Alias("PC原価")
    @ApiModelProperty(value = "PC原価")
    private String pcPrice;
    @Alias("店原価")
    @ApiModelProperty(value = "店原価")
    private String itemPrice;
    @Alias("店売価")
    @ApiModelProperty(value = "店売価")
    private String price;
    @Alias("PC原価金額")
    @ApiModelProperty(value = "PC原価金額")
    private String pcPriceAll;
    @Alias("店原価金額")
    @ApiModelProperty(value = "店原価金額")
    private String itemPriceAll;
    @Alias("店売価金額")
    @ApiModelProperty(value = "店売価金額")
    private String priceAll;
    @Alias("産地名")
    @ApiModelProperty(value = "産地名")
    private String placeName;
    @Alias("ロットNo")
    @ApiModelProperty(value = "ロットNo")
    private String lotNo;
    @ApiModelProperty(value = "修正済")
    private Integer alreadyUpdate;
}
