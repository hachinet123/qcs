package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.AppendedSearchBo;
import com.tre.centralkitchen.domain.vo.system.AppendedUpdateVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface AppendedMapper extends BaseMapperPlus {
    Page<AppendedUpdateVo> queryAppended(Page<AppendedUpdateVo> page, @Param("param") AppendedSearchBo bo);

    List<AppendedUpdateVo> queryAppended(@Param("param") AppendedSearchBo bo);
}
