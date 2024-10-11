package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.tr_bacteriacheck_item")
public class TrBacteriaCheckItem extends BaseEntity {

    @TableField("id")
    private Integer id;

    @TableField("seq")
    private Integer seq;

    @TableField("qy")
    private Integer qy;

    @TableField("check_item")
    private String checkItem;

    @TableField("item_name")
    private String itemName;

    @TableField("tempzone_typeid")
    private Integer tempZoneTypeId;

    @TableField("checkobj_typeid")
    private Integer checkObjTypeId;

    @TableField("produce_date")
    private LocalDateTime produceDate;

    @TableField(exist = false)
    private String productDate;

    @TableField(exist = false)
    private String productTime;

    @TableField("f_heated")
    private int fHeated;

    @TableField("f_progress")
    private int fProgress;

    @TableField("otherchecktime")
    private String otherCheckTime;

}
