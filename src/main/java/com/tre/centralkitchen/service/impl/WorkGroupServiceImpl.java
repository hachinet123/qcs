package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.WorkGroupBo;
import com.tre.centralkitchen.domain.po.MtProductwkgrp;
import com.tre.centralkitchen.domain.po.MtProductwkgrpHistory;
import com.tre.centralkitchen.domain.vo.system.WorkGroupVo;
import com.tre.centralkitchen.mapper.MtProductwkgrpHistoryMapper;
import com.tre.centralkitchen.mapper.MtProductwkgrpMapper;
import com.tre.centralkitchen.mapper.WorkGroupMapper;
import com.tre.centralkitchen.service.WorkGroupService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WorkGroupServiceImpl implements WorkGroupService {

    private final MtProductwkgrpMapper mtProductwkgrpMapper;
    private final MtProductwkgrpHistoryMapper productwkgrpHistoryMapper;
    @Resource
    private WorkGroupMapper workGroupMapper;

    @Override
    public TableDataInfo<WorkGroupVo> getWorkGroupList(WorkGroupBo bo, PageQuery pageQuery) {
        String strArr = bo.getLineId();
        List<String> list = new ArrayList<>();
        if (strArr != null && !strArr.isEmpty()) {
            list = Arrays.asList(strArr.split(","));
        }
        Integer centerId = bo.getCenterId();
        return TableDataInfo.build(workGroupMapper.getWorkGroupList(centerId, list, pageQuery.build()));
    }

    @Override
    public WorkGroupVo getUpWorkGroup(WorkGroupBo bo) {
        return workGroupMapper.getUpWorkGroup(bo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkGroup(WorkGroupBo bo) {
        QueryWrapper<MtProductwkgrp> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(MtProductwkgrp::getId, bo.getGroupSmall());
        queryWrapper1.lambda().eq(MtProductwkgrp::getFDel, 0);
        MtProductwkgrp mtProductwkgrp1 = mtProductwkgrpMapper.selectOne(queryWrapper1);
        mtProductwkgrp1.setFDel(1);
        mtProductwkgrpMapper.update(mtProductwkgrp1, queryWrapper1);
        MtProductwkgrpHistory history = new MtProductwkgrpHistory();
        BeanUtil.copyProperties(mtProductwkgrp1, history);
        history.setUpdateTypeId(3);
        productwkgrpHistoryMapper.insert(history);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkGroup(WorkGroupBo bo) {
        QueryWrapper<MtProductwkgrp> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtProductwkgrp::getId, bo.getGroupSmall());
        queryWrapper.lambda().eq(MtProductwkgrp::getCenterId, bo.getCenterId());
        queryWrapper.lambda().eq(MtProductwkgrp::getLineId, Integer.valueOf(bo.getLineId()));
        queryWrapper.lambda().eq(MtProductwkgrp::getFDel, 0);
        MtProductwkgrp mtProductwkgrp = mtProductwkgrpMapper.selectOne(queryWrapper);
        mtProductwkgrp.setName(bo.getGroupSmallName());
        mtProductwkgrp.setPid(bo.getGroupBig());
        mtProductwkgrp.setInsFuncId(bo.getInsFuncId());
        mtProductwkgrp.setInsOpeId(bo.getInsOpeId());
        mtProductwkgrpMapper.update(mtProductwkgrp, queryWrapper);
        MtProductwkgrpHistory history = new MtProductwkgrpHistory();
        BeanUtil.copyProperties(mtProductwkgrp, history);
        history.setUpdateTypeId(2);
        productwkgrpHistoryMapper.insert(history);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkGroupVo insertBigGroup(WorkGroupBo bo) {
        if (bo.getGroupBigName().equals("")) {
            throw new SysBusinessException(SysConstantInfo.GROUP_BIG_NOT_EMPTY, HttpStatus.HTTP_OK, SysConstantInfo.LARGE_CATEGORY_REPETITION_CODE);
        } else {
            QueryWrapper<MtProductwkgrp> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MtProductwkgrp::getName, bo.getGroupBigName());
            queryWrapper.lambda().eq(MtProductwkgrp::getFDel, 0);
            queryWrapper.lambda().eq(MtProductwkgrp::getLineId, Integer.parseInt(bo.getLineId()));
            queryWrapper.lambda().eq(MtProductwkgrp::getCenterId, bo.getCenterId());
            queryWrapper.lambda().eq(MtProductwkgrp::getPid, 0);
            MtProductwkgrp mtProductwkgrp = mtProductwkgrpMapper.selectOne(queryWrapper);
            if (mtProductwkgrp == null || mtProductwkgrp.getName() == null) {
                QueryWrapper<MtProductwkgrp> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select("max(id) as id");
                MtProductwkgrp id = mtProductwkgrpMapper.selectOne(queryWrapper1);
                if (ObjectUtil.isNull(id)) {
                    id.setId(0);
                }
                QueryWrapper<MtProductwkgrp> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().eq(MtProductwkgrp::getPid, 0);
                queryWrapper2.select("max(seq) as seq");
                MtProductwkgrp mtProductwkgrp2 = mtProductwkgrpMapper.selectOne(queryWrapper2);
                if (ObjectUtil.isNull(mtProductwkgrp2)) {
                    mtProductwkgrp2.setId(0);
                }
                MtProductwkgrp mtProductwkgrp1 = BeanUtil.toBean(bo, MtProductwkgrp.class);
                mtProductwkgrp1.setId(id.getId() + 1);
                mtProductwkgrp1.setCenterId(bo.getCenterId());
                mtProductwkgrp1.setLineId(Integer.valueOf(bo.getLineId()));
                mtProductwkgrp1.setName(bo.getGroupBigName());
                mtProductwkgrp1.setPid(0);
                mtProductwkgrp1.setSeq(mtProductwkgrp2.getSeq() + 1);
                mtProductwkgrp1.setInsFuncId(bo.getInsFuncId());
                mtProductwkgrp1.setInsOpeId(bo.getInsOpeId());
                mtProductwkgrpMapper.insert(mtProductwkgrp1);

                MtProductwkgrpHistory history = new MtProductwkgrpHistory();
                BeanUtil.copyProperties(mtProductwkgrp1, history);
                history.setUpdateTypeId(1);
                productwkgrpHistoryMapper.insert(history);
            } else {
                throw new SysBusinessException(SysConstantInfo.GROUP_BIG_EXISTENT, HttpStatus.HTTP_OK, SysConstantInfo.LARGE_CATEGORY_REPETITION_CODE);
            }
            QueryWrapper<MtProductwkgrp> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(MtProductwkgrp::getName, bo.getGroupBigName());
            queryWrapper1.lambda().eq(MtProductwkgrp::getFDel, 0);
            queryWrapper1.lambda().eq(MtProductwkgrp::getLineId, Integer.parseInt(bo.getLineId()));
            queryWrapper1.lambda().eq(MtProductwkgrp::getCenterId, bo.getCenterId());
            queryWrapper1.lambda().eq(MtProductwkgrp::getPid, 0);
            MtProductwkgrp mtProductwkgrp1 = mtProductwkgrpMapper.selectOne(queryWrapper1);
            WorkGroupVo workGroupVo = new WorkGroupVo();
            workGroupVo.setGroupBig(mtProductwkgrp1.getId());
            workGroupVo.setGroupBigName(mtProductwkgrp1.getName());
            return workGroupVo;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertSmallGroup(WorkGroupBo bo) {
        if (bo.getGroupBig() == null) {
            QueryWrapper<MtProductwkgrp> wrapper = new QueryWrapper<>();
            wrapper.lambda().eq(MtProductwkgrp::getName, bo.getGroupBigName());
            wrapper.lambda().eq(MtProductwkgrp::getCenterId, bo.getCenterId());
            wrapper.lambda().eq(MtProductwkgrp::getLineId, Integer.parseInt(bo.getLineId()));
            wrapper.lambda().eq(MtProductwkgrp::getFDel, 0);
            wrapper.lambda().eq(MtProductwkgrp::getPid, 0);
            MtProductwkgrp bigGroup = mtProductwkgrpMapper.selectOne(wrapper);
            if (bigGroup == null) {
                throw new SysBusinessException(SysConstantInfo.GROUP_BIG_NOT_EXISTENT, HttpStatus.HTTP_OK, SysConstantInfo.SMALL_CATEGORY_REPETITION_CODE);
            } else {
                bo.setGroupBig(bigGroup.getId());
            }
        }
        if (bo.getGroupSmallName().equals("")) {
            throw new SysBusinessException(SysConstantInfo.GROUP_SMALL_NOT_EMPTY, HttpStatus.HTTP_OK, SysConstantInfo.SMALL_CATEGORY_REPETITION_CODE);
        } else {
            QueryWrapper<MtProductwkgrp> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MtProductwkgrp::getName, bo.getGroupSmallName());
            queryWrapper.lambda().eq(MtProductwkgrp::getPid, bo.getGroupBig());
            queryWrapper.lambda().eq(MtProductwkgrp::getCenterId, bo.getCenterId());
            queryWrapper.lambda().eq(MtProductwkgrp::getLineId, Integer.valueOf(bo.getLineId()));
            queryWrapper.lambda().eq(MtProductwkgrp::getFDel, 0);
            MtProductwkgrp mtProductwkgrp = mtProductwkgrpMapper.selectOne(queryWrapper);

            if (mtProductwkgrp == null || mtProductwkgrp.getName() == null) {
                QueryWrapper<MtProductwkgrp> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select("max(id) as id");
                MtProductwkgrp id = mtProductwkgrpMapper.selectOne(queryWrapper1);

                MtProductwkgrp mtProductwkgrp1 = BeanUtil.toBean(bo, MtProductwkgrp.class);
                mtProductwkgrp1.setId(id.getId() + 1);
                mtProductwkgrp1.setCenterId(bo.getCenterId());
                mtProductwkgrp1.setLineId(Integer.valueOf(bo.getLineId()));
                mtProductwkgrp1.setName(bo.getGroupSmallName());
                mtProductwkgrp1.setPid(bo.getGroupBig());

                int seq = 0;
                QueryWrapper<MtProductwkgrp> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.lambda().eq(MtProductwkgrp::getCenterId, bo.getCenterId());
                queryWrapper3.lambda().eq(MtProductwkgrp::getLineId, Integer.valueOf(bo.getLineId()));
                queryWrapper3.lambda().eq(MtProductwkgrp::getPid, bo.getGroupBig());
                queryWrapper3.select("max(seq) as seq");
                MtProductwkgrp seq1 = mtProductwkgrpMapper.selectOne(queryWrapper3);
                if (seq1 == null || seq1.getSeq() == null) {
                    seq = 1;
                } else {
                    seq = seq1.getSeq() + 1;
                }
                mtProductwkgrp1.setSeq(seq);
                mtProductwkgrp1.setInsFuncId(bo.getInsFuncId());
                mtProductwkgrp1.setInsOpeId(bo.getInsOpeId());
                mtProductwkgrpMapper.insert(mtProductwkgrp1);

                MtProductwkgrpHistory history = new MtProductwkgrpHistory();
                BeanUtil.copyProperties(mtProductwkgrp1, history);
                history.setUpdateTypeId(1);
                productwkgrpHistoryMapper.insert(history);
            } else {
                throw new SysBusinessException(SysConstantInfo.GROUP_SMALL_EXISTENT, HttpStatus.HTTP_OK, SysConstantInfo.SMALL_CATEGORY_REPETITION_CODE);
            }
        }
    }

    @Override
    public void downloadWorkGroup(WorkGroupBo bo, HttpServletResponse response) {
        String strArr = bo.getLineId();
        List<String> list = new ArrayList<>();
        if (strArr != null && !strArr.isEmpty()) {
            list = Arrays.asList(strArr.split(","));
        }
        Integer centerId = bo.getCenterId();
        List<WorkGroupVo> workGroupDownLoadPO = workGroupMapper.getWorkGroupList(centerId, list);
        String fileName = "マスタ_作業グループ";
        SimpleCsvTableUtils.easyCsvExport(response, fileName, workGroupDownLoadPO, WorkGroupVo.class);
    }
}
