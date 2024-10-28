package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtSection;
import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.domain.vo.system.MtSectionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface MtSectionMapper extends BaseMapperPlus<MtSectionMapper, MtSection, MtEmployeesVo> {
    List<MtSectionVo> managersSelect(@Param("centerId") Integer centerId);
}
