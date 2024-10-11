package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtMail;
import com.tre.centralkitchen.domain.vo.common.MailListVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtMailMapper extends BaseMapperPlus<MtMailMapper, MtMail, MailListVo> {
}
