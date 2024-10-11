package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * ログインユーザーマスタ
 * </p>
 *
 * @author JDev
 * @since 2022-12-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.mt_userlogin")
public class MtUserlogin extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * ログインコード(社員番号など)
     */
    private String code;

    /**
     * 備考
     */
    private String memo;

}
