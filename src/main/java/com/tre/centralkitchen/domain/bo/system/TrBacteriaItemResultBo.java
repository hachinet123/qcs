package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import lombok.Data;

import java.util.List;

@Data
public class TrBacteriaItemResultBo extends BaseEntityBo {

    private Integer id;

    private Integer seq;

    private String ecolies;

    private String ecoli;

    private Integer fStaphylococcus;

    private Integer fPassed;

    private String memo;

    private List<BacteriaTimesNumBo>  numBos;

}
