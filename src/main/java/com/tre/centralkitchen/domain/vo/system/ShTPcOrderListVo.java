package com.tre.centralkitchen.domain.vo.system;

import lombok.Data;

import java.util.Date;

@Data
public class ShTPcOrderListVo {
    private Integer branchCd;
    private String itemId;
    private Integer mailNo;
    private Integer listNo;
    private Integer level1;
    private Integer rowNo;
    private Date startDate;
    private Date endDate;
    private Integer updateUser;
    private Date updateDate;
    private Integer pcCode;
}
