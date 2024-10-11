package com.tre.centralkitchen.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BacteriaCheckResultVo {

    /***
     *管理番号
     */
    private Integer id;
    /***
     *進捗
     */
    private Integer checkStatTypeId;

    private String checkStatTypeName;
    /***
     *タイトル
     */
    private String title;
    /***
     *セントラルキッチン
     */
    private String center;


    private String othercenter;
    /***
     *依頼日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date reqDate;
    /***
     *依頼者ID
     */
    private Integer reqUserId;
    /***
     *依頼者
     */
    private String reqUserName;
    /***
     *検査結果コメント
     */
    private String comment;
    /***
     *
     */
    private List<BacteriaCheckItemResultVo> bacteriaCheckItemResultVos;


}
