package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.mt_centerdlvstore")
public class MtCenterdlvstore extends BaseEntity {

    @TableField("center_id")
    private Integer centerId;

    @TableField("line_id")
    private Integer lineId;

    @TableField("store_id")
    private Integer storeId;

    @TableField("shortname")
    private String shortName;

    @TableField("seq")
    private Integer seq;

}
