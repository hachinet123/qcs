package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntityVo;
import lombok.Data;

@Data
public class WarehouseItemVo extends BaseEntityVo {

    private Integer centerId;

    private Integer warehouseId;

    private String itemId;

    private String itemName;

    private Integer seq;

    private String memo;
}
