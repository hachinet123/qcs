package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MtUserattrVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ログインコード(社員番号など)
     */
    @ApiModelProperty("ログインコード(社員番号など)")
    private String code;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * ラインID
     */
    @ApiModelProperty("ラインID")
    private Integer lineId;

    /**
     * フリー項目1
     */
    @ApiModelProperty("フリー項目1")
    private Integer freecolumn1;

    /**
     * フリー項目2
     */
    @ApiModelProperty("フリー項目2")
    private Integer freecolumn2;

    /**
     * フリー項目3
     */
    @ApiModelProperty("フリー項目3")
    private Integer freecolumn3;

    /**
     * フリー項目4
     */
    @ApiModelProperty("フリー項目4")
    private Integer freecolumn4;

    /**
     * フリー項目5
     */
    @ApiModelProperty("フリー項目5")
    private Integer freecolumn5;
}
