package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtPlace;
import com.tre.centralkitchen.domain.vo.common.MtPlaceVo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
@DS("postgres")
public interface MtPlaceMapper extends BaseMapperPlus<MtPlaceMapper, MtPlace, MtPlaceVo> {
}
