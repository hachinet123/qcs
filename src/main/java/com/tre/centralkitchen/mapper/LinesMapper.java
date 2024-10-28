package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.Lines;
import com.tre.centralkitchen.domain.vo.system.LinesVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@DS("postgres")
public interface LinesMapper extends BaseMapperPlus<LinesMapper, Lines, LinesVo> {
    List<Lines> selectList();
}
