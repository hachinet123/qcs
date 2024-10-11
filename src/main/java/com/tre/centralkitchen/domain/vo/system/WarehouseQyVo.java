package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntityVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class WarehouseQyVo extends BaseEntityVo {

    private Integer centerId;

    private String itemId;

    private BigDecimal instQy;

    private LocalDate dlvSCHedDate;
}
