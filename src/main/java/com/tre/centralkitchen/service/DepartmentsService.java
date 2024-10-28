package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.po.Lines;
import com.tre.centralkitchen.domain.vo.system.DepartmentsVo;

import java.util.List;

public interface DepartmentsService {

    List<DepartmentsVo> selectList(Integer lineId);

}
