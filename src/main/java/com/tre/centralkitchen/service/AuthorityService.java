package com.tre.centralkitchen.service;


import com.tre.centralkitchen.domain.vo.system.UserInfoVo;

public interface AuthorityService {
    UserInfoVo getUserInfoByTokenID();
}
