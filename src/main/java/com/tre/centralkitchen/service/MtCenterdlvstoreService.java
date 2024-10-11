package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.CenterdlvstoreBo;
import com.tre.centralkitchen.domain.bo.system.MtCenterdlvstoreBo;
import com.tre.centralkitchen.domain.vo.system.CenterdlvstoreVo;
import com.tre.centralkitchen.domain.vo.system.MtCenterdlvstoreVo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;


public interface MtCenterdlvstoreService {

    TableDataInfo<MtCenterdlvstoreVo> search(MtCenterdlvstoreBo centerdlvstoreBo, PageQuery pageQuery);

    void downloadCSV(MtCenterdlvstoreBo centerdlvstoreBo, HttpServletResponse response);

    void save(CenterdlvstoreBo centerdlvstoreBo);

    CenterdlvstoreVo info(CenterdlvstoreBo centerdlvstoreBo);

    void update(CenterdlvstoreBo centerdlvstoreBo);

    void delete(CenterdlvstoreBo centerdlvstoreBo);
}
