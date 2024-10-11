package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2023-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.purchasegroupbranches")
public class Purchasegroupbranches implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 調達グループコード
     */
    private Integer purchasegroupcd;

    /**
     * 店舗コード
     */
    private Integer branchcd;

    /**
     * 登録者ID
     */
    private String author;

    /**
     * 更新者ID
     */
    private String maintainer;

    /**
     * 登録日時
     */
    private LocalDateTime registered;

    /**
     * 更新日時
     */
    private LocalDateTime modified;


}
