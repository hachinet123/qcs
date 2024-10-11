package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.WkOdrTransbill;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface WkOdrTransbillMapper extends BaseMapperPlus<WkOdrTransbillMapper, WkOdrTransbill, WkOdrTransbill> {

}
