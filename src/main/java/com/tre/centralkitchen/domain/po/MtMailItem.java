package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 便設定個別マスタ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_mailitem")
public class MtMailItem extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * JAN
     */
    private String itemId;
    /**
     * 店舗ID
     */
    private Integer storeId;
    /**
     * 便番号
     */
    private Integer mailNo;

    /**
     * 備考
     */
    private String memo;
}
