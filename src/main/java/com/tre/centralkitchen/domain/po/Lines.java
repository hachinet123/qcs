package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.lines")
public class Lines {

    /**
     * ラインコード
     */
    @TableField("linecd")
    private Integer linecd;

    /**
     * ライン名
     */
    @TableField("linename")
    private String lineName;

}
