package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface SysOperLogMapper extends BaseMapperPlus<SysOperLogMapper, SysOperLog, SysOperLog> {

}
