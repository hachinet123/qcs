package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.HistoryBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("history.mt_userlogin_history")
public class MtUserloginHistory extends HistoryBaseEntity {
    private static final long serialVersionUID = 1L;
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
    @TableField("update_typeid")
    private Integer updateTypeId;
}
