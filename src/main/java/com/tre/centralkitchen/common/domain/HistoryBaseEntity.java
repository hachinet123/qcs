package com.tre.centralkitchen.common.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基本クラス
 */

@Data
public class HistoryBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * バックアップ日時
     */
    @ApiModelProperty(value = "バックアップ日時")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "JST")
    @TableField(fill = FieldFill.INSERT)
    private Date backupDate;

    /**
     * 削除flag
     */
    @ApiModelProperty(value = "削除flag")
    private Integer fDel;

    /**
     * 作成日付
     */
    @ApiModelProperty(value = "作成日付")
    @JsonFormat(pattern = "yyyy/MM/dd", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date insDate;

    /**
     * 作成時間
     */
    @ApiModelProperty(value = "作成時間")
    @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date insTime;

    /**
     * 作成ユーザー
     */
    @ApiModelProperty(value = "作成ユーザー")
    private Long insUserId;

    /**
     * 操作関数（作成）
     */
    @ApiModelProperty(value = "操作関数（作成）")
    private Long insFuncId;

    /**
     * 操作機能（作成）
     */
    @ApiModelProperty(value = "操作機能（作成）")
    private Long insOpeId;

    /**
     * 更新日付
     */
    @ApiModelProperty(value = "更新日付")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy/MM/dd", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date updDate;

    /**
     * 更新時間
     */
    @ApiModelProperty(value = "更新時間")
    @JsonFormat(pattern = "HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date updTime;

    /**
     * 更新ユーザー
     */
    @ApiModelProperty(value = "更新ユーザー")
    private Long updUserId;

    /**
     * 操作関数（更新）
     */
    @ApiModelProperty(value = "操作関数（更新）")
    private Long updFuncId;

    /**
     * 操作機能（更新）
     */
    @ApiModelProperty(value = "操作機能（更新）")
    private Long updOpeId;

}
