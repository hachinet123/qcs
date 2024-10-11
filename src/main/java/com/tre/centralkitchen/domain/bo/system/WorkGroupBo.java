package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkGroupBo extends BaseEntityBo {
    /**
     * センターID
     */
    @ApiModelProperty(value = "センターID", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    /**
     * ラインID
     */
    @ApiModelProperty(value = "ラインID", required = true)
    @NotBlank(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;
    /**
     * 大分類
     */
    @ApiModelProperty(value = "大分類")
    private Integer groupBig;
    /**
     * 大分類名
     */
    @ApiModelProperty(value = "大分類名")
    @Size(max = 20, message = SysConstantInfo.GROUP_BIG_NAME_MAX)
    private String groupBigName;
    /**
     * 小分類
     */
    @ApiModelProperty(value = "小分類")
    private Integer groupSmall;
    /**
     * 小分類名
     */
    @ApiModelProperty(value = "小分類名")
    @Size(max = 20, message = SysConstantInfo.GROUP_SMALL_NAME_MAX)
    private String groupSmallName;
}
