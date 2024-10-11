package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.Branches;
import com.tre.centralkitchen.domain.vo.common.BranchListVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@DS("postgres")
public interface BranchesMapper extends BaseMapperPlus<BranchesMapper, Branches, BranchListVo> {
}
