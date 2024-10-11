package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 計量実績自動確定保留設定
 * </p>
 *
 * @author JDev
 * @since 2023-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.tr_mailconfirm_hold")
public class TrMailConfirmHold extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    @TableField("dlvsched_date")
    private LocalDate dlvschedDate;

    /**
     * 便番号
     */
    @TableField("center_id")
    private Integer centerId;

    /**
     * 便番号
     */
    @TableField("mail_no")
    private Integer mailNo;

    /**
     * 保留開始時刻
     */
    @TableField("st_date")
    private LocalDateTime stDate;

    /**
     * 保留解除時刻
     */
    @TableField(value = "ed_date",updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime edDate;


}
