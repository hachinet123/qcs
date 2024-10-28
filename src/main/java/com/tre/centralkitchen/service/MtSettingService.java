package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.bo.system.MtSettingBo;
import com.tre.centralkitchen.domain.vo.system.MtSectionVo;
import com.tre.centralkitchen.domain.vo.system.MtSettingVo;

public interface MtSettingService {

    void insertData(MtSettingBo bo);

    MtSettingVo findOne(Integer userId);

}
