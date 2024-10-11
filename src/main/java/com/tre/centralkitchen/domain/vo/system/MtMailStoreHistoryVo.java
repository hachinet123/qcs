package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class MtMailStoreHistoryVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * バックアップ日時
     */
    private Date backupDate;

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
    private Integer flag;
}
