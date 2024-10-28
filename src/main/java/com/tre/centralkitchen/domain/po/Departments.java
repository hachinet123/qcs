package com.tre.centralkitchen.domain.po;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.departments")
public class Departments {


    @TableField("departmentcd")
    private Integer departmentCd;

    @TableField("departmentname")
    private String departmentName;

}
