package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtUserroleHistory;
import com.tre.centralkitchen.domain.vo.system.MtUserroleHistoryVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtUserroleHistoryMapper extends BaseMapperPlus<MtUserroleHistoryMapper, MtUserroleHistory, MtUserroleHistoryVo> {
}
