package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * センター別環境設定
 * </p>
 *
 * @author JDev
 * @since 2022-12-12
 */
@Data
@ApiModel("MtCenterstatusVo")
@EqualsAndHashCode(callSuper = false)
public class MtCenterstatusVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * 伝票番号付番管理
     */
    @ApiModelProperty("伝票番号付番管理")
    private Integer slipNo;

    /**
     * チーフコード
     */
    @ApiModelProperty("チーフコード")
    private Integer chiefNo;

    /**
     * 計量器IPアドレス
     */
    @ApiModelProperty("計量器IPアドレス")
    private String lcuIp;

    /**
     * ベンダーID
     */
    @ApiModelProperty("ベンダーID")
    private Integer vendorId;

    /**
     * 伝票コード
     */
    @ApiModelProperty("伝票コード")
    private Integer slipCode;

    /**
     * 機械フラグ
     */
    @ApiModelProperty("機械フラグ")
    private Integer fMachine;

    /**
     * 機械切替時間（分）
     */
    @ApiModelProperty("機械切替時間（分）")
    private Integer machineSwitchTime;

    /**
     * フリー項目1
     */
    @ApiModelProperty("フリー項目1")
    private Integer freecolumn1;

    /**
     * フリー項目2
     */
    @ApiModelProperty("フリー項目2")
    private Integer freecolumn2;

    /**
     * フリー項目3
     */
    @ApiModelProperty("フリー項目3")
    private Integer freecolumn3;

    /**
     * フリー項目4
     */
    @ApiModelProperty("フリー項目4")
    private Integer freecolumn4;

    /**
     * フリー項目5
     */
    @ApiModelProperty("フリー項目5")
    private Integer freecolumn5;

    /**
     * フリー項目6
     */
    @ApiModelProperty("フリー項目6")
    private Integer freecolumn6;

    /**
     * フリー項目7
     */
    @ApiModelProperty("フリー項目7")
    private Integer freecolumn7;

    /**
     * 削除フラグ
     */
    @ApiModelProperty("削除フラグ")
    private Integer fDel;

    /**
     * 登録日
     */
    @ApiModelProperty("登録日")
    private LocalDate insDate;

    /**
     * 登録時刻
     */
    @ApiModelProperty("登録時刻")
    private LocalDateTime insTime;

    /**
     * 登録者ID
     */
    @ApiModelProperty("登録者ID")
    private Integer insUserId;

    /**
     * 登録機能ID
     */
    @ApiModelProperty("登録機能ID")
    private Integer insFuncId;

    /**
     * 登録操作ID
     */
    @ApiModelProperty("登録操作ID")
    private Integer insOpeId;

    /**
     * 更新日
     */
    @ApiModelProperty("更新日")
    private LocalDate updDate;

    /**
     * 更新時刻
     */
    @ApiModelProperty("更新時刻")
    private LocalDateTime updTime;

    /**
     * 更新者ID
     */
    @ApiModelProperty("更新者ID")
    private Integer updUserId;

    /**
     * 更新機能ID
     */
    @ApiModelProperty("更新機能ID")
    private Integer updFuncId;

    /**
     * 更新操作ID
     */
    @ApiModelProperty("更新操作ID")
    private Integer updOpeId;


}
