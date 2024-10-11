package com.tre.centralkitchen.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("LCUモデル")
public class MtLcu7controlDto {

    /**
     * センターコード
     */
    @ApiModelProperty(value = "センターコード")
    private Integer centerCd;

    /**
     * 送信ファイル名
     */
    @ApiModelProperty(value = "送信ファイル名")
    private String sendFilename;

    /**
     * 受信ファイル名
     */
    @ApiModelProperty(value = "受信ファイル名")
    private String recvFilename;

    /**
     * 表示文字
     */
    @ApiModelProperty(value = "表示文字")
    private String display;

    /**
     * LCU7ip
     */
    @ApiModelProperty(value = "LCU7ip")
    private String ip;

    /**
     * LCU7user
     */
    @ApiModelProperty(value = "LCU7user")
    private String user;

    /**
     * LCU7password
     */
    @ApiModelProperty(value = "LCU7password")
    private String pwd;

    /**
     * LCU7path
     */
    @ApiModelProperty(value = "LCU7path")
    private String path;
}
