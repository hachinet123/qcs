package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ItemCenterMaterialVo extends BaseEntityVo {

    @Alias("プロセスセンターID")
    @ApiModelProperty(value = "プロセスセンターID")
    private Integer centerId;

    @Alias("センター")
    @ApiModelProperty(value = "センター")
    private String centerName;

    @Alias("原材料JAN")
    @ApiModelProperty(value = "原材料JAN")
    private String itemId;

    @Alias("原材料名")
    @ApiModelProperty(value = "原材料名")
    private String itemName;

    @Alias("安全在庫")
    @ApiModelProperty(value = "安全在庫")
    private Integer safetyStockQy;

}
