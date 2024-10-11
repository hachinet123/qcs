package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import lombok.Data;

import java.util.List;

@Data
public class MtCenterdlvstoreBo extends BaseEntityBo {

    private Integer centerId;

    private List<Integer> lineIds;

    private Integer fDel;
}
