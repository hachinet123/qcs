package com.tre.centralkitchen.domain.vo.system;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2022-11-17
 */
@Data
@ApiModel("MtLcu7controlVo")
@ExcelIgnoreUnannotated
public class MtLcu7controlVo {

    private static final long serialVersionUID = 1L;

    /**
     * オペレーションタイプ
     */
    @ExcelProperty(value = "オペレーションタイプ")
    @ApiModelProperty("オペレーションタイプ")
    private Integer opType;

    /**
     * 表示順
     */
    @ExcelProperty(value = "表示順")
    @ApiModelProperty("表示順")
    private Integer seq;

    /**
     * 送信ファイル名
     */
    @ExcelProperty(value = "送信ファイル名")
    @ApiModelProperty("送信ファイル名")
    private String sendFilename;

    /**
     * 受信ファイル名
     */
    @ExcelProperty(value = "受信ファイル名")
    @ApiModelProperty("受信ファイル名")
    private String recvFilename;

    /**
     * 表示文字
     */
    @ExcelProperty(value = "表示文字")
    @ApiModelProperty("表示文字")
    private String display;

    /**
     * テーブル名
     */
    @ExcelProperty(value = "テーブル名")
    @ApiModelProperty("テーブル名")
    private String tableName;

    /**
     * クリアシーケンス
     */
    @ExcelProperty(value = "クリアシーケンス")
    @ApiModelProperty("クリアシーケンス")
    private Integer clearSeq;

}
