package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.vo.system.MtMenuVo;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;

import java.util.List;

public interface IMtMenuService {

    List<MtMenuVo> getMenuList( UserInfoVo userInfoVo);
}
