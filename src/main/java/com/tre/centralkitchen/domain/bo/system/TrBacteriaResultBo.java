package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import lombok.Data;

import java.util.List;

@Data
public class TrBacteriaResultBo extends BaseEntityBo {

    private Integer id;

    private Integer checkStatTypeId;

    private String title;

    private String comment;

    List<TrBacteriaItemResultBo> trBacteriaItemResultBo;

}
