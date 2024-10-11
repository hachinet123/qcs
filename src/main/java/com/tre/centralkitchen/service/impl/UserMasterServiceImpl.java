package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.UserMasterConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.UserMasterBo;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.domain.vo.system.UserMasterUpSearchVo;
import com.tre.centralkitchen.domain.vo.system.UserMasterVo;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.UserMasterService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMasterServiceImpl implements UserMasterService {

    private final MtUserloginMapper userloginMapper;
    private final MtUserattrMapper mtUserattrMapper;
    private final MtUserroleMapper mtUserroleMapper;
    private final MtEmployeesMapper mtEmployeesMapper;
    private final MtUserloginHistoryMapper mtUserloginHistoryMapper;
    private final MtUserattrHistoryMapper mtUserattrHistoryMapper;
    private final MtUserroleHistoryMapper mtUserroleHistoryMapper;
    @Resource
    private UserMasterMapper userMasterMapper;

    @Override
    public TableDataInfo<UserMasterVo> getUserMasterList(UserMasterBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(userMasterMapper.getUserMasterList(bo, pageQuery.build()));
    }

    @Override
    public UserMasterUpSearchVo getUserMasterUpSearch(UserMasterBo bo) {
        return userMasterMapper.getUserMasterUpSearch(bo);
    }

    @Override
    public void deleteUserMaster(UserMasterBo bo) {
        QueryWrapper<MtUserlogin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtUserlogin::getCode, bo.getUserId());
        queryWrapper.lambda().eq(MtUserlogin::getFDel, 0);
        MtUserlogin mtUserlogin = userloginMapper.selectOne(queryWrapper);
        mtUserlogin.setFDel(1);
        userloginMapper.update(mtUserlogin, queryWrapper);
        //履歴書の挿入
        MtUserloginHistory history = new MtUserloginHistory();
        BeanUtil.copyProperties(mtUserlogin, history);
        history.setUpdateTypeId(3);
        mtUserloginHistoryMapper.insert(history);
        //ログインユーザーマスタ所属
        QueryWrapper<MtUserattr> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(MtUserattr::getCode, bo.getUserId());
        queryWrapper1.lambda().eq(MtUserattr::getFDel, 0);
        MtUserattr mtUserattr = mtUserattrMapper.selectOne(queryWrapper1);
        mtUserattr.setFDel(1);
        mtUserattrMapper.update(mtUserattr, queryWrapper1);
        //履歴書の挿入
        MtUserattrHistory mtUserattrHistory = new MtUserattrHistory();
        BeanUtil.copyProperties(mtUserattr, mtUserattrHistory);
        mtUserattrHistory.setUpdateTypeId(3);
        mtUserattrHistoryMapper.insert(mtUserattrHistory);
        //ログインユーザーマスタ権限
        QueryWrapper<MtUserrole> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(MtUserrole::getCode, bo.getUserId());
        queryWrapper2.lambda().eq(MtUserrole::getFDel, 0);
        MtUserrole mtUserrole = mtUserroleMapper.selectOne(queryWrapper2);
        mtUserrole.setFDel(1);
        mtUserroleMapper.update(mtUserrole, queryWrapper2);
        //履歴書の挿入
        MtUserroleHistory mtUserroleHistory = new MtUserroleHistory();
        BeanUtil.copyProperties(mtUserrole, mtUserroleHistory);
        mtUserroleHistory.setUpdateTypeId(3);
        mtUserroleHistoryMapper.insert(mtUserroleHistory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserMaster(UserMasterBo bo) {
        QueryWrapper<MtUserlogin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtUserlogin::getCode, bo.getUserId());
        MtUserlogin mtUserlogin = userloginMapper.selectOne(queryWrapper);
        MtUserlogin mtUserlogin1 = BeanUtil.toBean(bo, MtUserlogin.class);
        MtUserloginHistory mtUserloginHistory = new MtUserloginHistory();
        if (mtUserlogin == null) {
            mtUserlogin1.setCode(bo.getUserId());
            mtUserlogin1.setMemo(bo.getMemo());
            mtUserlogin1.setInsFuncId(bo.getInsFuncId());
            mtUserlogin1.setInsOpeId(bo.getInsOpeId());
            userloginMapper.insert(mtUserlogin1);

            BeanUtil.copyProperties(mtUserlogin1, mtUserloginHistory);
            mtUserloginHistory.setUpdateTypeId(1);
            mtUserloginHistoryMapper.insert(mtUserloginHistory);
        } else {
            mtUserlogin1.setFDel(0);
            mtUserlogin1.setMemo(bo.getMemo());
            mtUserlogin1.setUpdFuncId(bo.getUpdFuncId());
            mtUserlogin1.setUpdOpeId(bo.getUpdOpeId());
            userloginMapper.update(mtUserlogin1, queryWrapper);

            BeanUtil.copyProperties(mtUserlogin1, mtUserloginHistory);
            mtUserloginHistory.setCode(bo.getUserId());
            mtUserloginHistory.setInsDate(mtUserlogin.getInsDate());
            mtUserloginHistory.setInsTime(mtUserlogin.getInsTime());
            mtUserloginHistory.setInsUserId(mtUserlogin.getInsUserId());
            mtUserloginHistory.setUpdateTypeId(1);
            mtUserloginHistoryMapper.insert(mtUserloginHistory);
        }
        QueryWrapper<MtUserattr> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(MtUserattr::getCode, bo.getUserId());
        MtUserattr mtUserattr = mtUserattrMapper.selectOne(queryWrapper1);
        MtUserattr mtUserattr1 = BeanUtil.toBean(bo, MtUserattr.class);
        MtUserattrHistory mtUserattrHistory = new MtUserattrHistory();
        if (mtUserattr == null) {
            mtUserattr1.setCode(bo.getUserId());
            mtUserattr1.setCenterId(bo.getCenterId());
            mtUserattr1.setLineId(bo.getLineId());
            mtUserattr1.setInsFuncId(bo.getInsFuncId());
            mtUserattr1.setInsOpeId(bo.getInsOpeId());
            mtUserattrMapper.insert(mtUserattr1);

            BeanUtil.copyProperties(mtUserattr1, mtUserattrHistory);
            mtUserattrHistory.setUpdateTypeId(1);
            mtUserattrHistoryMapper.insert(mtUserattrHistory);
        } else {
            mtUserattr1.setFDel(0);
            mtUserattr1.setCenterId(bo.getCenterId());
            mtUserattr1.setLineId(bo.getLineId());
            mtUserattr1.setUpdFuncId(bo.getUpdFuncId());
            mtUserattr1.setUpdOpeId(bo.getUpdOpeId());
            mtUserattrMapper.update(mtUserattr1, queryWrapper1);

            mtUserattr1.setCode(bo.getUserId());
            BeanUtil.copyProperties(mtUserattr1, mtUserattrHistory);
            mtUserattrHistory.setUpdateTypeId(1);
            mtUserattrHistory.setInsDate(mtUserattr.getInsDate());
            mtUserattrHistory.setInsTime(mtUserattr.getInsTime());
            mtUserattrHistory.setInsUserId(mtUserattr.getInsUserId());
            mtUserattrHistoryMapper.insert(mtUserattrHistory);
        }
        QueryWrapper<MtUserrole> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(MtUserrole::getCode, bo.getUserId());
        MtUserrole mtUserrole = mtUserroleMapper.selectOne(queryWrapper2);
        MtUserrole mtUserrole1 = BeanUtil.toBean(bo, MtUserrole.class);
        MtUserroleHistory mtUserroleHistory = new MtUserroleHistory();
        if (mtUserrole == null) {
            mtUserrole1.setCode(bo.getUserId());
            mtUserrole1.setFreecolumn1(0);
            mtUserrole1.setFreecolumn2(0);
            mtUserrole1.setFreecolumn3(0);
            mtUserrole1.setFreecolumn4(0);
            mtUserrole1.setFreecolumn5(0);
            mtUserrole1.setInsFuncId(bo.getInsFuncId());
            mtUserrole1.setInsOpeId(bo.getInsOpeId());
            mtUserroleMapper.insert(mtUserrole1);

            BeanUtil.copyProperties(mtUserrole1, mtUserroleHistory);
            mtUserroleHistory.setUpdateTypeId(1);
            mtUserroleHistoryMapper.insert(mtUserroleHistory);
        } else {
            mtUserrole1.setFDel(0);
            mtUserrole1.setUpdFuncId(bo.getUpdFuncId());
            mtUserrole1.setUpdOpeId(bo.getUpdOpeId());
            mtUserroleMapper.update(mtUserrole1, queryWrapper2);

            mtUserrole1.setCode(bo.getUserId());
            BeanUtil.copyProperties(mtUserrole1, mtUserroleHistory);
            mtUserroleHistory.setInsDate(mtUserrole.getInsDate());
            mtUserroleHistory.setInsTime(mtUserrole.getInsTime());
            mtUserroleHistory.setInsUserId(mtUserrole.getInsUserId());
            mtUserroleHistory.setUpdateTypeId(1);
            mtUserroleHistoryMapper.insert(mtUserroleHistory);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserMaster(UserMasterBo bo) {
        QueryWrapper<MtUserlogin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtUserlogin::getCode, bo.getUserId());
        queryWrapper.lambda().eq(MtUserlogin::getFDel, 0);
        MtUserlogin mtUserlogin = userloginMapper.selectOne(queryWrapper);
        mtUserlogin.setMemo(bo.getMemo());
        mtUserlogin.setUpdFuncId(bo.getUpdFuncId());
        mtUserlogin.setUpdOpeId(bo.getUpdOpeId());
        userloginMapper.update(mtUserlogin, queryWrapper);

        MtUserloginHistory mtUserloginHistory = new MtUserloginHistory();
        BeanUtil.copyProperties(mtUserlogin, mtUserloginHistory);
        mtUserloginHistory.setUpdateTypeId(2);
        mtUserloginHistoryMapper.insert(mtUserloginHistory);

        QueryWrapper<MtUserattr> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(MtUserattr::getCode, bo.getUserId());
        queryWrapper1.lambda().eq(MtUserattr::getFDel, 0);
        MtUserattr mtUserattr = mtUserattrMapper.selectOne(queryWrapper1);
        mtUserattr.setCenterId(bo.getCenterId());
        mtUserattr.setLineId(bo.getLineId());
        mtUserattr.setUpdFuncId(bo.getUpdFuncId());
        mtUserattr.setUpdOpeId(bo.getUpdOpeId());
        mtUserattrMapper.update(mtUserattr, queryWrapper1);

        MtUserattrHistory mtUserattrHistory = new MtUserattrHistory();
        BeanUtil.copyProperties(mtUserattr, mtUserattrHistory);
        mtUserattrHistory.setUpdateTypeId(2);
        mtUserattrHistoryMapper.insert(mtUserattrHistory);

        QueryWrapper<MtUserrole> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(MtUserrole::getCode, bo.getUserId());
        queryWrapper2.lambda().eq(MtUserrole::getFDel, 0);
        MtUserrole mtUserrole = mtUserroleMapper.selectOne(queryWrapper2);
        mtUserrole.setOtherCenterAuthority(bo.getOtherCenterAuthority());
        mtUserrole.setOtherLineAuthority(bo.getOtherLineAuthority());
        mtUserrole.setWarehouseAuthority(bo.getWarehouseAuthority());
        mtUserrole.setMetaMaterialAuthority(bo.getMetaMaterialAuthority());
        mtUserrole.setMiddleAuthority(bo.getMiddleAuthority());
        mtUserrole.setProductAuthority(bo.getProductAuthority());
        mtUserrole.setMasterAuthority(bo.getMasterAuthority());
        mtUserrole.setUserAuthority(bo.getUserAuthority());
        mtUserrole.setExceptAuthority(bo.getExceptAuthority());
        mtUserrole.setSystemAuthority(bo.getSystemAuthority());
        mtUserrole.setRecipeAuthority(bo.getRecipeAuthority());
        mtUserrole.setUpdFuncId(bo.getUpdFuncId());
        mtUserrole.setUpdOpeId(bo.getUpdOpeId());
        mtUserroleMapper.update(mtUserrole, queryWrapper2);

        MtUserroleHistory mtUserroleHistory = new MtUserroleHistory();
        BeanUtil.copyProperties(mtUserrole, mtUserroleHistory);
        mtUserroleHistory.setUpdateTypeId(2);
        mtUserroleHistoryMapper.insert(mtUserroleHistory);
    }

    @Override
    public void downloadUserMaster(UserMasterBo bo, HttpServletResponse response) {
        List<UserMasterVo> downLoadVoList = userMasterMapper.getUserMasterList(bo);
        SimpleCsvTableUtils.easyCsvExport(response, UserMasterConstants.USER_MASTER_CSV_NAME, downLoadVoList, UserMasterVo.class);
    }

    @Override
    public MtEmployeesVo userName(String userId) {
        QueryWrapper<MtUserlogin> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtUserlogin::getCode, userId);
        queryWrapper.lambda().eq(MtUserlogin::getFDel, 0);
        MtUserlogin mtUserlogin = userloginMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(mtUserlogin)) {
            MtEmployeesVo mtEmployeesVo = mtEmployeesMapper.queryName(userId);
            if (mtEmployeesVo != null) {
                return mtEmployeesVo;
            } else {
                throw new SysBusinessException(SysConstantInfo.EMPLOYEE_NOT_EXISTENT_MSG, HttpStatus.HTTP_OK, SysConstantInfo.EMPLOYEE_NOT_EXISTENT_CODE);
            }
        } else {
            throw new SysBusinessException(SysConstantInfo.EMPLOYEE_EXISTENT_MSG, HttpStatus.HTTP_OK, SysConstantInfo.EMPLOYEE_EXISTENT_CODE);
        }
    }

    public MtEmployeesVo getUserName(String userId) {
        return mtEmployeesMapper.getUserName(userId);
    }
}
