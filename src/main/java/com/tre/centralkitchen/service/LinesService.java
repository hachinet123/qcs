package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.po.Lines;
import com.tre.centralkitchen.domain.vo.system.CenterdlvstoreMasterVo;

import java.util.List;

public interface LinesService {

    List<Lines> selectList();

}
