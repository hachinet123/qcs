package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtMenu;
import com.tre.centralkitchen.domain.vo.system.MtMenuVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface MtMenuMapper extends BaseMapperPlus<MtMenuMapper, MtMenu, MtMenuVo> {

}
