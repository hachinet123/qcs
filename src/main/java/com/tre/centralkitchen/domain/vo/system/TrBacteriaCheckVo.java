package com.tre.centralkitchen.domain.vo.system;


import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TrBacteriaCheckVo {
    /**
     *管理番号
     */
    private Integer id;

    private  String checkstatType;

    private String title;

    private String centerName;

    private Integer counts;


    /**
     *依頼日
     */
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate reqDate;

    private String reqUserName;

    @JsonFormat(pattern = "yyyy/MM/dd")
     private LocalDate checkDate;
    /**
     * 検査結果
     */
    private String checkResult;



}
