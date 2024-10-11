package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TrMailConfirmHoldBo extends BaseEntityBo {
    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    /**
     * 便
     */
    @ApiModelProperty(value = "便番号")
    private String mailNo;
    private Integer num;

    private List<String> mailNoList;
}
