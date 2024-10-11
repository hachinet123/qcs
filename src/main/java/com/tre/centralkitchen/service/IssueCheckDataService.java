package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.IssueCheckDataBo;
import com.tre.centralkitchen.domain.vo.system.IssueCheckDataVo;

import javax.servlet.http.HttpServletResponse;

public interface IssueCheckDataService {
    TableDataInfo<IssueCheckDataVo> queryList(IssueCheckDataBo bo, PageQuery pageQuery);

    void downloadCSV(IssueCheckDataBo bo, HttpServletResponse response);

}
