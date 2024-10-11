package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.service.AuthorityService;
import com.tre.centralkitchen.service.IUserInfoService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    private final TokenTakeApart tokenTakeApart;
    private final IUserInfoService iUserInfoService;

    @Override
    public UserInfoVo getUserInfoByTokenID() {
        String userId = tokenTakeApart.takeDecryptedUserId();
        return iUserInfoService.getUserInfo(userId);
    }
}
