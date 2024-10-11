package com.tre.centralkitchen.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class BacteriaCheckVo {

    /**
     *番号
     */
    private Integer id;
    /**
     *依頼タイトル
     */
    private String title;
    /**
     *進捗
     */
    private Integer checkStatTypeId;

    private String checkStatTypeName;
    /**
     *セントラルキッチン
     */
    private Integer centerId;
    /**
     *セントラルキッチンその他
     */
    private String otherCenter;
    /**
     *依頼日
     */
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate reqDate;
    /**
     *依頼者の社員コード
     */
    private Integer reqUserId;

    private String reqUserName;
    /**
     *備考
     */
    private String memo;


    private List<BacteriaCheckItemVo> trBacteriaCheckItems;
}
