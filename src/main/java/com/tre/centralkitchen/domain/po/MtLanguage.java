package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("qcs.mt_language")
public class MtLanguage {

    /**
     * 言語ID
     */
    @TableField("id")
    private Integer id;

    /**
     * 言語名
     */
    @TableField("name")
    private String name;

    /**
     * 得意先名
     */
    @TableField("言語名 (英語)")
    private String name_e;

}
