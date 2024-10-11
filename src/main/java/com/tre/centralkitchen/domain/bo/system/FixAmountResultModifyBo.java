package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.FixAmountResultModifyConstants;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 10225441
 * @version 10116842
 */
@Data
@ApiModel(value = FixAmountResultModifyConstants.SWAGGER_TITLE)
public class FixAmountResultModifyBo extends BaseEntityBo {
    private String id;
    @ApiModelProperty(value = "センター", example = "32", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    @ApiModelProperty(value = "便", example = "1,2,3")
    private String mailNo;
    @ApiModelProperty(value = "ライン", example = "1,2,3")
    private String lineId;
    @ApiModelProperty(value = "品番", example = "4637")
    private Integer type;
    @ApiModelProperty(value = "JAN", example = "2514845000000")
    private String jan;
    @ApiModelProperty(value = "商品名", example = "白飯（小）")
    private String goodsName;
    @ApiModelProperty(value = "生産計画あり", example = "0")
    @NotNull(message = "生産計画あり入力してください")
    @Max(value = 1, message = "生産計画あり"+SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "生産計画あり"+SysConstantInfo.NUMERICAL_LIMIT)
    private Integer hasProductPlan;
    @ApiModelProperty(value = "生産計画なし", example = "0")
    @NotNull(message = "生産計画なし入力してください")
    @Max(value = 1, message = "生産計画なし"+SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "生産計画なし"+SysConstantInfo.NUMERICAL_LIMIT)
    private Integer hasNoProductPlan;
    @ApiModelProperty(value = "修正済み含む", example = "0")
    @NotNull(message = "修正済含む入力してください")
    @Max(value = 1, message = "修正済み含む"+SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "修正済み含む"+SysConstantInfo.NUMERICAL_LIMIT)
    private Integer hasAlreadyUpdate;

    /* The following fields need to be converted because their data types are inconsistent with those stored in the database. */

    @ApiModelProperty(hidden = true)
    private List<Integer> lineIdList;
    @ApiModelProperty(hidden = true)
    private List<Short> mailIdList;
}
