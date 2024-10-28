package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.domain.vo.system.MtSectionVo;

import java.util.List;

public interface MtSectionService {
    List<MtSectionVo> managersSelect(Integer centerId);
}
