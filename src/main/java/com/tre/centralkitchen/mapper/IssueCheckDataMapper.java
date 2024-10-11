package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.IssueCheckDataBo;
import com.tre.centralkitchen.domain.vo.system.IssueCheckDataVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface IssueCheckDataMapper extends BaseMapperPlus {
    Page<IssueCheckDataVo> queryList(@Param("param") IssueCheckDataBo bo, Page<Object> build);

    List<IssueCheckDataVo> queryList(@Param("param") IssueCheckDataBo bo);
}
