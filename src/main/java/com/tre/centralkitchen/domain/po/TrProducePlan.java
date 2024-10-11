package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 生産計画トラン
 * </p>
 *
 * @author JDev
 * @since 2023-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.tr_produceplan")
public class TrProducePlan extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 納品予定日
     */
    private Date dlvschedDate;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * 便番号
     */
    private Integer mailNo;

    /**
     * JAN
     */
    private String itemId;

    /**
     * 店舗ID
     */
    private Integer storeId;

    /**
     * 生産計画数
     */
    private Integer qy;

}
