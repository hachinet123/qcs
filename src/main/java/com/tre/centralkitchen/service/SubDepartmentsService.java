package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.vo.system.SubDepartmentsVo;

import java.util.List;

public interface SubDepartmentsService {

    List<SubDepartmentsVo> selectList(Integer departmentId);

}
