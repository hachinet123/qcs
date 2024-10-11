package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2023-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_mailstore")
public class MtMailstore extends BaseEntity {

    private static final long serialVersionUID = 1L;

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
     * 備考
     */
    private String memo;
}
