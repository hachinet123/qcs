package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2023-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.wk_decide_production_results")
public class WkDecideProductionResults {

    private static final long serialVersionUID = 1L;

    private String id;

    private Integer slipCode;

    private Integer lineNo;

    private LocalDate dlvschedDate;

    private Integer mailNo;

    private Integer storeId;

    private String itemId;

    private Integer placeId;

    private String lotNo;

    private Integer machineNo;

    private BigDecimal costRcp;

    private BigDecimal costItem;

    private Integer price;

    private Integer instQy;

    private Integer actQy;

    private BigDecimal weight;

    private Integer departmentId;

    private Integer userId;


}
