package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 便コントロール
 * </p>
 *
 * @author JDev
 * @since 2022-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_mailcontrol")
public class MtMailcontrol extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 納品予定日
     */
    private LocalDate dlvschedDate;

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
     * 生産商品確定日時
     */
    private Date produceplanConfirmedDate;

    /**
     * 生産指示日時
     */
    private Date produceInstDate;

    /**
     * 生産実績取得日時
     */
    private LocalDateTime producedImportDate;

    /**
     * 計量実績確定日時
     */
    private Date producedConfirmedDate;

    /**
     * 定額実績確定日時
     */
    private Date throughConfirmedDate;

    /**
     * 出庫確定日時
     */
    private Date outConfirmedDate;

}
