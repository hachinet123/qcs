package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class shTOrderQtyTestVo {

    private String prodJAN;

    private Integer branchCD;

    private Integer supplierCD;

    private Date deliveryDate;

    private Integer bin;

    private BigDecimal recQuantity;

    private BigDecimal orderQty;

    private BigDecimal custOrderQty;

    private Integer orderKindCD;

    private Integer entryFLG;

    private Integer userCD;

    private Date createTime;

    private Date updateTime;
}
