package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntityVo;
import lombok.Data;

@Data
public class ItemCenterMaterSearchVo extends BaseEntityVo {

        private Integer centerId;

        private String itemId;

        private String itemName;

        private Integer safetyStockQy;
}
