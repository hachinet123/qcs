package com.tre.centralkitchen.domain.bo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class TrBacteriaCheckTimeResultBo extends BaseEntityBo {
    /**
     *番号
     */
    private Integer id;
    /**
     *依頼タイトル
     */
    @NotNull(groups ={save.class} ,message = "依頼タイトル空白")
    private String title;
    /**
     *進捗
     */
    private Integer checkStatTypeId;
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
    /**
     *備考
     */
    private String memo;


    private List<TrBacteriaCheckItemsBo> trBacteriaCheckItems;

    public @interface save{}

}
