package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.MailBasicSettingBo;
import com.tre.centralkitchen.domain.vo.system.MailBasicSettingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface MailBasicSettingMapper extends BaseMapperPlus {

    List<MailBasicSettingVo> getBasicSettingList(@Param("param") MailBasicSettingBo bo);

    MailBasicSettingVo getMailNo(@Param("param") MailBasicSettingBo bo);

}
