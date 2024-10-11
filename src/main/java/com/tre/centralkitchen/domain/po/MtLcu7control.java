package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_lcu7control")
public class MtLcu7control implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * オペレーションタイプ
     */
    private Integer opType;

    /**
     * 表示順
     */
    private Integer seq;

    /**
     * 送信ファイル名
     */
    private String sendFilename;

    /**
     * 受信ファイル名
     */
    private String recvFilename;

    /**
     * 表示文字
     */
    private String display;

    /**
     * テーブル名
     */
    private String tableName;

    /**
     * クリアシーケンス
     */
    private Integer clearSeq;

    /**
     * 削除フラグ
     */
    private Integer fDel;

    /**
     * 登録日
     */
    private Date insDate;

    /**
     * 登録時刻
     */
    private Date insTime;

    /**
     * 登録者ID
     */
    private Integer insUserId;

    /**
     * 登録機能ID
     */
    private Integer insFuncId;

    /**
     * 登録操作ID
     */
    private Integer insOpeId;

    /**
     * 更新日
     */
    private Date updDate;

    /**
     * 更新時刻
     */
    private Date updTime;

    /**
     * 更新者ID
     */
    private Integer updUserId;

    /**
     * 更新機能ID
     */
    private Integer updFuncId;

    /**
     * 更新操作ID
     */
    private Integer updOpeId;


}
