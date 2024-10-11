package com.tre.centralkitchen.domain.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MailListVo {
    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * 便番号
     */
    @ApiModelProperty("便番号")
    private Integer mailNo;

    /**
     * 便
     */
    @ApiModelProperty("便")
    private String mailName = "便";

    /**
     * リードタイム
     */
    @ApiModelProperty("リードタイム")
    private Integer leadtime;

    /**
     * 選択グループ
     */
    @ApiModelProperty("選択グループ")
    private String selGroup;

    /**
     * 生産実績自動確定時刻
     */
    @ApiModelProperty("生産実績自動確定時刻")
    private String producedconfirmTime;

    /**
     * プリンターIPアドレス
     */
    @ApiModelProperty("プリンターIPアドレス")
    private String printIp;

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

    /**
     * 表示順
     */
    @ApiModelProperty("表示順")
    private Integer seq;
}