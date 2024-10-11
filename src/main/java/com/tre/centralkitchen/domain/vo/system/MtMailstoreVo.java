package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;

import java.io.Serializable;

@Data
public class MtMailstoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * 便番号
     */
    private Integer mailNo;

    /**
     * 店舗ID
     */
    private Integer storeId;

    /**
     * 備考
     */
    private String memo;

    /**
     * 削除フラグ
     */
    private Integer fDel;
}
