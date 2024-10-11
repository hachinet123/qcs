package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtUserloginHistory;
import com.tre.centralkitchen.domain.vo.system.MtUserloginHistoryVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtUserloginHistoryMapper extends BaseMapperPlus<MtUserloginHistoryMapper, MtUserloginHistory, MtUserloginHistoryVo> {
}
