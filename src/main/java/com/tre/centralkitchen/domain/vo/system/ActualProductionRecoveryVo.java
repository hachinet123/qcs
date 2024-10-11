package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
@ApiModel()
public class ActualProductionRecoveryVo implements Serializable {

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

    /**
     * 計量実績確定日時
     */
    @Alias("最終確定日時")
    @ApiModelProperty("計量実績確定日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime producedConfirmedDate;

    /**
     * 計量実績確定フラグ
     */
    @ApiModelProperty("計量実績確定フラグ")
    private Integer producedConfirmedFlag;

}
