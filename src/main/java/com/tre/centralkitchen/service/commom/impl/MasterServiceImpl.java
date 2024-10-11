package com.tre.centralkitchen.service.commom.impl;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.domain.bo.common.CommonCenterLineBo;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.common.*;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.centralkitchen.service.impl.TmShoinServiceImpl;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {
    public static final String F_DEL = "f_del";
    private static final String ID = "center_id";
    private final MtPlaceMapper mtPlaceMapper;
    private final MtMailMapper mtMailMapper;
    private final MtProductwkgrpMapper mtProductwkgrpMapper;
    private final ProductsMapper productsMapper;
    private final MtHomeMapper homeMapper;
    private final CommonMapper commonMapper;
    private final TmShoinServiceImpl tmShoinService;

    @Override
    public List<CenterListVo> getCenterList() {
        return commonMapper.getCenterList();
    }

    @Override
    public List<LineListVo> getLineAllList() {
        return commonMapper.getLineAllList();
    }

    @Override
    public List<MtProductwkgrpVo> getBigGroupList(CommonCenterLineBo bo) {
        QueryWrapper<MtProductwkgrp> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtProductwkgrp::getPid, 0);
        queryWrapper.eq(F_DEL, 0);
        queryWrapper.eq(ID, bo.getCenterId());
        queryWrapper.lambda().eq(MtProductwkgrp::getLineId, bo.getLineId());
        queryWrapper.lambda().orderByAsc(MtProductwkgrp::getSeq).orderByAsc(MtProductwkgrp::getId);
        return mtProductwkgrpMapper.selectVoList(queryWrapper, MtProductwkgrpVo.class);
    }

    @Override
    public List<MailListVo> getMailList(CommonMailNoBo bo) {
        QueryWrapper<MtMail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ID, bo.getCenterId());
        queryWrapper.lambda().eq(MtMail::getFDel, 0);
        queryWrapper.orderByAsc("seq", "mail_no");
        return mtMailMapper.selectVoList(queryWrapper, MailListVo.class);
    }

    @Override
    public List<WorkGroupListVo> getWorkGroupList(String centerId, String lineId) {
        QueryWrapper<MtProductwkgrp> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtProductwkgrp::getFDel, 0);
        queryWrapper.lambda().notIn(MtProductwkgrp::getPid, 0);
        if (centerId != null && !centerId.isBlank()) {
            queryWrapper.lambda().eq(MtProductwkgrp::getCenterId, Integer.parseInt(centerId));
        }
        if (!lineId.isBlank()) {
            List<Integer> lineIdList = Arrays.stream(lineId.split(StringConstants.COMMA))
                    .map(Integer::parseInt).collect(Collectors.toList());
            queryWrapper.lambda().in(MtProductwkgrp::getLineId, lineIdList);
        }
        queryWrapper.orderByAsc("seq");
        return mtProductwkgrpMapper.selectVoList(queryWrapper, WorkGroupListVo.class);
    }

    @Override
    public List<BranchListVo> getBranchList() {
        return commonMapper.getBranchList();
    }

    @Override
    public List<MtPlaceVo> getPlaceList() {
        QueryWrapper<MtPlace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(F_DEL, 0);
        queryWrapper.orderByAsc("seq", "id");
        return mtPlaceMapper.selectVoList(queryWrapper, MtPlaceVo.class);
    }

    @Override
    public Products getItemName(String itemId) {
        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Products::getProductcd, itemId);
        return productsMapper.selectOne(queryWrapper);
    }

    @Override
    public List<BranchListVo> getStoreName(Integer centerId, Integer storeId) {
        return commonMapper.getStoreName(centerId, storeId);
    }

    @Override
    public List<MtHome> getHomeData() {
        QueryWrapper<MtHome> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(F_DEL, 0);
        queryWrapper.orderByAsc("seq", "id");
        return homeMapper.selectList(queryWrapper);
    }

    @Override
    public List<BranchListVo> queryMailBasicStoreList(Integer centerId) {
        return commonMapper.queryMailBasicStoreList(centerId);
    }

    @Override
    public TmShohinVo getItemNameByStoreCallCode(String storeIds, String callCode) {
        List<Integer> integerList = Arrays.stream(storeIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<TmShohinVo> tmShohinVos = new ArrayList<>();
        for (Integer storeId : integerList) {
            List<TmShohinVo> tmShohinVoList = tmShoinService.findJanByCallCodeAll(storeId, Integer.parseInt(callCode));
            if (!tmShohinVoList.isEmpty()) {
                tmShohinVos.add(tmShohinVoList.get(0));
            } else {
                throw new SysBusinessException(SysConstantInfo.STORE_NO_PRODUCT_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.ERROR_CODE);
            }
        }
        if (tmShohinVos.stream().collect(Collectors.groupingBy(TmShohinVo::getItemId)).size() > 1) {
            throw new SysBusinessException(SysConstantInfo.STORE_JAN_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.ERROR_CODE);
        }
        return tmShohinVos.get(0);
    }

    @Override
    public TmShohinVo getItemNameByStore(String storeIds, String itemId) {
        List<Integer> integerList = Arrays.stream(storeIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        List<TmShohinVo> tmShohinVos = new ArrayList<>();
        for (Integer storeId : integerList) {
            List<TmShohinVo> tmShohinVoList = tmShoinService.findJanStoreAll(storeId, itemId);
            if (!tmShohinVoList.isEmpty()) {
                tmShohinVos.add(tmShohinVoList.get(0));
            } else {
                throw new SysBusinessException(SysConstantInfo.STORE_NO_PRODUCT_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.ERROR_CODE);
            }
        }
        if (tmShohinVos.stream().collect(Collectors.groupingBy(TmShohinVo::getItemId)).size() > 1) {
            throw new SysBusinessException(SysConstantInfo.STORE_JAN_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.ERROR_CODE);
        }
        return tmShohinVos.get(0);
    }

    public List<WarehouseVo> getWarehouse(Integer centerId) {
        return commonMapper.getWarehouse(centerId);
    }

    @Override
    public List<BacteriacheckListVo> getTemperature() {
        return commonMapper.getBacteriacheckLList(1016);
    }

    @Override
    public List<BacteriacheckListVo> getCheckObj() {
        return commonMapper.getBacteriacheckLList(1023);
    }

    @Override
    public List<BacteriacheckListVo> getCheckTime() {
        return commonMapper.getBacteriacheckLList(1022);
    }

    @Override
    public List<StoreShortNameVo> getStoreShortName(Integer centerId, Boolean isExceptClosed) {
        return commonMapper.getStoreShortName(centerId, isExceptClosed);
    }

    @Override
    public TmShohinVo getItemNameByCenterCallCode(Integer centerId, Integer callCode) {
        return tmShoinService.findItemNameByCenterCallCode(centerId, callCode);
    }
}
