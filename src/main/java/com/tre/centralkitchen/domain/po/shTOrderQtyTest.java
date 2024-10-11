package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("dbo.shT_OrderQty_test")
public class shTOrderQtyTest {
    @TableField("ProdJAN")
    private String prodJAN;
    @TableField("BranchCD")
    private Integer branchCD;
    @TableField("SupplierCD")
    private Integer supplierCD;
    @TableField("DeliveryDate")
    private Date deliveryDate;
    @TableField("Bin")
    private Integer bin;
    @TableField("RecQuantity")
    private BigDecimal recQuantity;
    @TableField("OrderQty")
    private BigDecimal orderQty;
    @TableField("CustOrderQty")
    private BigDecimal custOrderQty;
    @TableField("OrderKindCD")
    private Integer orderKindCD;
    @TableField("EntryFLG")
    private Integer entryFLG;
    @TableField("UserCD")
    private Integer userCD;
    @TableField("CreateTime")
    private Date createTime;
    @TableField("UpdateTime")
    private Date updateTime;
}
