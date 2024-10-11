package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtProductwkgrp;
import com.tre.centralkitchen.domain.vo.common.MtProductwkgrpVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtProductwkgrpMapper extends BaseMapperPlus<MtProductwkgrpMapper, MtProductwkgrp, MtProductwkgrpVo> {
}
