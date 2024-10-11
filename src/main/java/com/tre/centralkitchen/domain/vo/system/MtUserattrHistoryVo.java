package com.tre.centralkitchen.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MtUserattrHistoryVo implements Serializable {
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
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * ラインID
     */
    private Integer lineId;

    /**
     * フリー項目1
     */
    @TableField("freecolumn_1")
    private Integer freecolumn1;

    /**
     * フリー項目2
     */
    @TableField("freecolumn_2")
    private Integer freecolumn2;

    /**
     * フリー項目3
     */
    @TableField("freecolumn_3")
    private Integer freecolumn3;

    /**
     * フリー項目4
     */
    @TableField("freecolumn_4")
    private Integer freecolumn4;

    /**
     * フリー項目5
     */
    @TableField("freecolumn_5")
    private Integer freecolumn5;
    /**
     * 修正フラグ(1:新規2:更新3:削除)
     */
    private Integer flag;
}
