package com.tre.centralkitchen.domain.vo.common;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2024-01-05 14:45
 */
@Data
public class StoreShortNameVo {

    /**
     * 店舗ID
     */
    private Integer storeId;

    /**
     * 帳票表記
     */
    private String storeShortName;

    private Integer isClosed;

    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * 便番号
     */
    private Integer mailNo;

    /**
     * ラインID
     */
    private Integer lineId;
}
