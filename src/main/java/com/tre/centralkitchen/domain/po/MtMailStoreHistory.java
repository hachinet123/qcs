package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.HistoryBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("history.mt_mailstore_history")
public class MtMailStoreHistory extends HistoryBaseEntity {

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
    /**
     * 修正フラグ(1:新規2:更新3:削除)
     */
    @TableField("update_typeid")
    private Integer updateTypeId;
}
