package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.WorkGroupBo;
import com.tre.centralkitchen.domain.vo.system.WorkGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface WorkGroupMapper extends BaseMapperPlus {

    Page<WorkGroupVo> getWorkGroupList(@Param("centerId") Integer centerId, @Param("list") List<String> centerLineIdBo, Page<Object> build);
    List<WorkGroupVo> getWorkGroupList(@Param("centerId") Integer centerId, @Param("list") List<String> centerLineIdBo);

    WorkGroupVo getUpWorkGroup(@Param("param") WorkGroupBo bo);
}
