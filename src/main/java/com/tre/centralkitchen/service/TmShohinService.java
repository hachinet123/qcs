package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.TmShohinBo;
import com.tre.centralkitchen.domain.vo.common.TmShohinVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @auther 10116842
 * @date 2023/12/12
 */
public interface TmShohinService {

    TableDataInfo<TmShohinVo> queryPage(TmShohinBo bo, PageQuery pageQuery);

    void downloadCSV(TmShohinBo bo, HttpServletResponse response);
}
