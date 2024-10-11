package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tr_stock", schema = "centralkitchen")
public class TrStock extends BaseEntity {

    @TableField("stock_date")
    private LocalDate stockDate;

    @TableField("center_id")
    private Integer centerId;

    @TableField("warehouse_id")
    private Integer warehouseId;

    @TableField("item_id")
    private String itemId;

    @TableField("cost")
    private BigDecimal cost;

    @TableField("qy")
    private BigDecimal qy;

    @TableField("am")
    private BigDecimal am;
}
