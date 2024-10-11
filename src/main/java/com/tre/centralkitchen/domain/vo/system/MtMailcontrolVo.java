package com.tre.centralkitchen.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class MtMailcontrolVo extends BaseEntityVo {

    private static final long serialVersionUID = 1L;

    /**
     * 納品予定日
     */
    @ApiModelProperty("納品予定日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dlvschedDate;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * プロセスセンター名
     */
    private String centerName;

    /**
     * 便番号
     */
    @ApiModelProperty("便番号")
    private Integer mailNo;

    /**
     * 便
     */
    @ApiModelProperty("便")
    private String mail;

    /**
     * リードタイム
     */
    @ApiModelProperty("リードタイム")
    private Integer leadtime;

    /**
     * 生産商品確定日時
     */
    @ApiModelProperty("生産商品確定日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime produceplanConfirmedDate;

    /**
     * 生産指示日時
     */
    @ApiModelProperty("生産指示日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime produceInstDate;

    /**
     * 生産実績取得日時
     */
    @ApiModelProperty("生産実績取得日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime producedImportDate;

    /**
     * 計量実績確定日時
     */
    @ApiModelProperty("計量実績確定日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime producedConfirmedDate;

    /**
     * 定額実績確定日時
     */
    @ApiModelProperty("定額実績確定日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime throughConfirmedDate;

    /**
     * 出庫確定日時
     */
    @ApiModelProperty("出庫確定日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime outConfirmedDate;

    /**
     * 生産商品確定フラグ
     */
    @ApiModelProperty("生産商品確定フラグ")
    private Integer produceplanConfirmedFlag;

    /**
     * 生産指示フラグ
     */
    @ApiModelProperty("生産指示フラグ")
    private Integer produceInstFlag;

    /**
     * 生産実績取得フラグ
     */
    @ApiModelProperty("生産実績取得フラグ")
    private Integer producedImportFlag;

    /**
     * 計量実績確定フラグ
     */
    @ApiModelProperty("計量実績確定フラグ")
    private Integer producedConfirmedFlag;

    /**
     * 定額実績確定フラグ
     */
    @ApiModelProperty("定額実績確定フラグ")
    private Integer throughConfirmedFlag;

    /**
     * 出庫確定フラグ
     */
    @ApiModelProperty("出庫確定フラグ")
    private Integer outConfirmedFlag;

    /**
     * 保留フラグ
     */
    @ApiModelProperty("保留フラグ")
    private Integer holdFlag;

    /**
     * 説明
     */
    @ApiModelProperty("説明")
    private String discript;

    /**
     * 備考
     */
    @ApiModelProperty("備考")
    private String memo;
}
