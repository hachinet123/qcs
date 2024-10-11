package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtUserlogin;
import com.tre.centralkitchen.domain.vo.system.MtUserLoginVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtUserloginMapper extends BaseMapperPlus<MtUserloginMapper, MtUserlogin, MtUserLoginVo> {
}
