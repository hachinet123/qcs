package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.UserMasterBo;
import com.tre.centralkitchen.domain.vo.system.UserMasterUpSearchVo;
import com.tre.centralkitchen.domain.vo.system.UserMasterVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface UserMasterMapper extends BaseMapperPlus {

    Page<UserMasterVo> getUserMasterList(@Param("param") UserMasterBo bo, Page<Object> build);

    List<UserMasterVo> getUserMasterList(@Param("param") UserMasterBo bo);

    UserMasterUpSearchVo getUserMasterUpSearch(@Param("param") UserMasterBo bo);

}
