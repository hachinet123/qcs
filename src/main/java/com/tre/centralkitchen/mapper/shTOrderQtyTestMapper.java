package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.shTOrderQtyTest;
import com.tre.centralkitchen.domain.vo.system.shTOrderQtyTestVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("sqlserver")
public interface shTOrderQtyTestMapper extends BaseMapperPlus<shTOrderQtyTestMapper, shTOrderQtyTest, shTOrderQtyTestVo> {
}