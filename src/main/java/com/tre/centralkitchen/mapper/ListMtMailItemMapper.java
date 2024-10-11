package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品別便
 */
@Mapper
@DS("postgres")
public interface ListMtMailItemMapper extends BaseMapperPlus {
    List<String> selectBasics();

    List<String> selectCases();

}
