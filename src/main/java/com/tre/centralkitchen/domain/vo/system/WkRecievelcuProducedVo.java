package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * LCU受信生産実績ワーク
 * </p>
 *
 * @author JDev
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WkRecievelcuProducedVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID")
    private Integer centerId;

    /**
     * 区分
     */
    @ApiModelProperty(value = "区分")
    private String column01;

    /**
     * 便番号
     */
    @ApiModelProperty(value = "便番号")
    private Integer column02;

    /**
     * 品番
     */
    @ApiModelProperty(value = "品番")
    private String column03;

    /**
     * 機械番号
     */
    @ApiModelProperty(value = "機械番号")
    private Integer column04;

    /**
     * 店舗コード
     */
    @ApiModelProperty(value = "店舗コード")
    private Integer column05;

    /**
     * 売単価
     */
    @ApiModelProperty(value = "売単価")
    private Integer column06;

    /**
     * 原単価
     */
    @ApiModelProperty(value = "原単価")
    private Integer column07;

    /**
     * 打ち切り区分
     */
    @ApiModelProperty(value = "打ち切り区分")
    private Integer column08;

    /**
     * 空き
     */
    @ApiModelProperty(value = "空き")
    private String column09;

    /**
     * 実績数
     */
    @ApiModelProperty(value = "実績数")
    private Integer column10;

    /**
     * 実績重量
     */
    @ApiModelProperty(value = "実績重量")
    private Integer column11;

    /**
     * 計量区分
     */
    @ApiModelProperty(value = "計量区分")
    private Integer column12;

    /**
     * 二重売価区分
     */
    @ApiModelProperty(value = "二重売価区分")
    private Integer column13;

    /**
     * 実績金額
     */
    @ApiModelProperty(value = "実績金額")
    private Integer column14;

    /**
     * 加工年月日
     */
    @ApiModelProperty(value = "加工年月日")
    private String column15;

    /**
     * 処理区分
     */
    @ApiModelProperty(value = "処理区分")
    private Integer column16;

    /**
     * 部門コード
     */
    @ApiModelProperty(value = "部門コード")
    private Integer column17;

    /**
     * バーコード情報
     */
    @ApiModelProperty(value = "バーコード情報")
    private String column18;

    /**
     * 実績区分
     */
    @ApiModelProperty(value = "実績区分")
    private Integer column19;

    /**
     * 二重売価
     */
    @ApiModelProperty(value = "二重売価")
    private String column20;

    /**
     * 指示数
     */
    @ApiModelProperty(value = "指示数")
    private Integer column21;

    /**
     * 加工時間
     */
    @ApiModelProperty(value = "加工時間")
    private String column22;

    /**
     * 賞味日
     */
    @ApiModelProperty(value = "賞味日")
    private String column23;

    /**
     * 賞味時間
     */
    @ApiModelProperty(value = "賞味時間")
    private String column24;

    /**
     * 値付け開始時間
     */
    @ApiModelProperty(value = "値付け開始時間")
    private String column25;

    /**
     * 値付け終了時間
     */
    @ApiModelProperty(value = "値付け終了時間")
    private String column26;

    /**
     * 定貫マーク
     */
    @ApiModelProperty(value = "定貫マーク")
    private Integer column27;

    /**
     * 割引モード
     */
    @ApiModelProperty(value = "割引モード")
    private Integer column28;

    /**
     * 計算重量
     */
    @ApiModelProperty(value = "計算重量")
    private Integer column29;

    /**
     * ラベル重量
     */
    @ApiModelProperty(value = "ラベル重量")
    private Integer column30;

    /**
     * 残個数
     */
    @ApiModelProperty(value = "残個数")
    private Integer column31;

    /**
     * 連番
     */
    @ApiModelProperty(value = "連番")
    private String column32;

    /**
     * システムタイプ
     */
    @ApiModelProperty(value = "システムタイプ")
    private Integer column33;

    /**
     * 価格丸め方法
     */
    @ApiModelProperty(value = "価格丸め方法")
    private String column34;

    /**
     * 総額丸め方法
     */
    @ApiModelProperty(value = "総額丸め方法")
    private String column35;

    /**
     * 税額丸め方法
     */
    @ApiModelProperty(value = "税額丸め方法")
    private String column36;

    /**
     * バーコード内価格区分
     */
    @ApiModelProperty(value = "バーコード内価格区分")
    private String column37;

    /**
     * 売価入力区分
     */
    @ApiModelProperty(value = "売価入力区分")
    private String column38;

    /**
     * 本体売単価
     */
    @ApiModelProperty(value = "本体売単価")
    private Integer column39;

    /**
     * 本体売価金額
     */
    @ApiModelProperty(value = "本体売価金額")
    private Integer column40;

    /**
     * トレース番号1
     */
    @ApiModelProperty(value = "トレース番号1")
    private String column41;

    /**
     * トレース番号1区分
     */
    @ApiModelProperty(value = "トレース番号1区分")
    private String column42;

    /**
     * トレース番号2
     */
    @ApiModelProperty(value = "トレース番号2")
    private String column43;

    /**
     * トレース番号2区分
     */
    @ApiModelProperty(value = "トレース番号2区分")
    private String column44;

    /**
     * トレース番号3
     */
    @ApiModelProperty(value = "トレース番号3")
    private String column45;

    /**
     * トレース番号3区分
     */
    @ApiModelProperty(value = "トレース番号3区分")
    private String column46;

    /**
     * 産地コード
     */
    @ApiModelProperty(value = "産地コード")
    private String column47;

    /**
     * 削除フラグ
     */
    @ApiModelProperty(value = "削除フラグ")
    private Integer fDel;

    /**
     * 登録日
     */
    @ApiModelProperty(value = "登録日")
    private LocalDate insDate;

    /**
     * 登録時刻
     */
    @ApiModelProperty(value = "登録時刻")
    private LocalDateTime insTime;

    /**
     * 登録者ID
     */
    @ApiModelProperty(value = "登録者ID")
    private Integer insUserId;

    /**
     * 登録機能ID
     */
    @ApiModelProperty(value = "登録機能ID")
    private Integer insFuncId;

    /**
     * 登録操作ID
     */
    @ApiModelProperty(value = "登録操作ID")
    private Integer insOpeId;

    /**
     * 更新日
     */
    @ApiModelProperty(value = "更新日")
    private LocalDate updDate;

    /**
     * 更新時刻
     */
    @ApiModelProperty(value = "更新時刻")
    private LocalDateTime updTime;

    /**
     * 更新者ID
     */
    @ApiModelProperty(value = "更新者ID")
    private Integer updUserId;

    /**
     * 更新機能ID
     */
    @ApiModelProperty(value = "更新機能ID")
    private Integer updFuncId;

    /**
     * 更新操作ID
     */
    @ApiModelProperty(value = "更新操作ID")
    private Integer updOpeId;


}
