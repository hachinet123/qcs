package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.mt_itemcenter_material")
public class MtItemCenterMaterial extends BaseEntity {

    @TableField("center_id")
    private Integer centerId;

    @TableField("item_id")
    private String itemId;

    @TableField("safetystock_qy")
    private Integer safetyStockQy;



    @TableField("exptime_alarm")
    private Integer exptimeAlarm;

    @TableField("freecolumn1")
    private String freeColumn1;

    @TableField("freecolumn2")
    private String freeColumn2;

    @TableField("freecolumn3")
    private String freeColumn3;

    @TableField("freecolumn4")
    private String freeColumn4;

    @TableField("freecolumn5")
    private String freeColumn5;

    @TableField("freecolumn6")
    private String freeColumn6;

    @TableField("freecolumn7")
    private String freeColumn7;

    @TableField("freecolumn8")
    private String freeColumn8;

    @TableField("freecolumn9")
    private String freeColumn9;

    @TableField("freecolumn10")
    private String freeColumn10;

    private String memo;

}
