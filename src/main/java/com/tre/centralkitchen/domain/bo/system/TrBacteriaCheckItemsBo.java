package com.tre.centralkitchen.domain.bo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TrBacteriaCheckItemsBo extends BaseEntityBo {

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
    @NotNull(groups ={save.class} ,message = "製造日時空白")
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private String produceDate;

    private String productDate;

    private String productTime;

    /**
     *提出パック数
     */
    @NotNull(groups ={save.class} ,message = "提出パック数空白")
    private Integer qy;
    /**
     *保存温度
     */
    @NotNull(groups ={save.class} ,message = "保存温度空白")
    private Integer tempZoneTypeId;
    /**
     *検査目的
     */
    @NotNull(groups ={save.class} ,message = "検査目的空白")
    private Integer checkObjTypeId;
    /**
     *製品群
     */
    private Integer fheated;
    /**
     *商品名
     */
    @NotNull(groups ={save.class} ,message = "商品名空白")
    private String  itemName;
    /**
     *進捗シート
     */
    private Integer fprogress;
    /**
     *検査時間(その他)
     */
    private String otherCheckDate;
    /**
     *検査時間
     */
    private List<Integer> checkTimeTypeId;


    public @interface save{}
}

