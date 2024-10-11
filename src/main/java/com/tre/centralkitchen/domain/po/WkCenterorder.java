package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 発注統合受注ワーク
 * </p>
 *
 * @author JDev
 * @since 2023-03-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.wk_centerorder")
public class WkCenterorder extends BaseEntity {

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
     * 店舗ID
     */
    private Integer storeId;

    /**
     * JAN
     */
    private String itemId;

    /**
     * 受注数
     */
    private Integer qy;

}
