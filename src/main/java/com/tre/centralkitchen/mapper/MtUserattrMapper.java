package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtUserattr;
import com.tre.centralkitchen.domain.vo.system.MtUserattrVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtUserattrMapper extends BaseMapperPlus<MtUserattrMapper, MtUserattr, MtUserattrVo> {
}
