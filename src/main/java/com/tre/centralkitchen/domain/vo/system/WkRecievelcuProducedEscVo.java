package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class WkRecievelcuProducedEscVo extends BaseEntityVo {

    /**
     * 退避日時
     */
    @Alias("退避日時")
    private LocalDateTime escDate;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * 区分
     */
    private String column01;

    /**
     * 便番号
     */
    @Alias("便番号")
    private Integer column02;

    /**
     * 呼出品番
     */
    @Alias("呼出品番")
    private String column03;

    @Alias("商品CD")
    private String itemId;

    @Alias("商品名")
    private String itemName;

    /**
     * 機械番号
     */
    @Alias("機械番号")
    private Integer column04;

    /**
     * 店舗コード
     */
    @Alias("店舗コード")
    private Integer column05;

    @Alias("店舗名")
    private String storeName;

    /**
     * 指示数
     */
    @Alias("指示数")
    private Integer column21;

    /**
     * 実績数
     */
    @Alias("実績数")
    private Integer column10;

    /**
     * 売単価
     */
    @Alias("売単価")
    private Integer column06;

    /**
     * 実績重量
     */
    @Alias("実績重量")
    private Integer column11;

    /**
     * 値付け開始時間
     */
    @Alias("値付け開始時間")
    private String column25;

    /**
     * 値付け終了時間
     */
    @Alias("値付け終了時間")
    private String column26;

    /**
     * 原単価
     */
    private Integer column07;

    /**
     * 打ち切り区分
     */
    private Integer column08;

    /**
     * 空き
     */
    private String column09;

    /**
     * 計量区分
     */
    private Integer column12;

    /**
     * 二重売価区分
     */
    private Integer column13;

    /**
     * 実績金額
     */
    private Integer column14;

    /**
     * 加工年月日
     */
    @Alias("加工年月日")
    private String column15;

    /**
     * 加工時間
     */
    @Alias("加工時間")
    private String column22;

    /**
     * 処理区分
     */
    private Integer column16;

    /**
     * 部門コード
     */
    private Integer column17;

    /**
     * バーコード情報
     */
    private String column18;

    /**
     * 実績区分
     */
    private Integer column19;

    /**
     * 二重売価
     */
    private String column20;

    /**
     * 賞味日
     */
    @Alias("賞味日")
    private String column23;

    /**
     * 賞味時間
     */
    @Alias("賞味時間")
    private String column24;


    /**
     * 定貫マーク
     */
    private Integer column27;

    /**
     * 割引モード
     */
    private Integer column28;

    /**
     * 計算重量
     */
    private Integer column29;

    /**
     * ラベル重量
     */
    private Integer column30;

    /**
     * 残個数
     */
    private Integer column31;

    /**
     * 連番
     */
    private String column32;

    /**
     * システムタイプ
     */
    private Integer column33;

    /**
     * 価格丸め方法
     */
    private String column34;

    /**
     * 総額丸め方法
     */
    private String column35;

    /**
     * 税額丸め方法
     */
    private String column36;

    /**
     * バーコード内価格区分
     */
    private String column37;

    /**
     * 売価入力区分
     */
    private String column38;

    /**
     * 本体売単価
     */
    private Integer column39;

    /**
     * 本体売価金額
     */
    private Integer column40;

    /**
     * トレース番号1
     */
    @Alias("トレース番号1")
    private String column41;

    /**
     * トレース番号1区分
     */
    private String column42;

    /**
     * トレース番号2
     */
    @Alias("トレース番号2")
    private String column43;

    /**
     * トレース番号2区分
     */
    private String column44;

    /**
     * トレース番号3
     */
    @Alias("トレース番号3")
    private String column45;

    /**
     * トレース番号3区分
     */
    private String column46;

    /**
     * 産地コード
     */
    @Alias("産地コード")
    private String column47;

    @Alias("産地名")
    private String placeName;

    @Alias("処理")
    private String handle;

}
