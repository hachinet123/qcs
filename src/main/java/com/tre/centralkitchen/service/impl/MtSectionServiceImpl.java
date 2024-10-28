package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.domain.vo.system.MtSectionVo;
import com.tre.centralkitchen.mapper.MtCenterdlvstoreMapper;
import com.tre.centralkitchen.mapper.MtSectionMapper;
import com.tre.centralkitchen.service.MtSectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MtSectionServiceImpl implements MtSectionService {

    private final MtSectionMapper mapper;

    @Override
    public List<MtSectionVo> managersSelect(Integer centerId) {
        return mapper.managersSelect(centerId);
    }

}
