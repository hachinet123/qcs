package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_productwkgrp")
public class MtProductwkgrp extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 作業グループID
     */
    private Integer id;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * ラインID
     */
    private Integer lineId;

    /**
     * 作業グループ名称
     */
    private String name;

    /**
     * 作業グループ名称カナ
     */
    private String nameK = "";

    /**
     * 作業グループ略称
     */
    private String nameS = "";

    /**
     * 作業グループ_親ID/0:第一階層の意(親なし)
     */
    private Integer pid;

    /**
     * 表示順
     */
    private Integer seq;

}
