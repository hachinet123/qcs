package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.vo.system.SubDepartmentsVo;
import com.tre.centralkitchen.mapper.SubDepartmentsMapper;
import com.tre.centralkitchen.service.SubDepartmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubDepartmentsServicesImpl implements SubDepartmentsService {

    private final SubDepartmentsMapper mapper;

    @Override
    public List<SubDepartmentsVo> selectList(Integer departmentId) {
        return mapper.selectList(departmentId);
    }
}
