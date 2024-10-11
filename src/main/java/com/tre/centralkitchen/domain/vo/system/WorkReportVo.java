package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * 作業報告書
 * </p>
 *
 * @author 10253955
 * @since 2023-12-19 13:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class WorkReportVo {

    @ApiModelProperty("ID")
    private String id;

    /**
     * センター
     */
    @ApiModelProperty("センター")
    private Integer centerId;

    /**
     * センター
     */
    @Alias("センター")
    @ApiModelProperty("センター")
    private String centerName;

    /**
     * 納品予定日 yyyy-MM-dd
     */
    @ApiModelProperty("納品予定日 yyyy-MM-dd")
    private LocalDate dlvschedDate;

    /**
     * 生産日 or 納品予定日 yyyy/MM/dd
     */
    @Alias("生産日/納品予定日")
    @ApiModelProperty("生産日/納品予定日 yyyy/MM/dd")
    private String scheduleDateStr;

    /**
     * 生産日
     */
    @ApiModelProperty("生産日")
    private LocalDate productDate;

    /**
     * 便
     */
    @Alias("便")
    @ApiModelProperty("便")
    private Integer mailNo;

    /**
     * ラインid
     */
    @ApiModelProperty("ラインid")
    private Integer lineId;

    /**
     * ライン
     */
    @Alias("ライン")
    @ApiModelProperty("ライン")
    private String lineName;

    /**
     * JAN
     */
    @Alias("JAN")
    @ApiModelProperty("JAN")
    private String jan;

    /**
     * 品番
     */
    @Alias("品番")
    @ApiModelProperty("品番")
    private Integer callCode;

    /**
     * 商品名
     */
    @Alias("商品名")
    @ApiModelProperty("商品名")
    private String itemName;

    /**
     * 賞味期限
     */
    @Alias("賞味期限")
    @ApiModelProperty("賞味期限")
    private Integer expTime;

    /**
     * 消費期限
     */
    @Alias("消費期限")
    @ApiModelProperty("消費期限")
    private String expirationTime;

    /**
     * 店舗コード
     */
    @Alias("店舗コード")
    @ApiModelProperty("店舗コード")
    private Integer storeId;

    /**
     * 生産計画トランの店舗コード
     */
    @ApiModelProperty("生産計画トランの店舗コード")
    private Integer proStoreId;

    /**
     * 店舗名
     */
    @Alias("店舗名")
    @ApiModelProperty("店舗名")
    private String storeName;

    /**
     * 店舗别名
     */
    private String storeShortName;

    /**
     * 検食数
     */
    @ApiModelProperty("検食数")
    private Integer tasteQy;

    /**
     * 指示数
     */
    @Alias("指示数")
    @ApiModelProperty("指示数")
    private Integer instructionNum;

    /**
     * 標準内容量
     */
    @ApiModelProperty("標準内容量")
    private Integer volume;

    /**
     * 標準内容量単位CD
     */
    @ApiModelProperty("標準内容量単位CD")
    private Integer volumeTypeId;

    /**
     * ユニット名
     */
    @ApiModelProperty("ユニット名")
    private String unitName;

    /**
     * コンテナ入数
     */
    @ApiModelProperty("コンテナ入数")
    private Integer containerUnit;

    /**
     * 基準店売単価
     */
    @ApiModelProperty("基準店売単価")
    private BigDecimal price;

    /**
     * 調達グループコード
     */
    @ApiModelProperty("調達グループコード")
    private Integer purchaseGroupCd;

    /**
     * 調達グループ名
     */
    @ApiModelProperty("調達グループ名")
    private String purchaseGroupName;
}
