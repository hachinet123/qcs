package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.vo.system.MailListSearchVo;

import javax.servlet.http.HttpServletResponse;

public interface IMailGlanceService {

    TableDataInfo<MailListSearchVo> getMailList(Integer centerId, PageQuery pageQuery);

    void downloadCSV(Integer centerId, HttpServletResponse response);
}