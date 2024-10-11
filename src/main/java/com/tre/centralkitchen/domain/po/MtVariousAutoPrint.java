package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 各種自動印刷設定
 *
 * @author 10225441
 * @date 2023-12-19 13:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "mt_various_auto_print", schema = "centralkitchen")
public class MtVariousAutoPrint extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * プロセスセンターID
     */
    @TableField(value = "center_id")
    private Integer centerId;

    /**
     * 印刷種別区分ID(3:作業報告書、4:ラベルチェックリスト)
     */
    @TableField(value = "autoprint_typeid")
    private Integer autoPrintTypeId;

    /**
     * ラインID
     */
    @TableField(value = "line_id")
    private Integer lineId;

    /**
     * 印刷部数
     */
    @TableField(value = "qy")
    private Integer qy;
}