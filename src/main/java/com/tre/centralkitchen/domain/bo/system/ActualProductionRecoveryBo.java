package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("ActualProductionRecoveryBo")
public class ActualProductionRecoveryBo extends BaseEntityBo {
    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    private Integer centerId;

    /**
     * 便番号
     */
    @ApiModelProperty(value = "便番号", required = true)
    private Integer mailNo;

    public boolean isHoldingOrConfirmed(List<ActualProductionRecoveryBo> checkList) {
        List<ActualProductionRecoveryBo> centerCollect = checkList.stream().filter(a -> Objects.equals(a.centerId, this.centerId)).collect(Collectors.toList());
        if (!centerCollect.isEmpty()) {
            List<Integer> binArray = centerCollect.stream().map(ActualProductionRecoveryBo::getMailNo).collect(Collectors.toList());
            return !binArray.contains(this.mailNo);
        } else {
            return true;
        }
    }
}
