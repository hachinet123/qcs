package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.vo.system.DepartmentsVo;
import com.tre.centralkitchen.mapper.DepartmentsMapper;
import com.tre.centralkitchen.service.DepartmentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentsServicesImpl implements DepartmentsService {

    private final DepartmentsMapper mapper;

    @Override
    public List<DepartmentsVo> selectList(Integer lineId) {
        return mapper.selectList(lineId);
    }
}
