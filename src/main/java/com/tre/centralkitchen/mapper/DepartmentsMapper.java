package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.Departments;
import com.tre.centralkitchen.domain.vo.system.DepartmentsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface DepartmentsMapper extends BaseMapperPlus<DepartmentsMapper, Departments, DepartmentsVo> {
    List<DepartmentsVo> selectList(@Param("lineId") Integer lineId);
}
