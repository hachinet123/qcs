package com.tre.centralkitchen.service.impl;
import lombok.Data;
import java.util.Date;

@Data
public class ShWOrderQtyVo {

    private String itemId;

    private Date deliveryDate;

    private Integer orderQty;
}
