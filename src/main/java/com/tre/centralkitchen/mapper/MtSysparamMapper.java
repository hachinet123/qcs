package com.tre.centralkitchen.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtSysparam;
import com.tre.centralkitchen.domain.vo.system.MtSysparamVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtSysparamMapper extends BaseMapperPlus<MtSysparamMapper, MtSysparam, MtSysparamVo> {

}
