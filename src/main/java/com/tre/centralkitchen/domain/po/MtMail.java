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
 * @since 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_mail")
public class MtMail extends BaseEntity {

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
     * リードタイム
     */
    private Integer leadtime;

    /**
     * 選択グループ
     */
    private String selGroup;

    /**
     * 生産実績自動確定時刻
     */
    private String producedconfirmTime;

    /**
     * プリンターIPアドレス
     */
    private String printIp;

    /**
     * 説明
     */
    private String discript;

    /**
     * 備考
     */
    private String memo;

    /**
     * 表示順
     */
    private Integer seq;
}
