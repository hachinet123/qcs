package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtUserattrHistory;
import com.tre.centralkitchen.domain.vo.system.MtUserattrHistoryVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtUserattrHistoryMapper extends BaseMapperPlus<MtUserattrHistoryMapper, MtUserattrHistory, MtUserattrHistoryVo> {
}
