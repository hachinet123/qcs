package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;

import javax.naming.ldap.PagedResultsControl;

@Data
public class BacteriaCheckResultPDFVo {

    private String checkItem;

    private String  checkDate;

    private String tempZone;

    private String qy;

    private String ecolies;

    private String fStaphylococcus;

    private String memo;

}
