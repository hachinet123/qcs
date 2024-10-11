package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class MtInstructAutoPrintVo extends BaseEntityVo {
    private static final long serialVersionUID = 1L;
    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    @ApiModelProperty("プロセスセンター")
    private String centerName;
    /**
     * 作業グループID
     */
    @ApiModelProperty("作業グループID")
    private Short workGroupId;

    @ApiModelProperty("作業グループ名")
    private String workGroupName;
    /**
     * 印刷部数
     */
    @ApiModelProperty("印刷部数")
    private Short qy;

    /**
     * 区別
     */
    @ApiModelProperty("区別")
    private Integer autoPrintTypeId;
}