package com.tre.centralkitchen.domain.bo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("MtLcu7controlBo")
public class MtLcu7controlBo {

    /**
     * オペレーションタイプ
     */
    @ApiModelProperty(value = "オペレーションタイプ", required = true)
    private Integer opType;

    /**
     * 表示順
     */
    @ApiModelProperty(value = "表示順", required = true)
    private Integer seq;

    /**
     * 送信ファイル名
     */
    @ApiModelProperty(value = "送信ファイル名", required = true)
    private String sendFilename;

    /**
     * 受信ファイル名
     */
    @ApiModelProperty(value = "受信ファイル名", required = true)
    private String recvFilename;

    /**
     * 表示文字
     */
    @ApiModelProperty(value = "表示文字", required = true)
    private String display;

    /**
     * テーブル名
     */
    @ApiModelProperty(value = "テーブル名", required = true)
    private String tableName;

    /**
     * クリアシーケンス
     */
    @ApiModelProperty(value = "クリアシーケンス", required = true)
    private Integer clearSeq;
}
