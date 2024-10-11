package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * システム設定
 * </p>
 *
 * @author JDev
 * @since 2022-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "centralkitchen.mt_sysparam")
public class MtSysparam extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * システムID
     */
    private Integer systemId;

    /**
     * パラメーターID
     */
    private Integer paramId;

    /**
     * パラメーター値1
     */
    private String paramVal1;

    /**
     * パラメーター値2
     */
    private String paramVal2;

    /**
     * パラメーター値3
     */
    private String paramVal3;

    /**
     * パラメーター値4
     */
    private String paramVal4;

    /**
     * パラメーター値5
     */
    private String paramVal5;

    /**
     * 備考
     */
    private String memo;
}
