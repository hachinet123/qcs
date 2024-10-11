package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * センター別環境設定
 * </p>
 *
 * @author JDev
 * @since 2022-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_centerstatus")
public class MtCenterstatus extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * 伝票番号付番管理
     */
    private Integer slipNo;

    /**
     * チーフコード
     */
    private Integer chiefNo;

    /**
     * 計量器IPアドレス
     */
    private String lcuIp;

    /**
     * ベンダーID
     */
    private Integer vendorId;

    /**
     * 伝票コード
     */
    private Integer slipCode;

    /**
     * 機械フラグ
     * 	1:LCU(テラオカ)
     * 	2:PCRS(イシダ)
     * 	3:イシダ店舗用
     */
    private Integer machineTypeid;

    /**
     * 機械切替時間（分）
     */
    private Integer machineSwitchTime;

    /**
     * 伝票番号採番(最小値)
     */
    private Integer minSlipNo;

    /**
     * 伝票番号採番(最大値)
     */
    private Integer maxSlipNo;

    /**
     * lcu_id
     */
    @TableField("lcu_id")
    private String lcuId;

    /**
     * lcu_pass
     */
    @TableField("lcu_pass")
    private String lcuPass;

    /**
     * 実績自動確定フラグ0:表示  1：不表示
     */
    @TableField("f_autoconfirm")
    private Integer fAutoconfirm;

    /**
     * 原材料平均使用量計算開始曜日
     */
    @TableField("calcst_wday")
    private Integer calcstWday;

    /**
     * フリー項目5
     */
    @TableField("freecolumn_5")
    private Integer freecolumn5;

    /**
     * フリー項目6
     */
    @TableField("freecolumn_6")
    private Integer freecolumn6;

    /**
     * フリー項目7
     */
    @TableField("freecolumn_7")
    private Integer freecolumn7;

}
