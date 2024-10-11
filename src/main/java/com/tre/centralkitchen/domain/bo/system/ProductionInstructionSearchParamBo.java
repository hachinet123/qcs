package com.tre.centralkitchen.domain.bo.system;

import cn.hutool.core.annotation.Alias;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 10225441
 * @version 10116842
 */
@Data
@ApiModel(value = "生産指示書")
public class ProductionInstructionSearchParamBo extends BaseEntityBo {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "センター", example = "32", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    @ApiModelProperty(value = "便", example = "1,2,3")
    @NotBlank(message = SysConstantInfo.MAIL_NO_NOT_EMPTY)
    private String mailNo;
    @ApiModelProperty(value = "ライン", example = "1,2,3")
    @NotBlank(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;
    @ApiModelProperty(value = "作業グループ", example = "1,2,3")
    @NotBlank(message = SysConstantInfo.WORKGROUP_NOT_EMPTY)
    private String workGroupId;
    @ApiModelProperty(value = "自動印刷部数", example = "10")
    @NotNull(message = SysConstantInfo.AUTOPRINT_NUM_NOT_EMPTY)
    private Short autoPrintNum;
}
