package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchasegroupbranchesVo {
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
