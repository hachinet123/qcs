package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
public class GetActualProductionVo extends BaseEntityVo {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * プロセスセンター名
     */
    @Alias("センター")
    @ApiModelProperty("センター")
    private String centerName;

    /**
     * 便番号
     */
    @ApiModelProperty("便番号")
    private Integer mailNo;

    /**
     * 便
     */
    @Alias("便")
    @ApiModelProperty("便")
    private String mail;

    @Alias("納品予定日")
    @ApiModelProperty("納品予定日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dlvschedDate;

    /**
     * 生産実績取得日時
     */
    @Alias("最終実績取得日時")
    @ApiModelProperty("最終実績取得日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime producedImportDate;

    /**
     * 生産実績取得フラグ
     */
    @ApiModelProperty("生産実績取得フラグ")
    private Integer producedImportFlag;

    /**
     * 最終確定日時
     */
    @Alias("最終確定日時")
    @ApiModelProperty("最終確定日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime producedConfirmedDate;

    /**
     * 計量実績確定フラグ
     */
    @ApiModelProperty("計量実績確定フラグ")
    private Integer producedConfirmedFlag;

    /**
     * 備考
     */
    @Alias("備考")
    @ApiModelProperty("備考")
    private String memo;

}
