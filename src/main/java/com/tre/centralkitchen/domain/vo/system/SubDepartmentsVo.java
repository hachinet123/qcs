package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubDepartmentsVo {

    @ApiModelProperty("subdeptcd")
    private Integer subDeptCd;

    @ApiModelProperty("subdeptname")
    private String subDeptName;

}
