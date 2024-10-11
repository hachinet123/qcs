package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.naming.ldap.PagedResultsControl;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class MtWarehouseItemBo extends BaseEntityBo {

    @ApiModelProperty(value = "センター"  , required = true  )
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    @ApiModelProperty(value = "倉庫" )
    private List<Integer> warehouseIds;

    @ApiModelProperty(value = "ライン", required = true  )
    @NotNull(message = SysConstantInfo.LINE_NOT_EMPTY)
    private List<Integer> lineIds;

    @ApiModelProperty(value = "原材料JAN" )
    private List<String> itemIds;

    @ApiModelProperty(value = "商品名" )
    private List<String> itemNames;


}
