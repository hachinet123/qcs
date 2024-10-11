package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.tr_bacteriacheck_item_result")
public class TrBacteriaCheckItemResult extends BaseEntity {

    /**
     *管理番号
     */
    @TableField("id")
    private Integer id;
    /**
     *明細番号
     */
    @TableField("seq")
    private Integer seq;
    /**
     *大腸菌群
     */
    @TableField("ecolies")
    private String ecolies;
    /**
     *大腸菌
     */
    @TableField("ecoli")
    private String ecoli;
    /**
     *黄色ブドウ球菌フラグ
     */
    @TableField("f_staphylococcus")
    private Integer fStaphylococcus;
    /**
     *合格フラグ
     */
    @TableField("f_passed")
    private Integer fPassed;
    /**
     *備考
     */
    @TableField("memo")
    private String memo;


}
