package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.departments")
public class SubDepartments {

    @TableField("subdeptcd")
    private Integer subDeptCd;

    @TableField("subdeptname")
    private String subDeptName;

}
