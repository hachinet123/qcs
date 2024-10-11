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
public class ProductionInstructionPoVo implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    private String id;

    @ApiModelProperty("センターコード")
    private Integer centerId;
    @Alias("センター")
    @ApiModelProperty("センター")
    private String centerName;
    @Alias("生産日")
    @ApiModelProperty("生産日")
    private String scheduleDate;
    @Alias("便")
    @ApiModelProperty("便")
    private String mailNo;

    @ApiModelProperty("ラインコード")
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
    @Alias("生産計画")
    @ApiModelProperty("生産計画")
    private Integer qty;
    @Alias("計量")
    @ApiModelProperty("計量")
    private String itemWeigh;
    @Alias("規格")
    @ApiModelProperty("規格")
    private String itemSpecs;
    @Alias("内容量")
    @ApiModelProperty("内容量")
    private String itemContent;
    @Alias("産地名")
    @ApiModelProperty("産地名")
    private String itemPlace;
    @ApiModelProperty("ロットGP")
    @Alias("ロットGP")
    private String batchGroupName;
    private String batchGroupId;
    private String batchGroup;
    private Integer mailId;
    private String itemId;
}
