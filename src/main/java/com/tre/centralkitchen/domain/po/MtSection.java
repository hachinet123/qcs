package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qcs.mt_section")
public class MtSection {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableField("id")
    private Integer id;

    /**
     * プロセスセンターID
     */
    @TableField("center_id")
    private Integer centerId;

    /**
     * 係名
     */
    @TableField("name")
    private String name;

}
