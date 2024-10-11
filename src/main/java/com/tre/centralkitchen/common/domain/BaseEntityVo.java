package com.tre.centralkitchen.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.annotation.UserId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基本クラス
 */

@Data
@NoArgsConstructor
public class BaseEntityVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 削除flag
     */
    @ApiModelProperty(value = "削除flag")
    private Integer fDel = 0;

    /**
     * 作成ユーザー
     */
    @ApiModelProperty(value = "作成ユーザー")
    @UserId(userName = "insUserName")
    private Long insUserId;

    /**
     * 更新ユーザー
     */
    @ApiModelProperty(value = "更新ユーザー")
    @UserId(userName = "updUserName")
    private Long updUserId;

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
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date insTime;

    /**
     * 更新日付
     */
    @ApiModelProperty(value = "更新日付")
    @JsonFormat(pattern = "yyyy/MM/dd", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date updDate;

    /**
     * 更新時間
     */
    @ApiModelProperty(value = "更新時間")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone = "JST")
    private Date updTime;

}
