package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MailBasicSettingBo extends BaseEntityBo {
    /**
     * センター
     */
    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    /**
     * 店舗CD
     */
    @ApiModelProperty(value = "店舗CD", required = true)
    @NotNull(message = SysConstantInfo.STORE_NOT_EMPTY)
    private Integer storeId;

    /**
     * 便
     */
    @ApiModelProperty(value = "便", required = true)
    private Integer mailNo;
    /**
     * 備考
     */
    @ApiModelProperty(value = "備考", required = true)
    private String memo;
    @ApiModelProperty(value = "グループ化", required = true)
    private String selGroup;
}
