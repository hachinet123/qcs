package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckTimeResultBo;
import com.tre.centralkitchen.domain.vo.system.BacteriaCheckVo;
import com.tre.centralkitchen.domain.vo.system.TrBacteriaCheckVo;

import javax.servlet.http.HttpServletResponse;

public interface TrBacteriaCheckService {

    TableDataInfo<TrBacteriaCheckVo> search(TrBacteriaCheckBo trBacteriaCheckBo, PageQuery pageQuery);

    void downloadCSV(TrBacteriaCheckBo bo, HttpServletResponse response);

    Integer save(TrBacteriaCheckTimeResultBo bo);

    Integer update(TrBacteriaCheckTimeResultBo bo);

    void delete(Integer id);

    BacteriaCheckVo bacteriaCheckItemSelect(Integer id);

    void bacteriaCheckItemResultImport(TrBacteriaCheckTimeResultBo bo);
}
