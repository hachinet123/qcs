package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class BacteriaCheckItemResultVo {


    /***
     *明細No
     */
    private Integer seq;
    /***
     *検体名
     */
    private String checkItem;
    /***
     *保存温度
     */
    private String tempZone;


    /***
     *検査時間
     */
    private Map<Integer,String> checkTime;

    /***
     *大腸菌群
     */
    private String ecolies;
    /***
     *大腸菌
     */
    private String ecoli;
    /***
     *黄色ブドウ球菌フラグ
     */
    private Integer   fStaphylococcus;
    /***
     *合格フラグ
     */
    private Integer fPassed;
    /***
     *備考
     */
    private String memo;
}
