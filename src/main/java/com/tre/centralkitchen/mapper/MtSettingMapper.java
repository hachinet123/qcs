package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtSetting;
import com.tre.centralkitchen.domain.vo.system.MtSettingVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtSettingMapper extends BaseMapperPlus<MtSettingMapper, MtSetting, MtSettingVo> {
}
