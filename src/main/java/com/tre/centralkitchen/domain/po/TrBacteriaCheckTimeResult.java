package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.tr_bacteriacheck_time_result")
public class TrBacteriaCheckTimeResult extends BaseEntity {

    /***
     * 管理番号
     */
    @TableField("id")
    private Integer id;
    /***
     * 明細番号
     */
    @TableField("seq")
    private Integer seq;
    /***
     * 検査時間区分ID
     */
    @TableField("checktime_typeid")
    private Integer checkTimeTypeId;
    /***
     * 一般細菌数
     */
    @TableField("qy")
    private String qy;





}
