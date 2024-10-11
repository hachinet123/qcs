package com.tre.centralkitchen.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Data
public class BacteriaCheckItemVo {


    /**
     *No
     */
    private Integer seq;
    /**
     *検体名
     */
    private String checkItem;
    /**
     *製造日時
     */

    private String productDate;

    private String productTime;


    /**
     *提出パック数
     */
    private Integer qy;
    /**
     *保存温度
     */
    private Integer tempZoneTypeId;
    /**
     *検査目的
     */
    private Integer checkObjTypeId;
    /**
     *製品群
     */
    private Integer fHeated;
    /**
     *商品名
     */
    private String  itemName;
    /**
     *進捗シート
     */
    private Integer fProgress;
    /**
     *検査時間(その他)
     */
    private String otherCheckDate;
    /**
     *検査時間
     */
    private List<Integer> checkTimeTypeId;
}
