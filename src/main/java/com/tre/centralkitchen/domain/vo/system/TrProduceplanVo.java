package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TrProduceplanVo {
    /**
     * 納品予定日
     */
    @ApiModelProperty("納品予定日")
    private LocalDate dlvschedDate;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * 便番号
     */
    @ApiModelProperty("便番号")
    private Integer mailNo;

    /**
     * JAN
     */
    @ApiModelProperty("JAN")
    private String itemId;

    /**
     * 店舗ID
     */
    @ApiModelProperty("店舗ID")
    private Integer storeId;

    /**
     * 生産計画数
     */
    @ApiModelProperty("生産計画数")
    private Integer qy;
}
