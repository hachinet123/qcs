package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 在庫_日次棚卸
 * </p>
 *
 * @author 10253955
 * @since 2023-12-22 9:27
 */
@Data
@ApiModel("DailyInventorySearchBo")
public class DailyInventorySearchBo {

    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    /**
     * 倉庫
     */
    @ApiModelProperty(value = "倉庫" )
    private Integer[] warehouseId;

    @ApiModelProperty(value = "ライン", required = true)
    @NotNull(message = SysConstantInfo.LINE_NOT_EMPTY)
    private Integer[] lineId;

    /**
     * ゼロ在庫含む
     */
    @ApiModelProperty(value = "ゼロ在庫含む", required = true)
    private Integer zeroInventory;
}
