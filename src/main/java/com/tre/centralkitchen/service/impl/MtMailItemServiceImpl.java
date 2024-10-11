package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.common.constant.FileTypeConstants;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.FixAmountResultModifyConstants;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.excel.GetResultListReadListener;
import com.tre.centralkitchen.common.utils.HeadContentCellStyle;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.common.utils.StringUtil;
import com.tre.centralkitchen.domain.bo.system.MtMailItemBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.common.BranchListVo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.system.MtMailItemVo;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.IMtMailItemService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 便設定個別マスタ
 */
@RequiredArgsConstructor
@Service
public class MtMailItemServiceImpl implements IMtMailItemService {
    private final MtMailitemMapper mtMailItemMapper;
    private final MtMailcontrolMapper mtMailcontrolMapper;
    private final TrProduceplanMapper trProduceplanMapper;
    private final ShTPcOrderListMapper shTPcOrderListMapper;
    private final MtCenterstatusMapper mtCenterstatusMapper;
    private final CommonMapper commonMapper;
    private final TokenTakeApart tokenTakeApart;
    private final MasterService masterService;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final MtMailItemHistoryMapper mtMailItemHistoryMapper;
    private final shTOrderQtyTestMapper shTOrderQtyTestMapper;
    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public TableDataInfo<MtMailItemVo> queryList(MtMailItemBo bo, PageQuery pageQuery) {
        String list = bo.getMailNo();
        if (list != null && !list.isEmpty()) {
            List<String> list1 = Arrays.asList(list.split(","));
            bo.setMailNoList(list1);
        }
        return TableDataInfo.build(mtMailItemMapper.queryList(pageQuery.build(), bo));
    }

    @Override
    public void downloadCSV(MtMailItemBo bo, HttpServletResponse response) {
        String list = bo.getMailNo();
        if (list != null && !list.isEmpty()) {
            List<String> list1 = Arrays.asList(list.split(","));
            bo.setMailNoList(list1);
        }
        if (bo.getCenterId() == null) {
            String[] header = SimpleCsvTableUtils.getHeaders(MailConstants.MT_MAIL_ITEM_CSV_HEADER, StringConstants.COMMA);
            String fileName = SimpleCsvTableUtils.getFileName(MailConstants.MT_MAIL_ITEM_CSV_NAME, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
            SimpleCsvTableUtils.printBeansToRespStream(response, fileName, new ArrayList<>(), MtMailItemVo.class, header);
        } else {
            List<MtMailItemVo> mtMailItemVos = mtMailItemMapper.queryList(bo);
            SimpleCsvTableUtils.easyCsvExport(response, MailConstants.MT_MAIL_ITEM_CSV_NAME, mtMailItemVos, MtMailItemVo.class);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(MtMailItemBo bo) {
        //取得納品予定日
        QueryWrapper<MtMailcontrol> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
        queryWrapper1.lambda().eq(MtMailcontrol::getMailNo, Integer.valueOf(bo.getBeforeMailNo()));
        queryWrapper1.lambda().eq(MtMailcontrol::getFDel, 0);
        MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper1);
        //center _id，mail_no，item_id、dlvsched _date抽気データ
        if (!ObjectUtil.isNull(mtMailcontrol)) {
            QueryWrapper<TrProducePlan> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.lambda().gt(TrProducePlan::getDlvschedDate, mtMailcontrol.getDlvschedDate());
            queryWrapper2.lambda().eq(TrProducePlan::getCenterId, bo.getCenterId());
            queryWrapper2.lambda().eq(TrProducePlan::getMailNo, Integer.valueOf(bo.getBeforeMailNo()));
            queryWrapper2.lambda().eq(TrProducePlan::getItemId, bo.getItemId());
            queryWrapper2.lambda().eq(TrProducePlan::getStoreId, bo.getStoreId());
            queryWrapper2.lambda().eq(TrProducePlan::getFDel, 0);
            List<TrProducePlan> trProducePlan = trProduceplanMapper.selectList(queryWrapper2);
            //循環置換
            for (TrProducePlan trProducePlan1 : trProducePlan) {
                trProducePlan1.setMailNo(Integer.valueOf(bo.getMailNo()));
                trProducePlan1.setUpdFuncId(bo.getUpdFuncId());
                trProducePlan1.setUpdOpeId(bo.getUpdOpeId());
                QueryWrapper<TrProducePlan> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.lambda().eq(TrProducePlan::getDlvschedDate, trProducePlan1.getDlvschedDate());
                queryWrapper3.lambda().eq(TrProducePlan::getCenterId, trProducePlan1.getCenterId());
                queryWrapper3.lambda().eq(TrProducePlan::getMailNo, Integer.valueOf(bo.getBeforeMailNo()));
                queryWrapper3.lambda().eq(TrProducePlan::getItemId, trProducePlan1.getItemId());
                queryWrapper3.lambda().eq(TrProducePlan::getStoreId, trProducePlan1.getStoreId());
                queryWrapper3.lambda().eq(TrProducePlan::getFDel, 0);
                trProduceplanMapper.update(trProducePlan1, queryWrapper3);
            }
        }
        //mtMailテーブルデータの更新
        MtMailItem update = BeanUtil.toBean(bo, MtMailItem.class);
        QueryWrapper<MtMailItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtMailItem::getCenterId, update.getCenterId());
        queryWrapper.lambda().eq(MtMailItem::getItemId, update.getItemId());
        queryWrapper.lambda().eq(MtMailItem::getStoreId, update.getStoreId());
        queryWrapper.lambda().eq(MtMailItem::getFDel, 0);
        MtMailItem mtMailItem = mtMailItemMapper.selectOne(queryWrapper);
        mtMailItem.setMailNo(update.getMailNo());
        mtMailItem.setMemo(update.getMemo());
        mtMailItem.setUpdFuncId(bo.getUpdFuncId());
        mtMailItem.setUpdOpeId(bo.getUpdOpeId());
        mtMailItemMapper.update(mtMailItem, queryWrapper);
        MtMailItemHistory history = new MtMailItemHistory();
        BeanUtil.copyProperties(mtMailItem, history);
        history.setUpdateTypeId(2);
        return mtMailItemHistoryMapper.insert(history) > 0;
    }

    @Override
    public void updateSqlserverPcOrderList(MtMailItemBo bo) {
        Date date = new Date();
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtCenterstatus::getCenterId, bo.getCenterId());
        queryWrapper.lambda().eq(MtCenterstatus::getFDel, 0);
        MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
        if (!ObjectUtil.isNull(mtCenterstatus)) {
            QueryWrapper<ShTPcOrderList> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(ShTPcOrderList::getBranchCd, bo.getStoreId());
            queryWrapper1.lambda().eq(ShTPcOrderList::getItemId, bo.getItemId());
            queryWrapper1.lambda().eq(ShTPcOrderList::getMailNo, Integer.parseInt(bo.getBeforeMailNo()));
            queryWrapper1.lambda().eq(ShTPcOrderList::getPcCode, mtCenterstatus.getVendorId());
            List<ShTPcOrderList> shTPcOrderList = shTPcOrderListMapper.selectList(queryWrapper1);
            shTPcOrderListMapper.mailItemInsertHistory(bo.getStoreId(), bo.getItemId(), Integer.parseInt(bo.getBeforeMailNo()), mtCenterstatus.getVendorId());
            for (ShTPcOrderList tPcOrderList : shTPcOrderList) {
                tPcOrderList.setMailNo(Integer.valueOf(bo.getMailNo()));
                tPcOrderList.setUpdateUser(Integer.valueOf(tokenTakeApart.takeDecryptedUserId()));
                tPcOrderList.setUpdateDate(date);
                QueryWrapper<ShTPcOrderList> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("BranchCD", tPcOrderList.getBranchCd());
                queryWrapper2.eq("JAN", tPcOrderList.getItemId());
                queryWrapper2.eq("Bin", bo.getBeforeMailNo());
                queryWrapper2.eq("ListNo", tPcOrderList.getListNo());
                queryWrapper2.eq("PcCode", tPcOrderList.getPcCode());
                shTPcOrderListMapper.update(tPcOrderList, queryWrapper2);
            }
        }
    }

    @Override
    public void updateSqlserverOrderQty(MtMailItemBo bo) {
        Date date = new Date();
        //取得納品予定日
        QueryWrapper<MtMailcontrol> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
        queryWrapper1.lambda().eq(MtMailcontrol::getMailNo, Integer.valueOf(bo.getBeforeMailNo()));
        queryWrapper1.lambda().eq(MtMailcontrol::getFDel, 0);
        MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper1);
        //取得ベンダーID
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtCenterstatus::getCenterId, bo.getCenterId());
        queryWrapper.lambda().eq(MtCenterstatus::getFDel, 0);
        MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
        if (!ObjectUtil.isNull(mtMailcontrol) && !ObjectUtil.isNull(mtCenterstatus)) {
            QueryWrapper<shTOrderQtyTest> shTOrderQtyTestQueryWrapper = new QueryWrapper<>();
            shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getProdJAN, bo.getItemId());
            shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getBranchCD, bo.getStoreId());
            shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getSupplierCD, mtCenterstatus.getVendorId());
            shTOrderQtyTestQueryWrapper.lambda().gt(shTOrderQtyTest::getDeliveryDate, df.format(mtMailcontrol.getDlvschedDate()));
            shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getBin, Integer.parseInt(bo.getBeforeMailNo()));
            List<shTOrderQtyTest> shTOrderQtyTests = shTOrderQtyTestMapper.selectList(shTOrderQtyTestQueryWrapper);
            for (shTOrderQtyTest shTOrderQtyTest : shTOrderQtyTests) {
                Integer mailNo = shTOrderQtyTest.getBin();
                shTOrderQtyTest.setBin(Integer.parseInt(bo.getMailNo()));
                shTOrderQtyTest.setUserCD(Integer.parseInt(tokenTakeApart.takeDecryptedUserId()));
                shTOrderQtyTest.setUpdateTime(date);
                QueryWrapper<shTOrderQtyTest> orderQtyTestQueryWrapper = new QueryWrapper<>();
                orderQtyTestQueryWrapper.eq("ProdJAN", shTOrderQtyTest.getProdJAN());
                orderQtyTestQueryWrapper.eq("BranchCD", shTOrderQtyTest.getBranchCD());
                orderQtyTestQueryWrapper.eq("SupplierCD", shTOrderQtyTest.getSupplierCD());
                orderQtyTestQueryWrapper.eq("DeliveryDate", shTOrderQtyTest.getDeliveryDate());
                orderQtyTestQueryWrapper.eq("Bin", mailNo);
                shTOrderQtyTestMapper.update(shTOrderQtyTest, orderQtyTestQueryWrapper);
            }
        }
    }

    public void updateSqlserverPcOrderList(List<MtMailItemVo> mtMailItemVos, Integer centerId) {
        Date date = new Date();
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtCenterstatus::getCenterId, centerId);
        queryWrapper.lambda().eq(MtCenterstatus::getFDel, 0);
        MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
        for (MtMailItemVo mtMailItemVo : mtMailItemVos) {
            QueryWrapper<ShTPcOrderList> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(ShTPcOrderList::getBranchCd, Integer.parseInt(mtMailItemVo.getStoreId()));
            queryWrapper1.lambda().eq(ShTPcOrderList::getItemId, mtMailItemVo.getItemId());
            queryWrapper1.lambda().eq(ShTPcOrderList::getPcCode, mtCenterstatus.getVendorId());
            List<ShTPcOrderList> shTPcOrderList = shTPcOrderListMapper.selectList(queryWrapper1);
            shTPcOrderListMapper.mailItemFmtInsertHistory(mtMailItemVo, mtCenterstatus.getVendorId());
            for (ShTPcOrderList tPcOrderList : shTPcOrderList) {
                Integer mailNo = tPcOrderList.getMailNo();
                tPcOrderList.setMailNo(Integer.valueOf(mtMailItemVo.getMailNo()));
                tPcOrderList.setUpdateUser(Integer.valueOf(tokenTakeApart.takeDecryptedUserId()));
                tPcOrderList.setUpdateDate(date);
                QueryWrapper<ShTPcOrderList> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("BranchCD", tPcOrderList.getBranchCd());
                queryWrapper2.eq("JAN", tPcOrderList.getItemId());
                queryWrapper2.eq("Bin", mailNo);
                queryWrapper2.eq("ListNo", tPcOrderList.getListNo());
                queryWrapper2.eq("PcCode", tPcOrderList.getPcCode());
                shTPcOrderListMapper.update(tPcOrderList, queryWrapper2);
            }
        }
    }

    @Override
    public void deleteSqlserverOrderList(MtMailItemBo bo) {
        Date date = new Date();
        List<MtMailItemVo> mtMailItemVo = mtMailItemMapper.selectBasicMailNo(bo);
        if (mtMailItemVo != null && !mtMailItemVo.isEmpty()) {
            QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MtCenterstatus::getCenterId, bo.getCenterId());
            queryWrapper.lambda().eq(MtCenterstatus::getFDel, 0);
            MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
            QueryWrapper<ShTPcOrderList> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(ShTPcOrderList::getBranchCd, bo.getStoreId());
            queryWrapper1.lambda().eq(ShTPcOrderList::getItemId, bo.getItemId());
            queryWrapper1.lambda().eq(ShTPcOrderList::getMailNo, Integer.parseInt(bo.getMailNo()));
            queryWrapper1.lambda().eq(ShTPcOrderList::getPcCode, mtCenterstatus.getVendorId());
            List<ShTPcOrderList> shTPcOrderList = shTPcOrderListMapper.selectList(queryWrapper1);
            shTPcOrderListMapper.mailItemInsertHistory(bo.getStoreId(), bo.getItemId(), Integer.parseInt(bo.getMailNo()), mtCenterstatus.getVendorId());
            for (ShTPcOrderList tPcOrderList : shTPcOrderList) {
                tPcOrderList.setMailNo(Integer.valueOf(mtMailItemVo.get(0).getMailNo()));
                tPcOrderList.setUpdateUser(Integer.valueOf(tokenTakeApart.takeDecryptedUserId()));
                tPcOrderList.setUpdateDate(date);
                QueryWrapper<ShTPcOrderList> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("BranchCD", tPcOrderList.getBranchCd());
                queryWrapper2.eq("JAN", tPcOrderList.getItemId());
                queryWrapper2.eq("Bin", bo.getMailNo());
                queryWrapper2.eq("ListNo", tPcOrderList.getListNo());
                queryWrapper2.eq("PcCode", tPcOrderList.getPcCode());
                shTPcOrderListMapper.update(tPcOrderList, queryWrapper2);
            }
        }
    }

    @Override
    public void deleteSqlserverOrderQty(MtMailItemBo bo) {
        Date date = new Date();
        //basicMailNo
        List<MtMailItemVo> mtMailItemVo = mtMailItemMapper.selectBasicMailNo(bo);
        if (mtMailItemVo != null && !mtMailItemVo.isEmpty()) {
            //取得納品予定日
            QueryWrapper<MtMailcontrol> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
            queryWrapper1.lambda().eq(MtMailcontrol::getMailNo, Integer.parseInt(bo.getMailNo()));
            queryWrapper1.lambda().eq(MtMailcontrol::getFDel, 0);
            MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper1);
            //取得ベンダーID
            QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MtCenterstatus::getCenterId, bo.getCenterId());
            queryWrapper.lambda().eq(MtCenterstatus::getFDel, 0);
            MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
            if (!ObjectUtil.isNull(mtMailcontrol) && !ObjectUtil.isNull(mtCenterstatus)) {
                QueryWrapper<shTOrderQtyTest> shTOrderQtyTestQueryWrapper = new QueryWrapper<>();
                shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getProdJAN, bo.getItemId());
                shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getBranchCD, bo.getStoreId());
                shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getSupplierCD, mtCenterstatus.getVendorId());
                shTOrderQtyTestQueryWrapper.lambda().gt(shTOrderQtyTest::getDeliveryDate, df.format(mtMailcontrol.getDlvschedDate()));
                shTOrderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getBin, Integer.parseInt(bo.getMailNo()));
                List<shTOrderQtyTest> shTOrderQtyTests = shTOrderQtyTestMapper.selectList(shTOrderQtyTestQueryWrapper);
                for (shTOrderQtyTest shTOrderQtyTest : shTOrderQtyTests) {
                    Integer mailNo = shTOrderQtyTest.getBin();
                    shTOrderQtyTest.setBin(Integer.parseInt(mtMailItemVo.get(0).getMailNo()));
                    shTOrderQtyTest.setUserCD(Integer.parseInt(tokenTakeApart.takeDecryptedUserId()));
                    shTOrderQtyTest.setUpdateTime(date);
                    QueryWrapper<shTOrderQtyTest> orderQtyTestQueryWrapper = new QueryWrapper<>();
                    orderQtyTestQueryWrapper.eq("ProdJAN", shTOrderQtyTest.getProdJAN());
                    orderQtyTestQueryWrapper.eq("BranchCD", shTOrderQtyTest.getBranchCD());
                    orderQtyTestQueryWrapper.eq("SupplierCD", shTOrderQtyTest.getSupplierCD());
                    orderQtyTestQueryWrapper.eq("DeliveryDate", shTOrderQtyTest.getDeliveryDate());
                    orderQtyTestQueryWrapper.eq("Bin", mailNo);
                    shTOrderQtyTestMapper.update(shTOrderQtyTest, orderQtyTestQueryWrapper);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(MtMailItemBo bo, Boolean isValid) {
        //basicMailNo
        List<MtMailItemVo> mtMailItemVo = mtMailItemMapper.selectBasicMailNo(bo);
        if (mtMailItemVo != null && !mtMailItemVo.isEmpty()) {
            //取得納品予定日
            QueryWrapper<MtMailcontrol> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(MtMailcontrol::getCenterId, bo.getCenterId());
            queryWrapper1.lambda().eq(MtMailcontrol::getMailNo, Integer.parseInt(bo.getMailNo()));
            queryWrapper1.lambda().eq(MtMailcontrol::getFDel, 0);
            MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper1);
            if (!ObjectUtil.isNull(mtMailcontrol)) {
                QueryWrapper<TrProducePlan> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().gt(TrProducePlan::getDlvschedDate, mtMailcontrol.getDlvschedDate());
                queryWrapper2.lambda().eq(TrProducePlan::getCenterId, bo.getCenterId());
                queryWrapper2.lambda().eq(TrProducePlan::getMailNo, Integer.parseInt(bo.getMailNo()));
                queryWrapper2.lambda().eq(TrProducePlan::getItemId, bo.getItemId());
                queryWrapper2.lambda().eq(TrProducePlan::getStoreId, bo.getStoreId());
                queryWrapper2.lambda().eq(TrProducePlan::getFDel, 0);
                List<TrProducePlan> trProducePlan = trProduceplanMapper.selectList(queryWrapper2);
                //循環置換
                for (TrProducePlan trProducePlan1 : trProducePlan) {
                    trProducePlan1.setMailNo(Integer.valueOf(mtMailItemVo.get(0).getMailNo()));
                    trProducePlan1.setUpdFuncId(bo.getUpdFuncId());
                    trProducePlan1.setUpdOpeId(bo.getUpdOpeId());
                    QueryWrapper<TrProducePlan> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.lambda().eq(TrProducePlan::getDlvschedDate, trProducePlan1.getDlvschedDate());
                    queryWrapper3.lambda().eq(TrProducePlan::getCenterId, trProducePlan1.getCenterId());
                    queryWrapper3.lambda().eq(TrProducePlan::getMailNo, Integer.parseInt(bo.getMailNo()));
                    queryWrapper3.lambda().eq(TrProducePlan::getItemId, trProducePlan1.getItemId());
                    queryWrapper3.lambda().eq(TrProducePlan::getStoreId, trProducePlan1.getStoreId());
                    queryWrapper3.lambda().eq(TrProducePlan::getFDel, 0);
                    trProduceplanMapper.update(trProducePlan1, queryWrapper3);
                }
            }
        }
        MtMailItem update = BeanUtil.toBean(bo, MtMailItem.class);
        QueryWrapper<MtMailItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtMailItem::getCenterId, update.getCenterId());
        queryWrapper.lambda().eq(MtMailItem::getItemId, update.getItemId());
        queryWrapper.lambda().eq(MtMailItem::getStoreId, update.getStoreId());
        queryWrapper.lambda().eq(MtMailItem::getFDel, 0);
        MtMailItem mtMailItem = mtMailItemMapper.selectOne(queryWrapper);
        MtMailItemHistory history = new MtMailItemHistory();
        BeanUtil.copyProperties(mtMailItem, history);
        history.setUpdateTypeId(3);
        history.setFDel(1);
        mtMailItemHistoryMapper.insert(history);
        return mtMailItemMapper.delete(queryWrapper) > 0;
    }

    @Override
    public MtMailItemVo queryByList(MtMailItemBo bo) {
        MtMailItemVo mtMailItemVo = mtMailItemMapper.queryByList(bo);
        mtMailItemVo.setBeforeMailNo(mtMailItemVo.getMailNo());
        return mtMailItemVo;
    }

    @Override
    public FileBackErrorVo fmtImport(UploadBo bo, MultipartFile file, HttpServletResponse response) throws Exception {
        FileBackErrorVo fileBackErrorVo = new FileBackErrorVo();
        List<MtMailItemVo> resList = new ArrayList<>();
        GetResultListReadListener<MtMailItemVo> listener = new GetResultListReadListener<>(resList, MtMailItemVo.class);
        EasyExcelFactory.read(file.getInputStream(), MtMailItemVo.class, listener).sheet().headRowNumber(2).doRead();
        Set<Boolean> checkRes = new HashSet<>();
        if (!resList.isEmpty()) {
            Set<String> centerIdSet = resList.stream().map(MtMailItemVo::getCenterId).filter(Objects::nonNull).collect(Collectors.toSet());
            resList.forEach(tmp -> {
                checkRes.add(checkUploadFmt(tmp, bo.getCenterId(), centerIdSet));
            });
        } else {
            throw new SysBusinessException(SysConstantInfo.FILE_DATA_EMPTY_MSG);
        }
        checkExcelWithDatabase(resList, checkRes, bo.getCenterId());
        if (!checkRes.isEmpty() && checkRes.contains(false)) {
            fileBackErrorVo.setFileError(downloadErrorFile(resList));
        } else {
            for (MtMailItemVo mtMailItemVo : resList) {
                if (mtMailItemVo.getMemo() == null) {
                    mtMailItemVo.setMemo("");
                }
            }
            updateDataBase(resList);
            updateSqlserverPcOrderList(resList, Integer.valueOf(resList.get(0).getCenterId()));
            updateSqlserverOrderQty(resList, Integer.valueOf(resList.get(0).getCenterId()));
            mtMailItemMapper.insertFmt(bo.getCenterId(), tokenTakeApart.takeDecryptedUserId(), resList, bo.getInsFuncId(), bo.getInsOpeId());
            mtMailItemHistoryMapper.insertFmtHistory(new Date(), bo.getCenterId(), tokenTakeApart.takeDecryptedUserId(), resList, bo.getInsFuncId(), bo.getInsOpeId());
        }
        fileBackErrorVo.setOtherFlag(checkRes.contains(false));
        fileBackErrorVo.setPriceFlag(false);
        fileBackErrorVo.setDateFlag(false);
        return fileBackErrorVo;
    }

    public void updateSqlserverOrderQty(List<MtMailItemVo> mtMailItemVos, Integer centerId) {
        Date date = new Date();
        //取得ベンダーID
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtCenterstatus::getCenterId, centerId);
        queryWrapper.lambda().eq(MtCenterstatus::getFDel, 0);
        MtCenterstatus mtCenterstatus = mtCenterstatusMapper.selectOne(queryWrapper);
        for (MtMailItemVo mtMailItemVo : mtMailItemVos) {
            //取得納品予定日
            QueryWrapper<MtMailcontrol> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.lambda().eq(MtMailcontrol::getCenterId, Integer.parseInt(mtMailItemVo.getCenterId()));
            queryWrapper1.lambda().eq(MtMailcontrol::getMailNo, Integer.valueOf(mtMailItemVo.getMailNo()));
            queryWrapper1.lambda().eq(MtMailcontrol::getFDel, 0);
            MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper1);
            QueryWrapper<shTOrderQtyTest> orderQtyTestQueryWrapper = new QueryWrapper<>();
            orderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getProdJAN, mtMailItemVo.getItemId());
            orderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getBranchCD, Integer.parseInt(mtMailItemVo.getStoreId()));
            orderQtyTestQueryWrapper.lambda().eq(shTOrderQtyTest::getSupplierCD, mtCenterstatus.getVendorId());
            orderQtyTestQueryWrapper.lambda().gt(shTOrderQtyTest::getDeliveryDate, df.format(mtMailcontrol.getDlvschedDate()));
            List<shTOrderQtyTest> shTOrderQtyTests = shTOrderQtyTestMapper.selectList(orderQtyTestQueryWrapper);
            for (shTOrderQtyTest shTOrderQtyTest : shTOrderQtyTests) {
                Integer mailNo = shTOrderQtyTest.getBin();
                shTOrderQtyTest.setBin(Integer.parseInt(mtMailItemVo.getMailNo()));
                shTOrderQtyTest.setUserCD(Integer.parseInt(tokenTakeApart.takeDecryptedUserId()));
                shTOrderQtyTest.setUpdateTime(date);
                QueryWrapper<shTOrderQtyTest> qtyTestQueryWrapper = new QueryWrapper<>();
                qtyTestQueryWrapper.eq("ProdJAN", shTOrderQtyTest.getProdJAN());
                qtyTestQueryWrapper.eq("BranchCD", shTOrderQtyTest.getBranchCD());
                qtyTestQueryWrapper.eq("SupplierCD", shTOrderQtyTest.getSupplierCD());
                qtyTestQueryWrapper.eq("DeliveryDate", shTOrderQtyTest.getDeliveryDate());
                qtyTestQueryWrapper.eq("Bin", mailNo);
                shTOrderQtyTestMapper.update(shTOrderQtyTest, qtyTestQueryWrapper);
            }
        }
    }

    private String downloadErrorFile(List<MtMailItemVo> resList) {
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = StrFormatter.format(SysConstantInfo.FMT_FILE_NAME_WITH_TIME_AND_ERROR_MSG, MailConstants.MT_MAIL_ITEM_CSV_NAME, curTime, FileTypeConstants.XLSX);
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), MtMailItemVo.class).withTemplate(simpleFmtTableUtils.getFmtFileInputStream(MailConstants.BUSINESS_NAME))
                .registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                ).needHead(false).sheet().doWrite(resList);
        return fileName;
    }

    private void updateDataBase(List<MtMailItemVo> mtMailItemVos) {
        for (MtMailItemVo mtMailItemVo : mtMailItemVos) {
            QueryWrapper<MtMailcontrol> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MtMailcontrol::getCenterId, Integer.parseInt(mtMailItemVo.getCenterId()));
            queryWrapper.lambda().eq(MtMailcontrol::getMailNo, Integer.parseInt(mtMailItemVo.getMailNo()));
            queryWrapper.lambda().eq(MtMailcontrol::getFDel, 0);
            MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper);
            if (mtMailcontrol != null) {
                QueryWrapper<TrProducePlan> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.lambda().gt(TrProducePlan::getDlvschedDate, mtMailcontrol.getDlvschedDate());
                queryWrapper2.lambda().eq(TrProducePlan::getCenterId, Integer.parseInt(mtMailItemVo.getCenterId()));
                queryWrapper2.lambda().eq(TrProducePlan::getItemId, mtMailItemVo.getItemId());
                queryWrapper2.lambda().eq(TrProducePlan::getStoreId, Integer.parseInt(mtMailItemVo.getStoreId()));
                queryWrapper2.lambda().eq(TrProducePlan::getFDel, 0);
                List<TrProducePlan> trProducePlan = trProduceplanMapper.selectList(queryWrapper2);
                //循環置換
                for (TrProducePlan trProducePlan1 : trProducePlan) {
                    Integer mailNo = trProducePlan1.getMailNo();
                    trProducePlan1.setMailNo(Integer.valueOf(mtMailItemVo.getMailNo()));
                    QueryWrapper<TrProducePlan> wrapper = new QueryWrapper<>();
                    wrapper.lambda().eq(TrProducePlan::getDlvschedDate, trProducePlan1.getDlvschedDate());
                    wrapper.lambda().eq(TrProducePlan::getCenterId, trProducePlan1.getCenterId());
                    wrapper.lambda().eq(TrProducePlan::getMailNo, mailNo);
                    wrapper.lambda().eq(TrProducePlan::getItemId, trProducePlan1.getItemId());
                    wrapper.lambda().eq(TrProducePlan::getStoreId, trProducePlan1.getStoreId());
                    wrapper.lambda().eq(TrProducePlan::getFDel, 0);
                    trProduceplanMapper.update(trProducePlan1, wrapper);
                }
            }
        }
    }

    @Override
    public JSONObject recordImport(List<MtMailItemVo> list) {
        JSONObject res = new JSONObject(2);
        if (!list.isEmpty()) {
            list.forEach(tmp -> {
                if (StringUtil.isBlank(tmp.getItemId())) {
                    throw new SysBusinessException(SysConstantInfo.JAN_NOT_EMPTY, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
                } else if (StringUtil.isBlank(tmp.getStoreId())) {
                    throw new SysBusinessException(SysConstantInfo.STORE_INPUT_NOT_EMPTY, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
                } else if (StringUtil.isBlank(tmp.getMailNo())) {
                    throw new SysBusinessException(SysConstantInfo.MAIL_NO_NOT_EMPTY, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
                } else if (tmp.getMemo().length() > 50) {
                    throw new SysBusinessException(SysConstantInfo.MEMO_MAX, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
                }
                if (masterService.getItemName(tmp.getItemId()) == null) {
                    throw new SysBusinessException(SysConstantInfo.JAN_NON_EXISTENT, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
                }
                if (masterService.getStoreName(Integer.parseInt(tmp.getCenterId()), Integer.valueOf(tmp.getStoreId())).isEmpty()) {
                    throw new SysBusinessException(SysConstantInfo.STORE_NON_EXISTENT, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
                }
            });
            updateDataBase(list);
            updateSqlserverPcOrderList(list, Integer.valueOf(list.get(0).getCenterId()));
            updateSqlserverOrderQty(list, Integer.valueOf(list.get(0).getCenterId()));
            mtMailItemMapper.recordImport(tokenTakeApart.takeDecryptedUserId(), list, FunType.MASTER.getCode(), OpeType.MAIL.getCode());
            mtMailItemHistoryMapper.recordImportHistory(new Date(), tokenTakeApart.takeDecryptedUserId(), list, FunType.MASTER.getCode(), OpeType.MAIL.getCode());
        }
        res.put("flag", true);
        return res;
    }

    @Override
    public Boolean recordImportCheck(String itemId, Integer storeId, Integer centerId) {
        Integer integer = mtMailItemMapper.recordImportCheck(itemId, storeId, centerId);
        return integer != 0;
    }

    private void setVoErrorMsg(MtMailItemVo bo, String errorMsg, String fieldName) {
        if (!bo.getErrorMsg().contains(StrFormatter.format(errorMsg, StringConstants.BLANK, fieldName))) {
            bo.setErrorMsg(StrFormatter.format(errorMsg, bo.getErrorMsg(), fieldName));
        }
    }

    private boolean checkUploadFmt(MtMailItemVo vo, Integer centerId, Set<String> centerIdSet) {
        Set<Boolean> checkResult = new HashSet<>();
        vo.setErrorMsg(FixAmountResultModifyConstants.ERROR_MSG_NOT_EXIST_INITIAL);
        checkResult.add(checkCenterId(vo, vo.getCenterId(), centerId, centerIdSet));
        checkResult.add(checkNumberNum(vo, vo.getMailNo(), MailConstants.EXCEL_COL_MAIL_NO, 16));
        checkResult.add(checkNumberNum(vo, vo.getStoreId(), MailConstants.EXCEL_COL_STORE_ID, 32));
        checkResult.add(checkNumberNum(vo, vo.getItemId(), MailConstants.EXCEL_COL_ITEM_ID, 13));
        if (vo.getMemo() != null) {
            checkResult.add(checkString(vo, vo.getMemo()));
        }
        if (vo.getItemId() != null && vo.getStoreId() != null) {
            try {
                Integer integer = mtMailItemMapper.recordImportCheck(vo.getItemId(), Integer.valueOf(vo.getStoreId()), Integer.valueOf(vo.getCenterId()));
                if (integer == 0) {
                    checkResult.add(false);
                    setVoErrorMsg(vo, MailConstants.CHECK_ITEM_STORE + "、{}");
                }
            } catch (Exception e) {
                setVoErrorMsg(vo, MailConstants.CHECK_ITEM_STORE + "、{}");
            }
        }
        if (vo.getErrorMsg().contains(FixAmountResultModifyConstants.ERROR_MSG_NOT_EXIST_INITIAL)) {
            vo.setErrorMsg(vo.getErrorMsg().replace(FixAmountResultModifyConstants.ERROR_MSG_NOT_EXIST_INITIAL, ""));
        } else {
            vo.setErrorMsg(vo.getErrorMsg().replace("、{}）", "）"));
        }
        return !checkResult.contains(false);
    }

    private boolean checkCenterId(MtMailItemVo vo, String parameter, Integer centerId, Set<String> centerIdSet) {
        if (centerIdSet.isEmpty() || ObjectUtil.isNull(parameter)) {
            setVoErrorMsg(vo, MailConstants.ERROR_MSG_NOT_NULL, StringConstants.BLANK);
            return false;
        }
        try {
            if (parameter.matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
                if (parameter.equals(centerId.toString())) {
                    return true;
                } else {
                    setVoErrorMsg(vo, MailConstants.ERROR_MSG_NOT_MATCH_02, MailConstants.EXCEL_COL_CENTER_ID);
                    return false;
                }
            } else {
                setVoErrorMsg(vo, MailConstants.EXCEL_COL_CENTER_ID + "、{}");
                return false;
            }
        } catch (Exception e) {
            setVoErrorMsg(vo, MailConstants.EXCEL_COL_CENTER_ID + "、{}");
            return false;
        }
    }

    private void setVoErrorMsg(MtMailItemVo vo, String fieldName) {
        if (!vo.getErrorMsg().contains(StrFormatter.format(vo.getErrorMsg(), fieldName))) {
            vo.setErrorMsg(StrFormatter.format(vo.getErrorMsg(), fieldName));
        }
    }

    private boolean checkNumberNum(MtMailItemVo bo, String parameter, String fieldName, int len) {
        if (parameter == null) {
            setVoErrorMsg(bo, MailConstants.ERROR_MSG_NOT_NULL, StringConstants.BLANK);
            return false;
        }
        try {
            if (parameter.length() <= len) {
                if (parameter.matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
                    return true;
                } else {
                    setVoErrorMsg(bo, fieldName + "、{}");
                    return false;
                }
            } else {
                setVoErrorMsg(bo, fieldName + "、{}");
                return false;
            }
        } catch (Exception e) {
            setVoErrorMsg(bo, fieldName + "、{}");
            return false;
        }
    }

    private boolean checkString(MtMailItemVo bo, String parameter) {
        try {
            if (parameter.length() <= 50) {
                return true;
            } else {
                setVoErrorMsg(bo, MailConstants.ERROR_MSG_LENGTH_ERROR, "備考");
                return false;
            }
        } catch (Exception e) {
            setVoErrorMsg(bo, "備考" + "、{}");
            return false;
        }
    }

    private void checkExcelWithDatabase(List<MtMailItemVo> list, Set<Boolean> checkRes, Integer centerId) {
        if (!list.isEmpty()) {
            List<String> itemIdList = list.stream().map(MtMailItemVo::getItemId).filter(Objects::nonNull).collect(Collectors.toList());
            List<Integer> storeIdList = list.stream().map(MtMailItemVo::getStoreId).filter(Objects::nonNull).filter(this::checkNumberNum).map(Integer::parseInt).collect(Collectors.toList());
            checkMailInfoWithMailList(list, checkRes, centerId);
            checkItemInfoWithItemList(list, checkRes, itemIdList, centerId);
            checkStoreInfoWithStoreList(centerId, list, checkRes, storeIdList);
            list.forEach(a ->
                    a.setErrorMsg("".equals(a.getErrorMsg()) ? null : a.getErrorMsg())
            );
        }
    }


    private boolean checkNumberNum(String number) {
        if (ObjectUtil.isNull(number)) {
            return false;
        }
        try {
            if (number.matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
                Integer.parseInt(number);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void checkMailInfoWithMailList(List<MtMailItemVo> resList, Set<Boolean> checkRes, Integer centerId) {
        Map<String, String> mailMap = new HashMap<>();
        List<Map<String, Object>> list = mtMailItemMapper.selectMailNoWithCenterId(centerId);
        list.forEach(tmp -> mailMap.put(tmp.get("id").toString(), tmp.get("name") != null ? tmp.get("name").toString() : StringConstants.BLANK));
        if (!mailMap.isEmpty()) {
            resList.forEach(tmp -> checkRes.add(checkCenterInfoWithCenterInfoList(mailMap, tmp, tmp.getMailNo())));
        } else {
            checkRes.add(false);
            resList.forEach(tmp -> setVoErrorMsg(tmp, MailConstants.ERROR_MSG_NOT_EXIST, MailConstants.EXCEL_COL_MAIL_NO));
        }
    }

    private boolean checkCenterInfoWithCenterInfoList(Map<String, String> map, MtMailItemVo o, String oValue) {
        if (map.containsKey(oValue) && map.containsValue(o.getCenterId())) {
            return true;
        } else {
            setVoErrorMsg(o, MailConstants.ERROR_MSG_NOT_MATCH_03, MailConstants.EXCEL_COL_MAIL_NO);
            return false;
        }
    }

    private void checkItemInfoWithItemList(List<MtMailItemVo> resList, Set<Boolean> checkRes, List<String> itemIdList, Integer centerId) {
        Map<String, String> itemMap = new HashMap<>();
        itemIdList.removeAll(Collections.singleton(null));
        if (!itemIdList.isEmpty()) {
            List<Map<String, Object>> list = mtMailItemMapper.selectItemId(centerId, itemIdList);
            list.forEach(tmp -> itemMap.put(tmp.get("id").toString(), tmp.get("name") != null ? tmp.get("name").toString() : StringConstants.BLANK));
            if (!itemMap.isEmpty()) {
                resList.forEach(tmp -> checkRes.add(checkInfoWithInfoList(itemMap, tmp, tmp.getItemId(), MailConstants.EXCEL_COL_ITEM_ID)));
            } else {
                checkRes.add(false);
                resList.forEach(tmp -> setVoErrorMsg(tmp, MailConstants.ERROR_MSG_NOT_EXIST, MailConstants.EXCEL_COL_ITEM_ID));
            }
        }
    }

    private boolean checkInfoWithInfoList(Map<String, String> map, MtMailItemVo o, String oValue, String
            fieldName) {
        if (!map.containsKey(oValue)) {
            setVoErrorMsg(o, MailConstants.ERROR_MSG_NOT_EXIST, fieldName);
            return false;
        } else {
            return true;
        }
    }

    private void checkStoreInfoWithStoreList(Integer centerId, List<MtMailItemVo> resList, Set<Boolean> checkRes, List<Integer> storeIdList) {
        Map<String, String> map = new HashMap<>();
        storeIdList.removeAll(Collections.singleton(null));
        if (!storeIdList.isEmpty()) {
            List<BranchListVo> list = commonMapper.queryMailBasicStoreList(centerId);
            list.forEach(tmp -> map.put(tmp.getBranchcd().toString(), tmp.getBranchname() != null ? tmp.getBranchname() : StringConstants.BLANK));
            if (!map.isEmpty()) {
                resList.forEach(tmp -> checkRes.add(checkInfoWithInfoList(map, tmp, tmp.getStoreId(), MailConstants.EXCEL_COL_STORE_ID)));
            } else {
                checkRes.add(false);
                resList.forEach(tmp -> setVoErrorMsg(tmp, MailConstants.ERROR_MSG_NOT_EXIST, MailConstants.EXCEL_COL_STORE_ID));
            }
        }
    }
}
