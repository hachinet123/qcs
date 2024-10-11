package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.utils.DivideEquallyUtils;
import com.tre.centralkitchen.domain.bo.system.MailBasicSettingBo;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.common.BranchListVo;
import com.tre.centralkitchen.domain.vo.system.MailBasicSettingVo;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.IMailBasicSettingService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MailBasicSettingServiceImpl implements IMailBasicSettingService {
    private final MtMailstoreMapper mtMailstoreMapper;
    private final TokenTakeApart tokenTakeApart;
    private final BranchesMapper branchesMapper;
    private final MtMailMapper mtMailMapper;
    private final MtMailcontrolMapper mtMailcontrolMapper;
    private final TrProduceplanMapper trProduceplanMapper;
    private final ShTPcOrderListMapper shTPcOrderListMapper;
    private final MtCenterstatusMapper mtCenterstatusMapper;
    private final MtMailStoreHistoryMapper mtMailstoreHistoryMapper;
    private final shTOrderQtyTestMapper shTOrderQtyTestMapper;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Resource
    private MailBasicSettingMapper mailBasicSettingMapper;

    /**
     * 基本設定
     */
    @Override
    public List<JSONObject> getBasicSettingList(MailBasicSettingBo bo) {
        List<MailBasicSettingVo> mailBasicSettingPoList = mailBasicSettingMapper.getBasicSettingList(bo);
        DecimalFormat decimalFormat = new DecimalFormat("000");
        for (MailBasicSettingVo mailBasicSettingVo : mailBasicSettingPoList) {
            if (mailBasicSettingVo.getStoreId() != null) {
                mailBasicSettingVo.setStoreId(decimalFormat.format(Integer.valueOf(mailBasicSettingVo.getStoreId())));
            }
        }
        List<JSONObject> outList = new ArrayList<>();
        Map<String, String> centerInfo = mailBasicSettingPoList.stream()
                .collect(Collectors.toMap(MailBasicSettingVo::getCenterId, MailBasicSettingVo::getCenterName, (val1, val2) -> val2, HashMap::new));
        Map<String, List<MailBasicSettingVo>> mapCenter = mailBasicSettingPoList.stream()
                .collect(Collectors.groupingBy(MailBasicSettingVo::getCenterId, LinkedHashMap::new, Collectors.toList()));
        mapCenter.forEach((key, value) -> {
            JSONObject parent = new JSONObject();
            parent.put("key", centerInfo.get(key));
            parent.put("centerId", key);
            List<JSONObject> innerList = new ArrayList<>();
            Map<Integer, List<MailBasicSettingVo>> mapMailNo = value.stream().collect(Collectors.groupingBy(MailBasicSettingVo::getMailNo));
            Map<Integer, List<MailBasicSettingVo>> map = new TreeMap<>();
            mapMailNo.forEach(map::put);
            map.forEach((key1, value1) -> {
                JSONObject innerJson = new JSONObject();
                innerJson.put("key", key1);
                for (int i = 0; i < map.get(key1).size(); i++) {
                    innerJson.put("selGroup", map.get(key1).get(i).getSelGroup());
                    innerJson.put("discript", map.get(key1).get(i).getDiscript());
                }
                innerJson.put("value", value1);
                innerList.add(innerJson);
            });
            parent.put("value", innerList);
            outList.add(parent);
        });
        return outList;
    }

    @Override
    public void deleteStore(MailBasicSettingBo bo) {
        if (bo.getStoreId() != null) {
            QueryWrapper<MtMailstore> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MtMailstore::getCenterId, bo.getCenterId());
            queryWrapper.lambda().eq(MtMailstore::getMailNo, bo.getMailNo());
            queryWrapper.lambda().eq(MtMailstore::getStoreId, bo.getStoreId());
            queryWrapper.lambda().eq(MtMailstore::getFDel, 0);
            MtMailstore mtMailstore = mtMailstoreMapper.selectOne(queryWrapper);
            if (mtMailstore != null) {
                mtMailstore.setFDel(1);
            } else {
                throw new SysBusinessException(SysConstantInfo.STORE_NON_EXISTENT, HttpStatus.HTTP_OK, SysConstantInfo.STORE_CD_NOT_EXIST_CODE);
            }
            mtMailstoreMapper.update(mtMailstore, queryWrapper);
            MtMailStoreHistory history = new MtMailStoreHistory();
            BeanUtil.copyProperties(mtMailstore, history);
            history.setUpdateTypeId(3);
            mtMailstoreHistoryMapper.insert(history);
        } else {
            throw new SysBusinessException(SysConstantInfo.STORE_NOT_EMPTY, HttpStatus.HTTP_OK, SysConstantInfo.STORE_CD_CANNOT_EMPTY_CODE);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertStore(MailBasicSettingBo bo) {
        QueryWrapper<MtMailstore> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtMailstore::getCenterId, bo.getCenterId());
        queryWrapper.lambda().eq(MtMailstore::getMailNo, bo.getMailNo());
        queryWrapper.lambda().eq(MtMailstore::getStoreId, bo.getStoreId());
        queryWrapper.lambda().eq(MtMailstore::getFDel, 0);
        MtMailstore mtMailstore = mtMailstoreMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(mtMailstore)) {
            //グループ化
            MtMail mtMail = getMtMail(bo);
            if (mtMail.getSelGroup() != null && !mtMail.getSelGroup().equals("")) {
                bo.setSelGroup(mtMail.getSelGroup());
                //取得前の便
                MailBasicSettingVo mailBasicSettingVos = mailBasicSettingMapper.getMailNo(bo);
                //納入予定日の取得
                replaceBin(bo, mailBasicSettingVos);
            }
            QueryWrapper<MtMailstore> wrapper1 = new QueryWrapper<>();
            wrapper1.lambda().eq(MtMailstore::getCenterId, bo.getCenterId());
            wrapper1.lambda().eq(MtMailstore::getMailNo, bo.getMailNo());
            wrapper1.lambda().eq(MtMailstore::getStoreId, bo.getStoreId());
            //便があったかどうかを判断する
            MtMailstore mailStore = mtMailstoreMapper.selectOne(wrapper1);
            if (mailStore == null) {
                MtMailstore mtMailStore1 = BeanUtil.toBean(bo, MtMailstore.class);
                mtMailStore1.setMemo("");
                mtMailStore1.setInsFuncId(bo.getInsFuncId());
                mtMailStore1.setInsOpeId(bo.getInsOpeId());
                mtMailstoreMapper.insert(mtMailStore1);
                MtMailStoreHistory history = BeanUtil.toBean(mtMailStore1, MtMailStoreHistory.class);
                history.setUpdateTypeId(1);
                mtMailstoreHistoryMapper.insert(history);
            } else {
                mailStore.setFDel(0);
                mailStore.setUpdFuncId(bo.getUpdFuncId());
                mailStore.setUpdOpeId(bo.getUpdOpeId());
                mtMailstoreMapper.update(mailStore, wrapper1);
                MtMailStoreHistory history = new MtMailStoreHistory();
                BeanUtil.copyProperties(mailStore, history);
                history.setUpdateTypeId(1);
                mtMailstoreHistoryMapper.insert(history);
            }
        } else {
            throw new SysBusinessException(SysConstantInfo.STORE_NON_EXISTENT, HttpStatus.HTTP_OK, SysConstantInfo.STORE_CD_EXIST_CODE);
        }
    }

    private void replaceBin(MailBasicSettingBo bo, MailBasicSettingVo mailBasicSettingVos) {
        DateTime now = DateUtil.date();
        if (!ObjectUtil.isNull(mailBasicSettingVos)) {
            QueryWrapper<MtMailcontrol> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
            queryWrapper1.lambda().eq(MtMailcontrol::getMailNo, mailBasicSettingVos.getMailNo());
            MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper1);
            //取得前のデータ
            if (mtMailcontrol != null) {
                QueryWrapper<TrProducePlan> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().gt(TrProducePlan::getDlvschedDate, mtMailcontrol.getDlvschedDate());
                queryWrapper2.lambda().eq(TrProducePlan::getCenterId, bo.getCenterId());
                queryWrapper2.lambda().eq(TrProducePlan::getMailNo, mailBasicSettingVos.getMailNo());
                queryWrapper2.lambda().eq(TrProducePlan::getStoreId, bo.getStoreId());
                queryWrapper2.lambda().eq(TrProducePlan::getFDel, 0);
                List<TrProducePlan> trProduceplan = trProduceplanMapper.selectList(queryWrapper2);
                //循環置換便
                for (TrProducePlan trProduceplan1 : trProduceplan) {
                    trProduceplan1.setMailNo(bo.getMailNo());
                    trProduceplan1.setUpdFuncId(bo.getUpdFuncId());
                    trProduceplan1.setUpdOpeId(bo.getUpdOpeId());
                    trProduceplan1.setUpdDate(now);
                    trProduceplan1.setUpdTime(now);
                    QueryWrapper<TrProducePlan> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.lambda().eq(TrProducePlan::getDlvschedDate, trProduceplan1.getDlvschedDate());
                    queryWrapper3.lambda().eq(TrProducePlan::getCenterId, trProduceplan1.getCenterId());
                    queryWrapper3.lambda().eq(TrProducePlan::getMailNo, mailBasicSettingVos.getMailNo());
                    queryWrapper3.lambda().eq(TrProducePlan::getItemId, trProduceplan1.getItemId());
                    queryWrapper3.lambda().eq(TrProducePlan::getStoreId, trProduceplan1.getStoreId());
                    trProduceplanMapper.update(trProduceplan1, queryWrapper3);
                }
            }
            QueryWrapper<MtMailstore> wrapper = new QueryWrapper<>();
            //以前の表に存在する場合は、置換fdelは1
            if (!ObjectUtil.isNull(mailBasicSettingVos)) {
                wrapper.lambda().eq(MtMailstore::getCenterId, Integer.valueOf(mailBasicSettingVos.getCenterId()));
                wrapper.lambda().eq(MtMailstore::getMailNo, mailBasicSettingVos.getMailNo());
                wrapper.lambda().eq(MtMailstore::getStoreId, bo.getStoreId());
                wrapper.lambda().eq(MtMailstore::getFDel, 0);
                MtMailstore mailStore = mtMailstoreMapper.selectOne(wrapper);
                mailStore.setFDel(1);
                mailStore.setUpdFuncId(bo.getUpdFuncId());
                mailStore.setUpdOpeId(bo.getUpdOpeId());
                mtMailstoreMapper.update(mailStore, wrapper);
                MtMailStoreHistory history = new MtMailStoreHistory();
                BeanUtil.copyProperties(mailStore, history);
                history.setUpdateTypeId(3);
                mtMailstoreHistoryMapper.insert(history);
            }
        }
    }

    private MtMail getMtMail(MailBasicSettingBo bo) {
        QueryWrapper<MtMail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtMail::getCenterId, bo.getCenterId());
        queryWrapper.lambda().eq(MtMail::getMailNo, bo.getMailNo());
        queryWrapper.lambda().eq(MtMail::getFDel, 0);
        return mtMailMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateSqlserverOrderList(MailBasicSettingBo bo) {
        Date date = new Date();
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtCenterstatus::getCenterId, bo.getCenterId());
        MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
        if (mtCenterstatus.getVendorId() != null) {
            MtMail mtMail = getMtMail(bo);
            if (mtMail != null && mtMail.getSelGroup() != null && !mtMail.getSelGroup().equals("")) {
                bo.setSelGroup(mtMail.getSelGroup());
                MailBasicSettingVo mailBasicSettingVos = mailBasicSettingMapper.getMailNo(bo);
                if (mailBasicSettingVos != null) {
                    QueryWrapper<ShTPcOrderList> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().eq(ShTPcOrderList::getBranchCd, bo.getStoreId());
                    queryWrapper1.lambda().eq(ShTPcOrderList::getMailNo, mailBasicSettingVos.getMailNo());
                    queryWrapper1.lambda().eq(ShTPcOrderList::getPcCode, mtCenterstatus.getVendorId());
                    List<ShTPcOrderList> shTPcOrderList = shTPcOrderListMapper.selectList(queryWrapper1);
                    shTPcOrderListMapper.mailBasicInsertHistory(bo.getStoreId(), mailBasicSettingVos.getMailNo(), mtCenterstatus.getVendorId());
                    if (!shTPcOrderList.isEmpty()) {
                        for (ShTPcOrderList tPcOrderList : shTPcOrderList) {
                            tPcOrderList.setMailNo(bo.getMailNo());
                        }
                        List<List<ShTPcOrderList>> list = DivideEquallyUtils.fixedGrouping(shTPcOrderList, 250);
                        for (List<ShTPcOrderList> list1 : list) {
                            shTPcOrderListMapper.updateSqlServerOrderList(date, list1, mailBasicSettingVos.getMailNo(), tokenTakeApart.takeDecryptedUserId());
                        }
                    }
                }
            }
        } else {
            throw new SysBusinessException("入力したセンターが不正です", HttpStatus.HTTP_OK, SysConstantInfo.STORE_CD_EXIST_CODE);
        }
    }

    @Override
    public void updateSqlserverOrderQty(MailBasicSettingBo bo) {
        Date date = new Date();
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtCenterstatus::getCenterId, bo.getCenterId());
        MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
        //
        QueryWrapper<MtMailcontrol> mailcontrolQueryWrapper = new QueryWrapper<>();
        mailcontrolQueryWrapper.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
        mailcontrolQueryWrapper.lambda().eq(MtMailcontrol::getMailNo, bo.getMailNo());
        MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(mailcontrolQueryWrapper);
        if (mtCenterstatus != null && mtCenterstatus.getVendorId() != null && mtMailcontrol != null) {
            MtMail mtMail = getMtMail(bo);
            if (mtMail != null && mtMail.getSelGroup() != null && !mtMail.getSelGroup().equals("")) {
                bo.setSelGroup(mtMail.getSelGroup());
                MailBasicSettingVo mailBasicSettingVos = mailBasicSettingMapper.getMailNo(bo);
                if (mailBasicSettingVos != null) {
                    QueryWrapper<shTOrderQtyTest> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().eq(shTOrderQtyTest::getBranchCD, bo.getStoreId());
                    queryWrapper1.lambda().eq(shTOrderQtyTest::getSupplierCD, mtCenterstatus.getVendorId());
                    queryWrapper1.lambda().gt(shTOrderQtyTest::getDeliveryDate, df.format(mtMailcontrol.getDlvschedDate()));
                    queryWrapper1.lambda().eq(shTOrderQtyTest::getBin, mailBasicSettingVos.getMailNo());
                    List<shTOrderQtyTest> shTOrderQtyTests = shTOrderQtyTestMapper.selectList(queryWrapper1);
                    if (!shTOrderQtyTests.isEmpty()) {
                        for (shTOrderQtyTest shTPcOrderList : shTOrderQtyTests) {
                            shTPcOrderList.setBin(bo.getMailNo());
                        }
                        List<List<shTOrderQtyTest>> list = DivideEquallyUtils.fixedGrouping(shTOrderQtyTests, 250);
                        for (List<shTOrderQtyTest> list1 : list) {
                            shTPcOrderListMapper.updateSqlServerOrderQty(date, list1, mailBasicSettingVos.getMailNo(), tokenTakeApart.takeDecryptedUserId());
                        }
                    }
                }
            }
        } else {
            throw new SysBusinessException("入力したセンターが不正です", HttpStatus.HTTP_OK, SysConstantInfo.STORE_CD_EXIST_CODE);
        }
    }

    @Override
    public List<BranchListVo> queryCenterName() {
        QueryWrapper<Branches> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branchtype", 2);
        return branchesMapper.selectVoList(queryWrapper, BranchListVo.class);
    }
}
