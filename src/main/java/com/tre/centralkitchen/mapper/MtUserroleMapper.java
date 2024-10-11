package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtUserrole;
import com.tre.centralkitchen.domain.vo.system.MtUserroleVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtUserroleMapper extends BaseMapperPlus<MtUserroleMapper, MtUserrole, MtUserroleVo> {
}
