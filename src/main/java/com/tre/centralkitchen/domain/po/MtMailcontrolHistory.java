package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.HistoryBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 便コントロール履歴
 * </p>
 *
 * @author JDev
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("history.mt_mailcontrol_history")
public class MtMailcontrolHistory extends HistoryBaseEntity {

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
     * 納品予定日
     */
    private LocalDate dlvschedDate;

    /**
     * 生産商品確定日時
     */
    private LocalDateTime produceplanConfirmedDate;

    /**
     * 受注日時
     */
    private LocalDateTime orderDate;

    /**
     * 生産指示日時
     */
    private LocalDateTime produceInstDate;

    /**
     * 生産実績取得日時
     */
    private LocalDateTime producedImportDate;

    /**
     * 計量実績確定日時
     */
    private LocalDateTime producedConfirmedDate;

    /**
     * 定額実績確定日時
     */
    private LocalDateTime throughConfirmedDate;

    /**
     * 出庫確定日時
     */
    private LocalDateTime outConfirmedDate;

    /**
     * 削除フラグ
     */
    private Integer fDel;

    /**
     * 修正フラグ(1:新規2:更新3:削除)
     */
    @TableField("update_typeid")
    private Integer updateTypeId;

}
