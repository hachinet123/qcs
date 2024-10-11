package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.naming.ldap.PagedResultsControl;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.mt_warehouseitem")
public class MtWarehouseitem extends BaseEntity {

    @TableField("center_id")
    private Integer centerId;

    @TableField("warehouse_id")
    private Integer warehouseId;

    @TableField("item_id")
    private String itemId;


    private Integer seq;

    private String memo;

}
