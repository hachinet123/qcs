package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.bo.system.MtSettingBo;
import com.tre.centralkitchen.domain.po.MtSetting;
import com.tre.centralkitchen.domain.vo.system.MtSettingVo;
import com.tre.centralkitchen.mapper.MtSettingMapper;
import com.tre.centralkitchen.service.MtSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MtSettingServiceImpl implements MtSettingService {

    private final MtSettingMapper mapper;

    @Override
    @Transactional
    public void insertData(MtSettingBo bo) {
        MtSetting mtSetting = new MtSetting();
        mtSetting.setUserId(bo.getUserId());
        mtSetting.setCenterId(bo.getCenterId());
        mtSetting.setSectionId(bo.getSectionId());
        mtSetting.setLanguageId(bo.getLanguageId());
        mtSetting.setFreeColumn1(0);
        mtSetting.setFreeColumn2(0);
        mtSetting.setFreeColumn3(0);
        mtSetting.setFreeColumn4(0);
        mtSetting.setFreeColumn5(0);
        mtSetting.setFreeColumn6(0);
        mtSetting.setFreeColumn7(0);
        mtSetting.setFreeColumn8(0);
        mtSetting.setFreeColumn9(0);
        mapper.insert(mtSetting);
    }

    @Override
    public MtSettingVo findOne(Integer userId) {
        return mapper.selectVoById(userId);
    }
}
