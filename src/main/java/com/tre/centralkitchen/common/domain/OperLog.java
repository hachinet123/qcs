package com.tre.centralkitchen.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class OperLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long operId;

    private String title;

    private Integer businessType;

    private String method;

    private String requestMethod;

    private Integer operatorType;

    private String operUserId;

    private String operUrl;

    private String operIp;

    private String operLocation;

    private String operParam;

    private String jsonResult;

    private Integer status;

    private String errorMsg;

    private Date operTime;

}
