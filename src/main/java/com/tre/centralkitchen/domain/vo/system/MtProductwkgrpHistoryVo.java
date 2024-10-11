package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntityVo;
import lombok.Data;

import java.util.Date;

@Data
public class MtProductwkgrpHistoryVo extends BaseEntityVo {
    private static final long serialVersionUID = 1L;
    /**
     * バックアップ日時
     */
    private Date backupDate;
    /**
     * 作業グループID
     */
    private Integer id;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * ラインID
     */
    private Integer lineId;

    /**
     * 作業グループ名称
     */
    private String name;

    /**
     * 作業グループ名称カナ
     */
    private String nameK = "";

    /**
     * 作業グループ略称
     */
    private String nameS = "";

    /**
     * 作業グループ_親ID/0:第一階層の意(親なし)
     */
    private Integer pid;

    /**
     * 表示順
     */
    private Integer seq;
    /**
     * 修正フラグ(1:新規2:更新3:削除)
     */
    private Integer flag;
}
