package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MtUserloginHistoryVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * バックアップ日時
     */
    private Date backupDate;
    /**
     * ログインコード(社員番号など)
     */
    private String code;

    /**
     * 備考
     */
    private String memo;
    /**
     * 修正フラグ(1:新規2:更新3:削除)
     */
    private Integer flag;
}
