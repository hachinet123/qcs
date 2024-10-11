package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 従業員基本マスタ
 * </p>
 *
 * @author JDev
 * @since 2023-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "centralkitchen.employees")
public class MtEmployees {

    private static final long serialVersionUID = 1L;
    /**
     * 従業員コード
     */
    @TableField("employeecode")
    private Integer employeeCode;

    /**
     * 従業員名称
     */
    @TableField("employeename")
    private String employeeName;
    /**
     * 退職区分
     */
    @TableField("retirementdivision")
    private Integer retirementdivision;
}
