package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.SubDepartments;
import com.tre.centralkitchen.domain.vo.system.DepartmentsVo;
import com.tre.centralkitchen.domain.vo.system.SubDepartmentsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface SubDepartmentsMapper extends BaseMapperPlus<SubDepartmentsMapper, SubDepartments, SubDepartmentsVo> {
    List<SubDepartmentsVo> selectList(@Param("departmentId") Integer departmentId);
}
