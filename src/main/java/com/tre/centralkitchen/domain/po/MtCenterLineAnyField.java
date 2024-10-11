package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * センター別ライン別汎用マスタ
 * </p>
 *
 * @author JDev
 * @since 2023-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_center_line_anyfield")
public class MtCenterLineAnyField extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * パラメーターID/ 1:旧ProductsDisplay 計量器へ送る商品データの選択コメントコード２で使用  / 2:旧AdditionlabelSize 計量器へ送る添加物データのラベルサイズで使用
     */
    private Integer paramId;

    /**
     * グループCD
     */
    private Integer lineId;

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
