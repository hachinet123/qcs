package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_place")
public class MtPlace extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 産地ID
     */
    private Integer id;

    /**
     * 産地名称
     */
    private String name;

    /**
     * 産地名称カナ
     */
    private String nameK;

    /**
     * 産地略称
     */
    private String nameS;

    /**
     * 表示順
     */
    private Integer seq;
}
