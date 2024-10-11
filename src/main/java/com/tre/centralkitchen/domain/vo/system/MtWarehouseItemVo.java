package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import lombok.Data;

@Data
public class MtWarehouseItemVo extends BaseEntityVo {

    @Alias("センター")
    private String centerName;


    private Integer centerId;

    @Alias("倉庫")
    private String warehouseName;


    private Integer warehouseId;

    @Alias("JAN")
    private String itemId;

    @Alias("原材料名")
    private String itemName;

    @Alias("表示順")
    private String seq;

    @Alias("備考")
    private String memo;
}
