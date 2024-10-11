package com.tre.centralkitchen.domain.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MtProductwkgrpVo {
    /**
     * 作業グループID
     */
    @ApiModelProperty("作業グループID")
    private Integer id;

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
     * 作業グループ名称
     */
    @ApiModelProperty("作業グループ名称")
    private String name;

    /**
     * 作業グループ名称カナ
     */
    @ApiModelProperty("作業グループ名称カナ")
    private String nameK;

    /**
     * 作業グループ略称
     */
    @ApiModelProperty("作業グループ略称")
    private String nameS;

    /**
     * 作業グループ_親ID/0:第一階層の意(親なし)
     */
    @ApiModelProperty("作業グループ_親ID/0:第一階層の意(親なし)")
    private Integer pid;

    /**
     * 表示順
     */
    @ApiModelProperty("表示順")
    private Integer seq;

    /**
     * 削除フラグ
     */
    @ApiModelProperty("削除フラグ")
    private Integer fDel;
}
