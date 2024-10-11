package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TrMailConfirmHoldVo {
    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID")
    private String centerId;
    /**
     * プロセスセンターname
     */
    @ApiModelProperty(value = "プロセスセンターname")
    private String centerName;
    /**
     * 便
     */
    @ApiModelProperty(value = "便番号")
    private String mail;
    /**
     * 生産実績確定時刻
     */
    @ApiModelProperty(value = "生産実績確定時刻")
    private String time;
    /**
     * 備考
     */
    @ApiModelProperty(value = "備考")
    private String memo;
    /**
     * 保留開始時刻
     */
    @ApiModelProperty(value = "保留開始時刻")
    private Date startDate;
    /**
     * 保留解除時刻
     */
    @ApiModelProperty(value = "保留解除時刻")
    private Date endDate;
    private Integer flag;
    /**
     * 便id
     */
    @ApiModelProperty("便id")
    private Integer mailNo;
}
