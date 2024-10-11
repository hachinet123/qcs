package com.tre.centralkitchen.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.WkDecideProductionResults;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface WkDecideProductionResultsMapper extends BaseMapperPlus<WkDecideProductionResultsMapper, WkDecideProductionResults, WkDecideProductionResults> {

}