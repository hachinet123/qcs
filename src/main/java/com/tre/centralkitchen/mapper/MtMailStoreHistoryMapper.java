package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtMailStoreHistory;
import com.tre.centralkitchen.domain.vo.system.MtMailStoreHistoryVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtMailStoreHistoryMapper extends BaseMapperPlus<MtMailStoreHistoryMapper, MtMailStoreHistory, MtMailStoreHistoryVo> {
}
