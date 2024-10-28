package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtClient;
import com.tre.centralkitchen.domain.vo.system.MtClientVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@DS("postgres")
public interface MtClientMapper extends BaseMapperPlus<MtClientMapper, MtClient, MtClientVo> {

    List<MtClient> selectList();

}
