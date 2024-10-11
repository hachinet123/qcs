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
@TableName(value = "centralkitchen.mt_documentlink")
public class MtDocumentLink extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private Integer id;

    /**
     * タイトル
     */
    private String title;

    /**
     * リンク
     */
    private String link;


    /**
     * 備考
     */
    private String memo;
}
