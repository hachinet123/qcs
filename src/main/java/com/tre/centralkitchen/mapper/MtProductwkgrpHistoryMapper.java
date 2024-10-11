package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtProductwkgrpHistory;
import com.tre.centralkitchen.domain.vo.system.MtProductwkgrpHistoryVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtProductwkgrpHistoryMapper extends BaseMapperPlus<MtProductwkgrpHistoryMapper, MtProductwkgrpHistory, MtProductwkgrpHistoryVo> {
}
