package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生産指示書自動印刷設定
 *
 * @author 10225441
 * @TableName centralkitchen.mt_instruct_autoprint
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value = "centralkitchen.mt_instruct_autoprint")
@Data
public class MtInstructAutoPrint extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * プロセスセンターID
     */
    @TableField(value = "center_id")
    private Integer centerId;
    /**
     * 便番号
     */
    @TableField(value = "mail_no")
    private Short mailNo;
    /**
     * 作業グループID(カンマ区切り)
     */
    @TableField(value = "wkgrp_id")
    private Short workGroupId;
    /**
     * ラインID
     */
    @TableField(value = "line_id")
    private Short lineId;
    /**
     * 印刷部数
     */
    @TableField(value = "qy")
    private Short qy;
    /**
     * 区別
     */
    @TableField(value = "autoprint_typeid")
    private Integer autoPrintTypeId;
}