package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.TrMailConfirmHoldBo;
import com.tre.centralkitchen.domain.po.MtMailcontrol;
import com.tre.centralkitchen.domain.po.TrMailConfirmHold;
import com.tre.centralkitchen.domain.vo.system.TrMailConfirmHoldVo;
import com.tre.centralkitchen.mapper.MtMailcontrolMapper;
import com.tre.centralkitchen.mapper.TrMailConfirmHoldMapper;
import com.tre.centralkitchen.service.TrMailConfirmHoldService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrMailConfirmHoldServiceImpl implements TrMailConfirmHoldService {
    private final TrMailConfirmHoldMapper mapper;
    private final MtMailcontrolMapper mtMailcontrolMapper;

    private static void checkout(TrMailConfirmHoldBo bo) {
        String mailNoList = bo.getMailNo();
        if (mailNoList != null && !mailNoList.isEmpty()) {
            List<String> list1 = Arrays.asList(mailNoList.split(","));
            bo.setMailNoList(list1);
        }
    }

    @Override
    public TableDataInfo<TrMailConfirmHoldVo> queryList(TrMailConfirmHoldBo bo, PageQuery pageQuery) {
        checkout(bo);
        Page<TrMailConfirmHoldVo> page = mapper.queryList(bo, pageQuery.build());
        for (TrMailConfirmHoldVo vo : page.getRecords()) {
            if (ObjectUtil.isNull(vo.getStartDate())) {
                vo.setFlag(0);
                vo.setMemo(vo.getTime() + SysConstantInfo.MEMO);
            } else {
                if (ObjectUtil.isNull(vo.getEndDate())) {
                    vo.setFlag(1);
                } else {
                    if (vo.getEndDate().before(vo.getStartDate())) {
                        vo.setFlag(1);
                    } else {
                        vo.setFlag(0);
                        vo.setMemo(vo.getTime() + SysConstantInfo.MEMO);
                    }
                }
            }
        }
        return TableDataInfo.build(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TrMailConfirmHoldBo bo) {
        QueryWrapper<MtMailcontrol> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
        queryWrapper.lambda().eq(MtMailcontrol::getMailNo, Integer.valueOf(bo.getMailNo()));
        MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper);
        if (!ObjectUtil.isNull(mtMailcontrol)) {
            QueryWrapper<TrMailConfirmHold> queryWrapper1 = new QueryWrapper<>();
            if (bo.getNum() == 0) {
                queryWrapper1.lambda().eq(TrMailConfirmHold::getCenterId, bo.getCenterId());
                queryWrapper1.lambda().eq(TrMailConfirmHold::getMailNo, Integer.valueOf(bo.getMailNo()));
                queryWrapper1.lambda().eq(TrMailConfirmHold::getDlvschedDate, mtMailcontrol.getDlvschedDate());
                TrMailConfirmHold trMailconfirmHold = mapper.selectOne(queryWrapper1);
                existCheck(bo, mtMailcontrol, queryWrapper1, trMailconfirmHold);
            } else {
                queryWrapper1.lambda().eq(TrMailConfirmHold::getCenterId, bo.getCenterId());
                queryWrapper1.lambda().eq(TrMailConfirmHold::getMailNo, Integer.valueOf(bo.getMailNo()));
                queryWrapper1.lambda().eq(TrMailConfirmHold::getDlvschedDate, mtMailcontrol.getDlvschedDate());
                TrMailConfirmHold trMailconfirmHold = mapper.selectOne(queryWrapper1);
                if (!ObjectUtil.isNull(trMailconfirmHold)) {
                    trMailconfirmHold.setEdDate(LocalDateTime.now());
                    trMailconfirmHold.setUpdFuncId(bo.getUpdFuncId());
                    trMailconfirmHold.setUpdOpeId(bo.getUpdOpeId());
                    mapper.update(trMailconfirmHold, queryWrapper1);
                } else {
                    throw new SysBusinessException(SysConstantInfo.ERROR, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
                }
            }
        } else {
            throw new SysBusinessException(SysConstantInfo.DLVSCHEDDATE_NOT_EXISTENT, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    private void existCheck(TrMailConfirmHoldBo bo, MtMailcontrol mtMailcontrol, QueryWrapper<TrMailConfirmHold> queryWrapper1, TrMailConfirmHold trMailconfirmHold) {
        if (ObjectUtil.isNull(trMailconfirmHold)) {
            TrMailConfirmHold trMailConfirmHold1 = BeanUtil.toBean(bo, TrMailConfirmHold.class);
            trMailConfirmHold1.setDlvschedDate(mtMailcontrol.getDlvschedDate());
            trMailConfirmHold1.setStDate(LocalDateTime.now());
            trMailConfirmHold1.setInsFuncId(bo.getInsFuncId());
            trMailConfirmHold1.setInsOpeId(bo.getInsOpeId());
            mapper.insert(trMailConfirmHold1);
        } else {
            if (!ObjectUtil.isNull(trMailconfirmHold.getEdDate())) {
                trMailconfirmHold.setStDate(LocalDateTime.now());
                trMailconfirmHold.setEdDate(null);
                trMailconfirmHold.setUpdFuncId(bo.getUpdFuncId());
                trMailconfirmHold.setUpdOpeId(bo.getUpdOpeId());
                mapper.update(trMailconfirmHold, queryWrapper1);
            }
        }
    }
}
