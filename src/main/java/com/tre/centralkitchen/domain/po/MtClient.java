package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qcs.mt_client")
public class MtClient {

    /**
     * 得意先ID
     */
    @TableField("id")
    private Integer id;

    /**
     * 得意先名
     */
    @TableField("name")
    private String name;

}
