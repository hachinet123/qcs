package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.HistoryBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 便設定個別マスタ
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("history.mt_mailitem_history")
public class MtMailItemHistory extends HistoryBaseEntity {

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
    /**
     * 修正フラグ(1:新規2:更新3:削除)
     */
    @TableField("update_typeid")
    private Integer updateTypeId;
}
