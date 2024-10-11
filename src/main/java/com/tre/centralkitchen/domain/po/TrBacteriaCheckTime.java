package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.tr_bacteriacheck_time")
public class TrBacteriaCheckTime extends BaseEntity {

    @TableField("id")
    private Integer id;

    @TableField("seq")
    private int seq;

    @TableField("checktime_typeid")
    private int checktimeTypeid;




}
