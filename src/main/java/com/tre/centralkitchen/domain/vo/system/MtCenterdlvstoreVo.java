package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

@Data
public class MtCenterdlvstoreVo {

    /***
     *プロセスセンターID
     */
    private Integer centerId;
    /***
     *センター
     */
    @Alias("センター")
    private String centerName;
    /***
     *ラインID
     */
    private Integer lineId;
    /***
     *ライン
     */
    @Alias("ライン")
    private String lineName;
    /***
     *店舗コード
     */
    private Integer storeId;
    /***
     *店舗名
     */
    @Alias("店舗名")
    private String storeName;
    /***
     *帳票表示
     */
    @Alias("帳票表示")
    private String shortName;
    /***
     *表示順
     */
    @Alias("表示順")
    private Integer seq;
    /***
     *開店日
     */
    @Alias("開店日")
    private String  openedDate;
}
