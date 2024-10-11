package com.tre.centralkitchen.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MtEmployeesVo {
    /**
     * 従業員コード
     */
    @ApiModelProperty("従業員コード")
    private Integer employeeCode;
    /**
     * 従業員名称
     */
    @ApiModelProperty("従業員名称")
    private String employeeName;
    /**
     * 退職区分
     */
    @TableField("retirementdivision")
    private Integer retirementdivision;
}
