package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DS("postgres")
public interface UserInfoMapper {
    UserInfoVo selectByUserId(@Param("userId") String userId);
}
