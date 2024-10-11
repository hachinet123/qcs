package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.UserMasterBo;
import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.domain.vo.system.UserMasterUpSearchVo;
import com.tre.centralkitchen.domain.vo.system.UserMasterVo;

import javax.servlet.http.HttpServletResponse;

public interface UserMasterService {

    TableDataInfo<UserMasterVo> getUserMasterList(UserMasterBo bo, PageQuery pageQuery);

    UserMasterUpSearchVo getUserMasterUpSearch(UserMasterBo bo);

    void deleteUserMaster(UserMasterBo bo);

    void insertUserMaster(UserMasterBo bo);

    void updateUserMaster(UserMasterBo bo);

    void downloadUserMaster(UserMasterBo bo, HttpServletResponse response);

    MtEmployeesVo userName(String userId);

    MtEmployeesVo getUserName(String userId);

}
