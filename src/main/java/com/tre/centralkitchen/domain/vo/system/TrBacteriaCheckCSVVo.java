package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = false)
public class

TrBacteriaCheckCSVVo extends BaseEntityVo {

    @Alias("管理番号")
    private Integer id;

    private Integer seq;


    @Alias("進捗")
    private  String checkstatType;

    @Alias("依頼タイトル")
    private String title;

    @Alias(" セントラルキッチン")
    private String branChnameAbbr;

    @Alias(" 依頼日")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate reqDate;

    @Alias("依頼者コード")
    private String reqUserId;

    @Alias("依頼者名")
    private String reqUserName;

    @Alias("検体名")
    private String checkItem;

    @Alias("生産日時")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate produceDate;

    @Alias("検査時間")
    private String checkDate;

    @Alias("提出パック数")
    private Integer qy;

    @Alias("保存温度")
    private String  tempZoneName;

    @Alias("検査目的")
    private String   checkObjName;

    @Alias("製品群")
    private String fHeated;

    @Alias(" 商品名")
    private String itemName;

    @Alias("商談進捗シート記載")
    private String fProgress;






}
