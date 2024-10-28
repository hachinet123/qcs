package com.tre.centralkitchen.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DepartmentsVo {

    @ApiModelProperty("departmentcd")
    private Integer departmentCd;

    @ApiModelProperty("departmentname")
    private String departmentName;

}
