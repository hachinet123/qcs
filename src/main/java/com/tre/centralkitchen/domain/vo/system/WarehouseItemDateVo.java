package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class WarehouseItemDateVo {

    @Alias("在庫日")
    @ApiModelProperty(value = "在庫日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    @Alias("納品")
    @ApiModelProperty(value = "納品数")
    private BigDecimal dlvsched;

    @Alias("生産")
    @ApiModelProperty(value = "生産数")
    private BigDecimal instQy;

    @Alias("在庫数")
    @ApiModelProperty(value = "在庫数")
    private BigDecimal tStorkQy;

    private Integer flag;
}
