package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.mapper.UserInfoMapper;
import com.tre.centralkitchen.service.IUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfoVo getUserInfo(String userId) {
        return userInfoMapper.selectByUserId(userId);
    }

}
