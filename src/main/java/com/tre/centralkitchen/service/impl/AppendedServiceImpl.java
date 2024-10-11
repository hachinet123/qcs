package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.http.HttpStatus;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import com.tre.centralkitchen.common.constant.*;
import com.tre.centralkitchen.common.constant.business.FixAmountResultModifyConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.ItemType;
import com.tre.centralkitchen.common.excel.GetResultListReadListener;
import com.tre.centralkitchen.common.utils.*;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.bo.system.*;
import com.tre.centralkitchen.domain.po.MtCenterstatus;
import com.tre.centralkitchen.domain.po.TrOutTransbill;
import com.tre.centralkitchen.domain.po.TrOutTransitem;
import com.tre.centralkitchen.domain.po.WkOdrTransbill;
import com.tre.centralkitchen.domain.vo.common.*;
import com.tre.centralkitchen.domain.vo.system.AppendedUpdateVo;
import com.tre.centralkitchen.domain.vo.system.MtCenterstatusVo;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.AppendedService;
import com.tre.centralkitchen.service.AuthorityService;
import com.tre.centralkitchen.service.IMtCenterstatusService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppendedServiceImpl implements AppendedService {

    public static final String SLIP_CODE = "slip_code";
    public static final String DLVSCHED_DATE = "dlvsched_date";
    public static final String CENTER_ID = "center_id";
    public static final String MAIL_NO = "mail_no";
    public static final String STORE_ID = "store_id";
    public static final String LINE_NO = "line_no";
    public static final String RECIPE = "recipe";
    public static final String ORDER = "order";
    public static final String SALES = "sales";
    public static final String RECIPE_AM = "recipeAm";
    public static final String ORDER_AM = "orderAm";
    public static final String SALES_AM = "salesAm";
    public static final String WEIGHT_AM = "weightAm";
    public static final String R_AM = "rAm";
    public static final String O_AM = "oAm";
    public static final String S_AM = "sAm";
    public static final String QY_AM = "qyAm";
    private final AppendedMapper appendedMapper;
    private final TrOutTransitemMapper trOutTransitemMapper;
    private final TrOutTransbillMapper trOutTransbillMapper;
    private final WkOdrTransbillMapper wkOdrTransbillMapper;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final MasterService masterService;
    private final MtCenterstatusMapper mtCenterstatusMapper;
    private final IMtCenterstatusService iMtCenterstatusService;
    private final TmShoinServiceImpl tmShoinService;
    private final AuthorityService authorityService;

    @Override
    public TableDataInfo<AppendedUpdateVo> queryAppended(PageQuery pageQuery, AppendedSearchBo bo) {
        bo.build();
        Page<AppendedUpdateVo> appendedUpdateVoPage = appendedMapper.queryAppended(pageQuery.build(), bo);
        extractedViewData(appendedUpdateVoPage.getRecords());
        return TableDataInfo.build(appendedUpdateVoPage);
    }

    private static void extractedViewData(List<AppendedUpdateVo> appendedUpdateVoList) {
        appendedUpdateVoList.forEach(a -> {
            if (a.getTeikanTypeid() == 0) {
                if (a.getWeight() == null || a.getWeight().compareTo(new BigDecimal("0")) == 0) {
                    a.setCostRcp(BigDecimal.valueOf(0));
                    a.setCost(BigDecimal.valueOf(0));
                    a.setPrice(0);
                } else {
                    a.setCostRcp(a.getCostRcpAm().multiply(BigDecimal.valueOf(100)).divide(a.getWeight(), 2, RoundingMode.HALF_UP));
                    a.setCost(a.getCostAm().multiply(BigDecimal.valueOf(100)).divide(a.getWeight(), 2, RoundingMode.HALF_UP));
                    a.setPrice(Integer.parseInt(a.getPriceAm().multiply(BigDecimal.valueOf(100)).divide(a.getWeight(), 0, RoundingMode.HALF_UP).toString()));
                }
            }
            if (a.getWeight() == null) {
                a.setWeight(new BigDecimal("0"));
            }
            a.setDlvschedDateFmt(DateUtil.format(a.getDlvschedDate(), "yyyy/MM/dd"));
        });
    }

    @Override
    public void downloadCSV(AppendedSearchBo bo, HttpServletResponse response) {
        bo.build();
        List<AppendedUpdateVo> appendedUpdateVoList = appendedMapper.queryAppended(bo);
        extractedViewData(appendedUpdateVoList);
        SimpleCsvTableUtils.easyCsvExport(response, SysConstants.APPENDED_FILE_NAME_CSV, appendedUpdateVoList, AppendedUpdateVo.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(List<AppendedUpdateBo> boList) {
        boList.forEach(a -> {
            if (StringUtils.isBlank(a.getQuantity())) {
                a.setQuantity("0");
            }
            if (StringUtils.isBlank(a.getWeight())) {
                a.setWeight("0");
            }
            if (StringUtils.isBlank(a.getCostRcp())) {
                a.setCostRcp("0");
            }
            if (StringUtils.isBlank(a.getCost())) {
                a.setCost("0");
            }
            if (StringUtils.isBlank(a.getPrice())) {
                a.setPrice("0");
            }
            List<TmShohinVo> tmShohinVoList = tmShoinService.findJanStoreAll(Integer.parseInt(a.getStoreId()), a.getItemId());
            if (tmShohinVoList.isEmpty()) {
                throw new SysBusinessException(SysConstantInfo.JAN_STORE_ERROR);
            } else {
                if (tmShohinVoList.get(0).getTeikanTypeid() == 0) {
                    if (Double.parseDouble(a.getWeight()) > NumberConstants.NUM_INT_0 && Integer.parseInt(a.getQuantity()) <= NumberConstants.NUM_INT_0) {
                        throw new SysBusinessException(StrFormatter.format(FixAmountResultModifyConstants.ERROR_MSG_FILL_EXIST, FixAmountResultModifyConstants.EXCEL_COL_QTY));
                    }
                    if (Double.parseDouble(a.getWeight()) == NumberConstants.NUM_INT_0 && Integer.parseInt(a.getQuantity()) != NumberConstants.NUM_INT_0) {
                        throw new SysBusinessException(StrFormatter.format(FixAmountResultModifyConstants.ERROR_MSG_FILL_EXIST_0, FixAmountResultModifyConstants.EXCEL_COL_QTY));
                    }
                }
            }
        });

        Map<Object, List<AppendedUpdateBo>> mapBoList = boList.stream().collect(Collectors.groupingBy(u -> u.getSlipCode() + "|" + u.getDlvschedDate() + "|" + u.getCenterId() + "|" + u.getMailNo() + "|" + u.getStoreId()));

        for (AppendedUpdateBo appendedUpdateBo : boList) {
            TrOutTransitem trOutTransitem = updateTrOutTransitem(appendedUpdateBo);
            updateWkOdrTransbill(appendedUpdateBo, trOutTransitem);
        }
        for (Map.Entry<Object, List<AppendedUpdateBo>> entry : mapBoList.entrySet()) {
            AppendedUpdateBo appendedUpdateBo = entry.getValue().get(0);
            updateTrOutTransbill(appendedUpdateBo);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileBackErrorVo fmtImport(UploadBo bo, MultipartFile file, Boolean warningCheck, HttpServletResponse response) throws Exception {
        FileBackErrorVo fileBackErrorVo = new FileBackErrorVo();
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains(SysConstants.APPENDED_FILE_NAME)) {
            List<AppendedBo> boList = new ArrayList<>();
            GetResultListReadListener<AppendedBo> listener = new GetResultListReadListener<>(boList);
            EasyExcelFactory.read(file.getInputStream(), AppendedBo.class, listener).sheet().headRowNumber(3).doRead();
            if (!boList.isEmpty()) {
                List<AppendedBo> boListNew = checkAddFileData(bo.getCenterId(), boList, warningCheck, fileBackErrorVo);
                setSlipCodeAndLineNo(boListNew);
                if (!boListNew.isEmpty()) {
                    Map<Object, List<AppendedBo>> listMap = boListNew.stream().collect(Collectors.groupingBy(u -> u.getSlipCode() + "|" + u.getDlvschedDate() + "|" + u.getCenterId() + "|" + u.getMailNo() + "|" + u.getStoreId()));

                    for (AppendedBo appendedBo : boListNew) {
                        QueryWrapper<TrOutTransitem> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq(SLIP_CODE, Integer.parseInt(appendedBo.getSlipCode()));
                        queryWrapper.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedBo.getDlvschedDate()));
                        queryWrapper.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
                        queryWrapper.eq(MAIL_NO, Integer.parseInt(appendedBo.getMailNo()));
                        queryWrapper.eq(STORE_ID, Integer.parseInt(appendedBo.getStoreId()));
                        queryWrapper.eq(LINE_NO, Integer.parseInt(appendedBo.getLineNo()));
                        Long trOutTransitems = trOutTransitemMapper.selectCount(queryWrapper);
                        if (trOutTransitems > 0) {
                            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.APPENDED_NOT_EXISTENT_MSG, appendedBo.getSlipCode(), appendedBo.getLineNo()));
                        }

                        QueryWrapper<WkOdrTransbill> queryWrapper2 = new QueryWrapper<>();
                        queryWrapper2.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
                        queryWrapper2.eq("trans_date", DateUtil.parseDate(appendedBo.getDlvschedDate()));
                        queryWrapper2.eq("transbill_code", Integer.parseInt(appendedBo.getSlipCode()));
                        queryWrapper2.eq(LINE_NO, Integer.parseInt(appendedBo.getLineNo()));
                        Long wkOdrTransbills = wkOdrTransbillMapper.selectCount(queryWrapper2);
                        if (wkOdrTransbills > 0) {
                            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.APPENDED_NOT_EXISTENT_MSG, appendedBo.getSlipCode(), appendedBo.getLineNo()));
                        }

                        TmShohinVo tmShohinVo = tmShoinService.findByKey(Integer.parseInt(appendedBo.getStoreId()), appendedBo.getItemId(), appendedBo.getAreaId(), true);
                        if (Objects.isNull(tmShohinVo)) {
                            tmShohinVo = tmShoinService.findByKey(0, appendedBo.getItemId(), appendedBo.getAreaId(), true);
                        }

                        insertTrOutTransitem(appendedBo, bo, tmShohinVo, ItemType.product.getCode());
                        insertWkOdrTransbill(appendedBo, bo, ItemType.product.getCode());
                    }

                    for (Map.Entry<Object, List<AppendedBo>> entry : listMap.entrySet()) {
                        AppendedBo appendedBo = entry.getValue().get(0);
                        QueryWrapper<TrOutTransbill> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq(SLIP_CODE, Integer.parseInt(appendedBo.getSlipCode()));
                        queryWrapper.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedBo.getDlvschedDate()));
                        queryWrapper.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
                        queryWrapper.eq(MAIL_NO, Integer.parseInt(appendedBo.getMailNo()));
                        queryWrapper.eq(STORE_ID, Integer.parseInt(appendedBo.getStoreId()));
                        Long trOutTransbills = trOutTransbillMapper.selectCount(queryWrapper);
                        if (trOutTransbills == 0) {
                            insertTrOutTransbill(appendedBo, bo);
                        } else {
                            appendedBo.setUpdFuncId(bo.getUpdFuncId());
                            appendedBo.setUpdOpeId(bo.getUpdOpeId());
                            AppendedUpdateBo appendedUpdateBo = BeanUtil.toBean(appendedBo, AppendedUpdateBo.class);
                            updateTrOutTransbill(appendedUpdateBo);
                        }
                    }

                }
            } else {
                throw new SysBusinessException(SysConstantInfo.FILE_DATA_EMPTY_MSG);
            }
        }
        if (fileName != null && fileName.contains(SysConstants.APPENDED_UPDATE_FILE_NAME)) {
            List<AppendedUpdateBo> boList = new ArrayList<>();
            GetResultListReadListener<AppendedUpdateBo> listener = new GetResultListReadListener<>(boList);
            EasyExcelFactory.read(file.getInputStream(), AppendedUpdateBo.class, listener).sheet().headRowNumber(3).doRead();
            List<AppendedUpdateBo> boListNew = checkUpdateFileData(bo.getCenterId(), boList, warningCheck, fileBackErrorVo);
            if (!boListNew.isEmpty()) {
                update(boListNew);
            }
        }
        return fileBackErrorVo;
    }

    @Override
    public void fmtExport(AppendedSearchBo bo, HttpServletResponse response) throws Exception {
        bo.build();
        List<AppendedUpdateVo> appendedUpdateVoList = appendedMapper.queryAppended(bo);
        extractedViewData(appendedUpdateVoList);
        downloadFmtDataFile(appendedUpdateVoList, response);
    }

    // 商品-定貫：使用db基本价格
    // 商品-不定貫：使用db基本价格
    // kit-定貫：計算商品-定貫后的基本价格*入力数量/商品数量
    // kit-不定貫：計算商品-不定貫后的基本价格*入力数量/商品数量
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMailNo20(AppendedMailNo20Bo mailNo20Bo) {
        List<Integer> integerList = Arrays.stream(mailNo20Bo.getStoreIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());

        for (Integer storeId : integerList) {
            UploadBo bo = new UploadBo();
            bo.setCenterId(Integer.valueOf(mailNo20Bo.getCenterId()));

            AppendedBo appendedBo = new AppendedBo();
            appendedBo.setSlipCode(String.valueOf(iMtCenterstatusService.getSlip(Integer.valueOf(mailNo20Bo.getCenterId()))));
            appendedBo.setItemId(mailNo20Bo.getItemId());
            appendedBo.setMailNo("20");
            appendedBo.setCallCode(mailNo20Bo.getCallCode());
            appendedBo.setCenterId(mailNo20Bo.getCenterId());
            appendedBo.setQuantity(mailNo20Bo.getQuantity());
            appendedBo.setWeight(mailNo20Bo.getWeight());
            appendedBo.setStoreId(String.valueOf(storeId));
            appendedBo.setDlvschedDate(mailNo20Bo.getDlvschedDate());
            appendedBo.setLineNo("1");

            HashSet<String> list = new HashSet<>();
            if (StringUtils.isNotBlank(mailNo20Bo.getCallCode())) {
                List<TmShohinVo> tmShohinVos = tmShoinService.findJanByCallCodeAll(storeId, Integer.parseInt(mailNo20Bo.getCallCode()));
                if (tmShohinVos.isEmpty()) {
                    throw new SysBusinessException(SysConstantInfo.STORE_JAN_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.STORE_JAN_ERROR_CODE);
                } else {
                    if (StringUtils.isNotBlank(mailNo20Bo.getItemId())) {
                        List<TmShohinVo> tmShohinVoList = tmShohinVos.stream().filter(s -> s.getItemId().equals(mailNo20Bo.getItemId())).collect(Collectors.toList());
                        if (tmShohinVoList.isEmpty()) {
                            throw new SysBusinessException(SysConstantInfo.STORE_JAN_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.STORE_JAN_ERROR_CODE);
                        } else {
                            extracted(appendedBo, list, tmShohinVoList);
                        }
                    } else {
                        List<TmShohinVo> tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId() == Integer.parseInt(appendedBo.getStoreId())).collect(Collectors.toList());
                        if (tmShohinVoList.isEmpty()) {
                            tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId().equals(0)).collect(Collectors.toList());
                        }
                        appendedBo.setItemId(tmShohinVoList.get(0).getItemId());
                        extracted(appendedBo, list, tmShohinVoList);
                    }
                }
            }
            if (StringUtils.isBlank(mailNo20Bo.getCallCode()) && StringUtils.isNotBlank(appendedBo.getItemId())) {
                List<TmShohinVo> tmShohinVoList = tmShoinService.findJanStoreAll(Integer.parseInt(appendedBo.getStoreId()), appendedBo.getItemId());
                if (tmShohinVoList.isEmpty()) {
                    throw new SysBusinessException(SysConstantInfo.STORE_NO_PRODUCT_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.STORE_NO_PRODUCT_ERROR_CODE);
                } else {
                    extracted(appendedBo, list, tmShohinVoList);
                }
            }
            if (list.contains("重量") || list.contains("数量と重量")) {
                throw new SysBusinessException(StrFormatter.format(SysConstantInfo.INCORRECT, Joiner.on("、").join(list)), HttpStatus.HTTP_OK, SysConstantInfo.INCORRECT_CODE);
            }

            QueryWrapper<TrOutTransitem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(SLIP_CODE, Integer.parseInt(appendedBo.getSlipCode()));
            queryWrapper.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedBo.getDlvschedDate()));
            queryWrapper.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
            queryWrapper.eq(MAIL_NO, Integer.parseInt(appendedBo.getMailNo()));
            queryWrapper.eq(STORE_ID, Integer.parseInt(appendedBo.getStoreId()));
            queryWrapper.eq(LINE_NO, Integer.parseInt(appendedBo.getLineNo()));
            Long trOutTransitems = trOutTransitemMapper.selectCount(queryWrapper);
            if (trOutTransitems > 0) {
                throw new SysBusinessException(StrFormatter.format(SysConstantInfo.APPENDED_NOT_EXISTENT_MSG, appendedBo.getSlipCode(), appendedBo.getLineNo()));
            }

            QueryWrapper<WkOdrTransbill> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
            queryWrapper2.eq("trans_date", DateUtil.parseDate(appendedBo.getDlvschedDate()));
            queryWrapper2.eq("transbill_code", Integer.parseInt(appendedBo.getSlipCode()));
            queryWrapper2.eq(LINE_NO, Integer.parseInt(appendedBo.getLineNo()));
            Long wkOdrTransbills = wkOdrTransbillMapper.selectCount(queryWrapper2);
            if (wkOdrTransbills > 0) {
                throw new SysBusinessException(StrFormatter.format(SysConstantInfo.APPENDED_NOT_EXISTENT_MSG, appendedBo.getSlipCode(), appendedBo.getLineNo()));
            }
            TmShohinVo tmShohinVo = tmShoinService.findByKey(Integer.parseInt(appendedBo.getStoreId()), appendedBo.getItemId(), appendedBo.getAreaId(), true);
            if (Objects.isNull(tmShohinVo)) {
                tmShohinVo = tmShoinService.findByKey(0, appendedBo.getItemId(), appendedBo.getAreaId(), true);
            }

            if (mailNo20Bo.getType().equals(ItemType.kit.getCode())) {
                appendedBo.setCostRcp(BigDecimal.valueOf(Double.parseDouble(mailNo20Bo.getQuantity())).multiply(tmShohinVo.getCostPc()).divide(BigDecimal.valueOf(tmShohinVo.getNSzname()), 2, RoundingMode.HALF_UP).toString());
                appendedBo.setCost(BigDecimal.valueOf(Double.parseDouble(mailNo20Bo.getQuantity())).multiply(tmShohinVo.getCost()).divide(BigDecimal.valueOf(tmShohinVo.getNSzname()), 2, RoundingMode.HALF_UP).toString());
                appendedBo.setPrice(BigDecimal.valueOf(Double.parseDouble(mailNo20Bo.getQuantity())).multiply(tmShohinVo.getPrice()).divide(BigDecimal.valueOf(tmShohinVo.getNSzname()), 2, RoundingMode.HALF_UP).toString());
            }

            insertTrOutTransitem(appendedBo, bo, tmShohinVo, mailNo20Bo.getType());
            insertWkOdrTransbill(appendedBo, bo, mailNo20Bo.getType());

            QueryWrapper<TrOutTransbill> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq(SLIP_CODE, Integer.parseInt(appendedBo.getSlipCode()));
            queryWrapper3.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedBo.getDlvschedDate()));
            queryWrapper3.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
            queryWrapper3.eq(MAIL_NO, Integer.parseInt(appendedBo.getMailNo()));
            queryWrapper3.eq(STORE_ID, Integer.parseInt(appendedBo.getStoreId()));
            Long trOutTransbills = trOutTransbillMapper.selectCount(queryWrapper3);
            if (trOutTransbills == 0) {
                insertTrOutTransbill(appendedBo, bo);
            } else {
                appendedBo.setUpdFuncId(bo.getUpdFuncId());
                appendedBo.setUpdOpeId(bo.getUpdOpeId());
                AppendedUpdateBo appendedUpdateBo = BeanUtil.toBean(appendedBo, AppendedUpdateBo.class);
                updateTrOutTransbill(appendedUpdateBo);
            }
        }
    }

    private void insertTrOutTransitem(AppendedBo appendedBo, UploadBo bo, TmShohinVo tmShohinVo, String type) {
        TrOutTransitem trOutTransitem = BeanUtil.toBean(bo, TrOutTransitem.class);
        trOutTransitem.setSlipCode(Integer.parseInt(appendedBo.getSlipCode()));
        trOutTransitem.setDlvschedDate(DateUtil.parseDate(appendedBo.getDlvschedDate()));
        trOutTransitem.setCenterId(Integer.parseInt(appendedBo.getCenterId()));
        trOutTransitem.setMailNo(Integer.parseInt(appendedBo.getMailNo()));
        trOutTransitem.setStoreId(Integer.parseInt(appendedBo.getStoreId()));
        trOutTransitem.setLineNo(Integer.parseInt(appendedBo.getLineNo()));
        trOutTransitem.setItemId(appendedBo.getItemId());
        trOutTransitem.setLotNo(appendedBo.getLotNo());
        if (type.equals(ItemType.kit.getCode())) {
            trOutTransitem.setQy(1);
        } else {
            trOutTransitem.setQy(Integer.parseInt(appendedBo.getQuantity()));
        }
        trOutTransitem.setWeightAm(new BigDecimal(appendedBo.getWeight()));
        trOutTransitem.setWmTypeid(Integer.parseInt(appendedBo.getTeikanTypeid()));

        if (StringUtils.isBlank(appendedBo.getPlaceId())) {
            trOutTransitem.setPlaceId(tmShohinVo.getPlaceId() == null ? 0 : tmShohinVo.getPlaceId());
        } else {
            trOutTransitem.setPlaceId(StringUtils.isBlank(appendedBo.getPlaceId()) ? 0 : Integer.parseInt(appendedBo.getPlaceId()));
        }
        if (StringUtils.isBlank(appendedBo.getCostRcp())) {
            trOutTransitem.setCostRcp(tmShohinVo.getCostPc() == null ? new BigDecimal("0.00") : tmShohinVo.getCostPc());
        } else {
            trOutTransitem.setCostRcp(new BigDecimal(appendedBo.getCostRcp()));
        }
        if (StringUtils.isBlank(appendedBo.getCost())) {
            trOutTransitem.setCost(tmShohinVo.getCost() == null ? new BigDecimal("0.00") : tmShohinVo.getCost());
        } else {
            trOutTransitem.setCost(new BigDecimal(appendedBo.getCost()));
        }
        if (StringUtils.isBlank(appendedBo.getPrice())) {
            trOutTransitem.setPrice(tmShohinVo.getPrice() == null ? 0 : Double.valueOf(tmShohinVo.getPrice().toString()).intValue());
        } else {
            trOutTransitem.setPrice(Double.valueOf(appendedBo.getPrice()).intValue());
        }
        appendedBo.setCostRcp(trOutTransitem.getCostRcp().toString());
        appendedBo.setCost(trOutTransitem.getCost().toString());
        appendedBo.setPrice(trOutTransitem.getPrice().toString());

        Map<String, BigDecimal> map = calcPrice(trOutTransitem.getWmTypeid(), trOutTransitem.getCostRcp(), trOutTransitem.getCost(), trOutTransitem.getPrice(), trOutTransitem.getWeightAm(), trOutTransitem.getQy());
        trOutTransitem.setCostRcp(map.get(RECIPE));
        trOutTransitem.setCost(map.get(ORDER));
        trOutTransitem.setPrice(Integer.parseInt(map.get(SALES).toString()));
        trOutTransitem.setRecipeAm(map.get(RECIPE_AM));
        trOutTransitem.setOrderAm(map.get(ORDER_AM));
        trOutTransitem.setSalesAm(Long.parseLong(map.get(SALES_AM).toString()));
        // 20
        set20MailNoTrOutTransItem(trOutTransitem);

        trOutTransitemMapper.insert(trOutTransitem);
    }

    private void insertTrOutTransbill(AppendedBo appendedBo, UploadBo bo) {
        QueryWrapper<TrOutTransitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SLIP_CODE, Integer.parseInt(appendedBo.getSlipCode()));
        queryWrapper.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedBo.getDlvschedDate()));
        queryWrapper.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
        queryWrapper.eq(MAIL_NO, Integer.parseInt(appendedBo.getMailNo()));
        queryWrapper.eq(STORE_ID, Integer.parseInt(appendedBo.getStoreId()));
        List<TrOutTransitem> trOutTransitems = trOutTransitemMapper.selectList(queryWrapper);

        Map<String, Object> sumMap = sum(trOutTransitems);
        TrOutTransbill trOutTransbill = BeanUtil.toBean(bo, TrOutTransbill.class);
        trOutTransbill.setSlipCode(Integer.parseInt(appendedBo.getSlipCode()));
        trOutTransbill.setDlvschedDate(DateUtil.parseDate(appendedBo.getDlvschedDate()));
        trOutTransbill.setCenterId(Integer.parseInt(appendedBo.getCenterId()));
        trOutTransbill.setMailNo(Integer.parseInt(appendedBo.getMailNo()));
        trOutTransbill.setStoreId(Integer.parseInt(appendedBo.getStoreId()));
        trOutTransbill.setOutDate(DateUtil.date());
        trOutTransbill.setQyAm(Integer.valueOf(sumMap.get(QY_AM).toString()));
        trOutTransbill.setWeightAm(new BigDecimal(sumMap.get(WEIGHT_AM).toString()));
        trOutTransbill.setRAm(new BigDecimal(sumMap.get(R_AM).toString()));
        trOutTransbill.setOAm(new BigDecimal(sumMap.get(O_AM).toString()));
        trOutTransbill.setSAm(Long.parseLong(sumMap.get(S_AM).toString()));
        trOutTransbill.setDepartmentId(appendedBo.getDepartmentId());
        trOutTransbill.setWmTypeid(Integer.parseInt(appendedBo.getTeikanTypeid()));
        trOutTransbillMapper.insert(trOutTransbill);
    }

    private void insertWkOdrTransbill(AppendedBo appendedBo, UploadBo bo, String type) {
        WkOdrTransbill wkOdrTransbill = BeanUtil.toBean(bo, WkOdrTransbill.class);
        wkOdrTransbill.setCenterId(Integer.parseInt(appendedBo.getCenterId()));
        wkOdrTransbill.setTransDate(DateUtil.parseDate(appendedBo.getDlvschedDate()));
        wkOdrTransbill.setTransbillCode(Integer.parseInt(appendedBo.getSlipCode()));
        wkOdrTransbill.setMailNo(Integer.parseInt(appendedBo.getMailNo()));
        wkOdrTransbill.setBillKindid(30);
        wkOdrTransbill.setOutOrgId(Integer.parseInt(appendedBo.getMailNo().equals("20") ? appendedBo.getStoreId() : appendedBo.getCenterId()));
        wkOdrTransbill.setInOrgId(Integer.parseInt(appendedBo.getMailNo().equals("20") ? appendedBo.getCenterId() : appendedBo.getStoreId()));
        wkOdrTransbill.setInDepartmentId(appendedBo.getDepartmentId());
        wkOdrTransbill.setOutDepartmentId(appendedBo.getDepartmentId());

        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
        MtCenterstatusVo mtCenterstatusVo = mtCenterstatusMapper.selectVoOne(queryWrapper);

        wkOdrTransbill.setVendorId(mtCenterstatusVo.getVendorId());
        wkOdrTransbill.setBillDate(DateUtil.parseDate(appendedBo.getDlvschedDate()));
        wkOdrTransbill.setInDate(DateUtil.parseDate(appendedBo.getDlvschedDate()));
        wkOdrTransbill.setCountDate(DateUtil.parseDate(appendedBo.getDlvschedDate()));
        wkOdrTransbill.setDlvrouteTypeid(1);
        wkOdrTransbill.setTcdcTypeid(3);
        wkOdrTransbill.setDlvwayTypeid(1);
        wkOdrTransbill.setShippingTypeid(0);
        wkOdrTransbill.setThermalZoneTypeid(2);
        wkOdrTransbill.setReasonTypeid(99);
        wkOdrTransbill.setTransitemLineNo(0);
        wkOdrTransbill.setSendDate(DateUtil.date());
        wkOdrTransbill.setFSend(0);

        QueryWrapper<TrOutTransitem> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq(SLIP_CODE, Integer.parseInt(appendedBo.getSlipCode()));
        queryWrapper2.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedBo.getDlvschedDate()));
        queryWrapper2.eq(CENTER_ID, Integer.parseInt(appendedBo.getCenterId()));
        queryWrapper2.eq(MAIL_NO, Integer.parseInt(appendedBo.getMailNo()));
        queryWrapper2.eq(STORE_ID, Integer.parseInt(appendedBo.getStoreId()));
        List<TrOutTransitem> trOutTransitems = trOutTransitemMapper.selectList(queryWrapper2);

        Map<String, BigDecimal> map = calcPrice(Integer.parseInt(appendedBo.getTeikanTypeid()), new BigDecimal(appendedBo.getCostRcp()), new BigDecimal(appendedBo.getCost()), Integer.parseInt(appendedBo.getPrice()), new BigDecimal(appendedBo.getWeight()), type.equals(ItemType.kit.getCode()) ? 1 : Integer.parseInt(appendedBo.getQuantity()));

        wkOdrTransbill.setOAm(Math.abs(Long.parseLong(trOutTransitems.stream().map(TrOutTransitem::getOrderAm).reduce(BigDecimal.ZERO, BigDecimalUtils::sum).setScale(0, RoundingMode.HALF_UP).toString())));
        wkOdrTransbill.setSAm(Math.abs(trOutTransitems.stream().filter(Objects::nonNull).mapToLong(TrOutTransitem::getSalesAm).sum()));
        wkOdrTransbill.setLineNo(Integer.parseInt(appendedBo.getLineNo()));
        if (type.equals(ItemType.kit.getCode())) {
            wkOdrTransbill.setQy(1000);
        } else {
            wkOdrTransbill.setQy(Integer.parseInt(appendedBo.getQuantity()) * 1000);
        }

        wkOdrTransbill.setCost(Integer.parseInt(map.get(ORDER).multiply(new BigDecimal("1000")).setScale(0, RoundingMode.HALF_UP).toString()));
        wkOdrTransbill.setOrderAm(Long.parseLong(map.get(ORDER_AM).toString()));
        wkOdrTransbill.setSalesAm(Long.parseLong(map.get(SALES_AM).toString()));
        wkOdrTransbill.setPrice(Integer.parseInt(map.get(SALES).toString()));
        wkOdrTransbill.setItemId(appendedBo.getItemId());

        wkOdrTransbillMapper.insert(wkOdrTransbill);
    }

    private TrOutTransitem updateTrOutTransitem(AppendedUpdateBo appendedUpdateBo) {
        UpdateWrapper<TrOutTransitem> ew = new UpdateWrapper<>();
        ew.lambda().eq(TrOutTransitem::getDlvschedDate, DateUtil.parseDate(appendedUpdateBo.getDlvschedDate())).eq(TrOutTransitem::getCenterId, Integer.parseInt(appendedUpdateBo.getCenterId())).eq(TrOutTransitem::getMailNo, Integer.parseInt(appendedUpdateBo.getMailNo())).eq(TrOutTransitem::getStoreId, Integer.parseInt(appendedUpdateBo.getStoreId())).eq(TrOutTransitem::getLineNo, Integer.parseInt(appendedUpdateBo.getLineNo())).eq(TrOutTransitem::getSlipCode, Integer.parseInt(appendedUpdateBo.getSlipCode()));

        TrOutTransitem trOutTransitem = new TrOutTransitem();
        trOutTransitem.setPlaceId(StringUtils.isBlank(appendedUpdateBo.getPlaceId()) ? 0 : Integer.parseInt(appendedUpdateBo.getPlaceId()));
        trOutTransitem.setLotNo(appendedUpdateBo.getLotNo());
        trOutTransitem.setMailNo(Integer.parseInt(appendedUpdateBo.getMailNo()));
        trOutTransitem.setQy(Integer.parseInt(StringUtils.isBlank(appendedUpdateBo.getQuantity()) ? "0" : appendedUpdateBo.getQuantity()));
        trOutTransitem.setWeightAm(new BigDecimal(StringUtils.isBlank(appendedUpdateBo.getWeight()) ? "0" : appendedUpdateBo.getWeight()));
        trOutTransitem.setCostRcp(new BigDecimal(StringUtils.isBlank(appendedUpdateBo.getCostRcp()) ? "0" : appendedUpdateBo.getCostRcp()));
        trOutTransitem.setCost(new BigDecimal(StringUtils.isBlank(appendedUpdateBo.getCost()) ? "0" : appendedUpdateBo.getCost()));
        trOutTransitem.setPrice(Integer.parseInt(StringUtils.isBlank(appendedUpdateBo.getPrice()) ? "0" : appendedUpdateBo.getPrice()));
        Map<String, BigDecimal> map = calcPrice(Integer.parseInt(appendedUpdateBo.getTeikanTypeid()), trOutTransitem.getCostRcp(), trOutTransitem.getCost(), trOutTransitem.getPrice(), trOutTransitem.getWeightAm(), trOutTransitem.getQy());
        trOutTransitem.setCostRcp(map.get(RECIPE));
        trOutTransitem.setCost(map.get(ORDER));
        trOutTransitem.setPrice(Integer.parseInt(map.get(SALES).toString()));
        trOutTransitem.setRecipeAm(map.get(RECIPE_AM));
        trOutTransitem.setOrderAm(map.get(ORDER_AM));
        trOutTransitem.setSalesAm(Long.parseLong(map.get(SALES_AM).toString()));
        trOutTransitem.setUpdFuncId(appendedUpdateBo.getUpdFuncId());
        trOutTransitem.setUpdOpeId(appendedUpdateBo.getUpdOpeId());
        // 20
        set20MailNoTrOutTransItem(trOutTransitem);
        trOutTransitemMapper.update(trOutTransitem, ew);
        return trOutTransitem;
    }

    private void set20MailNoTrOutTransItem(TrOutTransitem trOutTransitem) {
        if (trOutTransitem.getMailNo() == 20) {
            trOutTransitem.setQy(-trOutTransitem.getQy());
            trOutTransitem.setWeightAm(trOutTransitem.getWeightAm().multiply(new BigDecimal("-1")));
            trOutTransitem.setRecipeAm(trOutTransitem.getRecipeAm().multiply(new BigDecimal("-1")));
            trOutTransitem.setOrderAm(trOutTransitem.getOrderAm().multiply(new BigDecimal("-1")));
            trOutTransitem.setSalesAm(-trOutTransitem.getSalesAm());
        }
    }

    private void updateTrOutTransbill(AppendedUpdateBo appendedUpdateBo) {
        QueryWrapper<TrOutTransitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SLIP_CODE, Integer.parseInt(appendedUpdateBo.getSlipCode()));
        queryWrapper.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedUpdateBo.getDlvschedDate()));
        queryWrapper.eq(CENTER_ID, Integer.parseInt(appendedUpdateBo.getCenterId()));
        queryWrapper.eq(MAIL_NO, Integer.parseInt(appendedUpdateBo.getMailNo()));
        queryWrapper.eq(STORE_ID, Integer.parseInt(appendedUpdateBo.getStoreId()));
        List<TrOutTransitem> trOutTransitems = trOutTransitemMapper.selectList(queryWrapper);

        Map<String, Object> sumMap = sum(trOutTransitems);
        TrOutTransbill trOutTransbill = new TrOutTransbill();
        trOutTransbill.setMailNo(Integer.parseInt(appendedUpdateBo.getMailNo()));
        trOutTransbill.setUpdFuncId(appendedUpdateBo.getUpdFuncId());
        trOutTransbill.setUpdOpeId(appendedUpdateBo.getUpdOpeId());
        trOutTransbill.setQyAm(Integer.valueOf(sumMap.get(QY_AM).toString()));
        trOutTransbill.setWeightAm(new BigDecimal(sumMap.get(WEIGHT_AM).toString()));
        trOutTransbill.setRAm(new BigDecimal(sumMap.get(R_AM).toString()));
        trOutTransbill.setOAm(new BigDecimal(sumMap.get(O_AM).toString()));
        trOutTransbill.setSAm(Long.parseLong(sumMap.get(S_AM).toString()));
        UpdateWrapper<TrOutTransbill> ew = new UpdateWrapper<>();
        ew.lambda().eq(TrOutTransbill::getDlvschedDate, DateUtil.parseDate(appendedUpdateBo.getDlvschedDate())).eq(TrOutTransbill::getCenterId, Integer.parseInt(appendedUpdateBo.getCenterId())).eq(TrOutTransbill::getMailNo, Integer.parseInt(appendedUpdateBo.getMailNo())).eq(TrOutTransbill::getStoreId, Integer.parseInt(appendedUpdateBo.getStoreId())).eq(TrOutTransbill::getSlipCode, Integer.parseInt(appendedUpdateBo.getSlipCode()));
        trOutTransbillMapper.update(trOutTransbill, ew);
    }

    private void updateWkOdrTransbill(AppendedUpdateBo appendedUpdateBo, TrOutTransitem trOutTransitem) {

        UpdateWrapper<WkOdrTransbill> ew = new UpdateWrapper<>();
        ew.lambda().eq(WkOdrTransbill::getTransDate, DateUtil.parseDate(appendedUpdateBo.getDlvschedDate())).eq(WkOdrTransbill::getCenterId, Integer.parseInt(appendedUpdateBo.getCenterId())).eq(WkOdrTransbill::getLineNo, Integer.parseInt(appendedUpdateBo.getLineNo())).eq(WkOdrTransbill::getMailNo, Integer.parseInt(appendedUpdateBo.getMailNo())).eq(WkOdrTransbill::getTransbillCode, Integer.parseInt(appendedUpdateBo.getSlipCode()));

        WkOdrTransbill wkOdrTransbill = new WkOdrTransbill();
        wkOdrTransbill.setQy(Integer.parseInt(StringUtils.isBlank(appendedUpdateBo.getQuantity()) ? "0" : appendedUpdateBo.getQuantity()) * 1000);
        wkOdrTransbill.setCost(Math.abs(Integer.parseInt(trOutTransitem.getCost().multiply(new BigDecimal("1000")).setScale(0, RoundingMode.HALF_UP).toString())));
        wkOdrTransbill.setPrice(Math.abs(trOutTransitem.getPrice()));
        wkOdrTransbill.setOrderAm(Math.abs(Long.parseLong(trOutTransitem.getOrderAm().toString())));
        wkOdrTransbill.setSalesAm(Math.abs(trOutTransitem.getSalesAm()));

        wkOdrTransbill.setUpdFuncId(appendedUpdateBo.getUpdFuncId());
        wkOdrTransbill.setUpdOpeId(appendedUpdateBo.getUpdOpeId());
        wkOdrTransbillMapper.update(wkOdrTransbill, ew);

        QueryWrapper<TrOutTransitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SLIP_CODE, Integer.parseInt(appendedUpdateBo.getSlipCode()));
        queryWrapper.eq(DLVSCHED_DATE, DateUtil.parseDate(appendedUpdateBo.getDlvschedDate()));
        queryWrapper.eq(CENTER_ID, Integer.parseInt(appendedUpdateBo.getCenterId()));
        queryWrapper.eq(MAIL_NO, Integer.parseInt(appendedUpdateBo.getMailNo()));
        queryWrapper.eq(STORE_ID, Integer.parseInt(appendedUpdateBo.getStoreId()));
        List<TrOutTransitem> trOutTransitems = trOutTransitemMapper.selectList(queryWrapper);

        WkOdrTransbill wkOdrTransbill2 = new WkOdrTransbill();
        wkOdrTransbill2.setOAm(Math.abs(Long.parseLong(trOutTransitems.stream().map(TrOutTransitem::getOrderAm).reduce(BigDecimal.ZERO, BigDecimalUtils::sum).setScale(0, RoundingMode.HALF_UP).toString())));
        wkOdrTransbill2.setSAm(Math.abs(trOutTransitems.stream().filter(Objects::nonNull).mapToLong(TrOutTransitem::getSalesAm).sum()));

        UpdateWrapper<WkOdrTransbill> ew2 = new UpdateWrapper<>();
        ew2.lambda().eq(WkOdrTransbill::getTransDate, DateUtil.parseDate(appendedUpdateBo.getDlvschedDate())).eq(WkOdrTransbill::getCenterId, Integer.parseInt(appendedUpdateBo.getCenterId())).eq(WkOdrTransbill::getTransbillCode, Integer.parseInt(appendedUpdateBo.getSlipCode()));

        wkOdrTransbillMapper.update(wkOdrTransbill2, ew2);
    }

    private List<AppendedBo> checkAddFileData(Integer centerId, List<AppendedBo> resList, Boolean warningCheck, FileBackErrorVo fileBackErrorVo) {
        // getMstList
        CommonMailNoBo bo = new CommonMailNoBo();
        bo.setCenterId(centerId);
        List<MailListVo> mailListVos = masterService.getMailList(bo);
        List<MtPlaceVo> mtPlaceVos = masterService.getPlaceList();
        List<BranchListVo> branchVos = masterService.queryMailBasicStoreList(centerId);
        UserInfoVo userInfoVo = authorityService.getUserInfoByTokenID();

        // start check
        Set<Boolean> checkResult = new HashSet<>();
        Set<Boolean> priceFlag = new HashSet<>();
        Set<Boolean> dateFlag = new HashSet<>();
        Set<Boolean> otherFlag = new HashSet<>();
        for (AppendedBo appendedBo : resList) {
            replaceComma(appendedBo);
            // required
            List<String> list = checkMustList(appendedBo);
            if (!list.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedBo.setErrorMsg(SysConstantInfo.PARAM_NOT_NULL_MSG);
                continue;
            }
            // format
            HashSet<String> formatList = checkFormatList(appendedBo);
            if (!formatList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedBo.setErrorMsg(StrFormatter.format(SysConstantInfo.INCORRECT, Joiner.on("、").join(formatList)));
                continue;
            }
            // center as
//            if (!centerId.equals(Integer.parseInt(appendedBo.getCenterId()))) {
//                checkResult.add(true);
//                otherFlag.add(true);
//                appendedBo.setErrorMsg(SysConstantInfo.CENTER_FRAME_ERROR);
//                continue;
//            }

            // authority
            List<String> authorityList = AuthorityUtils.checkAuthority(appendedBo.getCenterId(), appendedBo.getLineId(), userInfoVo);
            if (!authorityList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedBo.setErrorMsg(StrFormatter.format(SysConstantInfo.AUTHORITY, Joiner.on("、").join(authorityList)));
                continue;
            }

            //resetData
            if (StringUtils.isBlank(appendedBo.getQuantity())) {
                appendedBo.setQuantity(NumberConstants.NUM_0);
            }

            if (StringUtils.isBlank(appendedBo.getWeight())) {
                appendedBo.setWeight(NumberConstants.NUM_0);
            }

            // mst
            List<String> mstList = checkMstList(appendedBo, mailListVos, branchVos, mtPlaceVos);
            if (!mstList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedBo.setErrorMsg(StrFormatter.format(SysConstantInfo.PARAM_NOT_EXISTENT_MSG, Joiner.on("、").join(mstList)));
                continue;
            }

            if (warningCheck) {
                // priceCheck
                if (StringUtils.isNotBlank(appendedBo.getCostRcp()) && StringUtils.isNotBlank(appendedBo.getCost()) && new BigDecimal(appendedBo.getCostRcp()).compareTo(new BigDecimal(appendedBo.getCost())) > 0) {
                    checkResult.add(true);
                    priceFlag.add(true);
                    appendedBo.setErrorMsg((StringUtil.isNotBlank(appendedBo.getErrorMsg()) ? appendedBo.getErrorMsg() : "") + SysConstantInfo.PRICE_CHECK_1);
                }

                if (StringUtils.isNotBlank(appendedBo.getCost()) && StringUtils.isNotBlank(appendedBo.getPrice()) && new BigDecimal(appendedBo.getCost()).compareTo(new BigDecimal(appendedBo.getPrice())) > 0) {
                    checkResult.add(true);
                    priceFlag.add(true);
                    appendedBo.setErrorMsg((StringUtil.isNotBlank(appendedBo.getErrorMsg()) ? appendedBo.getErrorMsg() : "") + SysConstantInfo.PRICE_CHECK_2);
                }

                // dateCheck
                if (StringUtils.isNotBlank(appendedBo.getDlvschedDate()) && (!DateRangeChecker.isWithinPastThreeMonths(appendedBo.getDlvschedDate()) ||
                        !DateRangeChecker.isWithinNextMonth(appendedBo.getDlvschedDate()))) {
                    checkResult.add(true);
                    dateFlag.add(true);
                    appendedBo.setErrorMsg((StringUtil.isNotBlank(appendedBo.getErrorMsg()) ? appendedBo.getErrorMsg() : "") + SysConstantInfo.DLVSCHE_CHECK);
                }
            }
        }
        if (checkResult.contains(true)) {
            fileBackErrorVo.setFileError(downloadErrorFile(resList));
            resList = new ArrayList<>();
        }
        fileBackErrorVo.setOtherFlag(otherFlag.contains(true));
        fileBackErrorVo.setPriceFlag(priceFlag.contains(true));
        fileBackErrorVo.setDateFlag(dateFlag.contains(true));
        return resList;
    }

    private List<AppendedUpdateBo> checkUpdateFileData(Integer centerId, List<AppendedUpdateBo> resList, Boolean warningFlag, FileBackErrorVo fileBackErrorVo) {
        // getMstList
        CommonMailNoBo bo = new CommonMailNoBo();
        bo.setCenterId(centerId);
        List<MailListVo> mailListVos = masterService.getMailList(bo);
        List<MtPlaceVo> mtPlaceVos = masterService.getPlaceList();
        // start check
        Set<Boolean> checkResult = new HashSet<>();
        Set<Boolean> priceFlag = new HashSet<>();
        Set<Boolean> dateFlag = new HashSet<>();
        Set<Boolean> otherFlag = new HashSet<>();
        for (AppendedUpdateBo appendedUpdateBo : resList) {
            replaceCommaUpdate(appendedUpdateBo);
            // required
            List<String> list = checkMustListUpdate(appendedUpdateBo);
            if (!list.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedUpdateBo.setErrorMsg(SysConstantInfo.PARAM_NOT_NULL_MSG);
                continue;
            }
            // format
            HashSet<String> formatList = checkFormatListUpdate(appendedUpdateBo);
            if (!formatList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedUpdateBo.setErrorMsg(StrFormatter.format(SysConstantInfo.INCORRECT, Joiner.on("、").join(formatList)));
                continue;
            }
            // center as
//            if (!centerId.equals(Integer.parseInt(appendedUpdateBo.getCenterId()))) {
//                checkResult.add(true);
//                otherFlag.add(true);
//                appendedUpdateBo.setErrorMsg(SysConstantInfo.CENTER_FRAME_ERROR);
//                continue;
//            }

            //resetData
            if (StringUtils.isBlank(appendedUpdateBo.getQuantity())) {
                appendedUpdateBo.setQuantity(NumberConstants.NUM_0);
            }

            if (StringUtils.isBlank(appendedUpdateBo.getWeight())) {
                appendedUpdateBo.setWeight(NumberConstants.NUM_0);
            }

            // mst
            List<String> mstList = checkMstListUpdate(appendedUpdateBo, mailListVos, mtPlaceVos);
            if (!mstList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedUpdateBo.setErrorMsg(StrFormatter.format(SysConstantInfo.PARAM_NOT_EXISTENT_MSG, Joiner.on("、").join(mstList)));
                continue;
            }

            // Check transmission
            boolean transmission = checkTransmission(appendedUpdateBo);
            if (transmission) {
                checkResult.add(true);
                otherFlag.add(true);
                appendedUpdateBo.setErrorMsg(SysConstantInfo.TRANSMISSION);
                continue;
            }

            // Check 20
//            boolean mailNo = checkMailNo(appendedUpdateBo);
//            if (mailNo) {
//                checkResult.add(true);
//                otherFlag.add(true);
//                appendedUpdateBo.setErrorMsg(SysConstantInfo.MAIL_NO_20);
//                continue;
//            }


            if (warningFlag) {
                // priceCheck
                if (StringUtils.isNotBlank(appendedUpdateBo.getCostRcp()) && StringUtils.isNotBlank(appendedUpdateBo.getCost()) && new BigDecimal(appendedUpdateBo.getCostRcp()).compareTo(new BigDecimal(appendedUpdateBo.getCost())) > 0) {
                    priceFlag.add(true);
                    checkResult.add(true);
                    appendedUpdateBo.setErrorMsg((StringUtil.isNotBlank(appendedUpdateBo.getErrorMsg()) ? appendedUpdateBo.getErrorMsg() : "") + SysConstantInfo.PRICE_CHECK_1);
                }

                if (StringUtils.isNotBlank(appendedUpdateBo.getCost()) && StringUtils.isNotBlank(appendedUpdateBo.getPrice()) && new BigDecimal(appendedUpdateBo.getCost()).compareTo(new BigDecimal(appendedUpdateBo.getPrice())) > 0) {
                    priceFlag.add(true);
                    checkResult.add(true);
                    appendedUpdateBo.setErrorMsg((StringUtil.isNotBlank(appendedUpdateBo.getErrorMsg()) ? appendedUpdateBo.getErrorMsg() : "") + SysConstantInfo.PRICE_CHECK_2);
                }

                // dateCheck
                if (StringUtils.isNotBlank(appendedUpdateBo.getDlvschedDate()) && (!DateRangeChecker.isWithinPastThreeMonths(appendedUpdateBo.getDlvschedDate()) ||
                        !DateRangeChecker.isWithinNextMonth(appendedUpdateBo.getDlvschedDate()))) {
                    checkResult.add(true);
                    dateFlag.add(true);
                    appendedUpdateBo.setErrorMsg((StringUtil.isNotBlank(appendedUpdateBo.getErrorMsg()) ? appendedUpdateBo.getErrorMsg() : "") + SysConstantInfo.DLVSCHE_CHECK);
                }
            }
        }
        if (checkResult.contains(true)) {
            fileBackErrorVo.setFileError(downloadErrorFileUpdate(resList));
            resList = new ArrayList<>();
        }
        fileBackErrorVo.setOtherFlag(otherFlag.contains(true));
        fileBackErrorVo.setPriceFlag(priceFlag.contains(true));
        fileBackErrorVo.setDateFlag(dateFlag.contains(true));
        return resList;
    }

    private boolean checkMailNo(AppendedUpdateBo appendedUpdateBo) {
        return Integer.parseInt(appendedUpdateBo.getMailNo()) == 20;
    }

    private boolean checkTransmission(AppendedUpdateBo appendedUpdateBo) {
        QueryWrapper<WkOdrTransbill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("transbill_code", Integer.parseInt(appendedUpdateBo.getSlipCode()));
        queryWrapper.eq("trans_date", DateUtil.parseDate(appendedUpdateBo.getDlvschedDate()));
        List<WkOdrTransbill> wkOdrTransbills = wkOdrTransbillMapper.selectList(queryWrapper);
        return wkOdrTransbills.isEmpty() ||
                !wkOdrTransbills.stream().filter(a -> a.getFSend() != 0).collect(Collectors.toList()).isEmpty();
    }

    private static void replaceComma(AppendedBo appendedBo) {
        if (StringUtil.isNotBlank(appendedBo.getQuantity())) {
            appendedBo.setQuantity(appendedBo.getQuantity().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedBo.getWeight())) {
            appendedBo.setWeight(appendedBo.getWeight().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedBo.getCostRcp())) {
            appendedBo.setCostRcp(appendedBo.getCostRcp().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedBo.getCost())) {
            appendedBo.setCost(appendedBo.getCost().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedBo.getPrice())) {
            appendedBo.setPrice(appendedBo.getPrice().replace(",", ""));
        }
        appendedBo.setErrorMsg(null);
    }

    private static void replaceCommaUpdate(AppendedUpdateBo appendedUpdateBo) {
        if (StringUtil.isNotBlank(appendedUpdateBo.getQuantity())) {
            appendedUpdateBo.setQuantity(appendedUpdateBo.getQuantity().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedUpdateBo.getWeight())) {
            appendedUpdateBo.setWeight(appendedUpdateBo.getWeight().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedUpdateBo.getCostRcp())) {
            appendedUpdateBo.setCostRcp(appendedUpdateBo.getCostRcp().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedUpdateBo.getCost())) {
            appendedUpdateBo.setCost(appendedUpdateBo.getCost().replace(",", ""));
        }
        if (StringUtil.isNotBlank(appendedUpdateBo.getPrice())) {
            appendedUpdateBo.setPrice(appendedUpdateBo.getPrice().replace(",", ""));
        }
        appendedUpdateBo.setErrorMsg(null);
    }

    private void setSlipCodeAndLineNo(List<AppendedBo> resList) {
        resList.sort(Comparator.comparing(AppendedBo::getMailNo).thenComparing(AppendedBo::getStoreId).thenComparing(AppendedBo::getDlvschedDate));

        AtomicInteger dLineNo = new AtomicInteger(1);
        AtomicReference<Date> dDlvschedDate = new AtomicReference<>(new Date());
        AtomicInteger dMailNo = new AtomicInteger();
        AtomicInteger dStoreId = new AtomicInteger();
        AtomicInteger dDepartmentId = new AtomicInteger();
        AtomicInteger dFirst = new AtomicInteger(1);
        AtomicInteger dSlipNo = new AtomicInteger();
        resList.stream().filter(a -> Integer.parseInt(a.getQuantity()) < 0 && StringUtils.isBlank(a.getSlipCode())).forEach(a -> {
            if (dLineNo.get() > 9 || !dDlvschedDate.get().equals(DateUtil.parseDate(a.getDlvschedDate())) || dMailNo.get() != Integer.parseInt(a.getMailNo()) || dStoreId.get() != Integer.parseInt(a.getStoreId()) || dDepartmentId.get() != a.getDepartmentId() || dFirst.get() == 1) {
                dLineNo.set(1);
                dSlipNo.set(iMtCenterstatusService.getSlip(Integer.parseInt(a.getCenterId())));
                dFirst.set(0);
            }
            a.setSlipCode(String.valueOf(dSlipNo.get()));
            a.setLineNo(String.valueOf(dLineNo.get()));

            dLineNo.getAndIncrement();
            dDlvschedDate.set(DateUtil.parseDate(a.getDlvschedDate()));
            dMailNo.set(Integer.parseInt(a.getMailNo()));
            dStoreId.set(Integer.parseInt(a.getStoreId()));
            dDepartmentId.set(a.getDepartmentId());
        });

        AtomicInteger lLineNo = new AtomicInteger(1);
        AtomicReference<Date> lDlvschedDate = new AtomicReference<>(new Date());
        AtomicInteger lMailNo = new AtomicInteger();
        AtomicInteger lStoreId = new AtomicInteger();
        AtomicInteger lDepartmentId = new AtomicInteger();
        AtomicInteger lFirst = new AtomicInteger(1);
        AtomicInteger lSlipNo = new AtomicInteger();
        resList.stream().filter(a -> Integer.parseInt(a.getQuantity()) >= 0 && StringUtils.isBlank(a.getSlipCode())).forEach(a -> {
            if (lLineNo.get() > 9 || !lDlvschedDate.get().equals(DateUtil.parseDate(a.getDlvschedDate())) || lMailNo.get() != Integer.parseInt(a.getMailNo()) || lStoreId.get() != Integer.parseInt(a.getStoreId()) || lDepartmentId.get() != a.getDepartmentId() || lFirst.get() == 1) {
                lLineNo.set(1);
                lSlipNo.set(iMtCenterstatusService.getSlip(Integer.parseInt(a.getCenterId())));
                lFirst.set(0);
            }
            a.setSlipCode(String.valueOf(lSlipNo.get()));
            a.setLineNo(String.valueOf(lLineNo.get()));

            lLineNo.getAndIncrement();
            lDlvschedDate.set(DateUtil.parseDate(a.getDlvschedDate()));
            lMailNo.set(Integer.parseInt(a.getMailNo()));
            lStoreId.set(Integer.parseInt(a.getStoreId()));
            lDepartmentId.set(a.getDepartmentId());
        });
    }

    private List<String> checkMstList(AppendedBo appendedBo, List<MailListVo> mailListVos, List<BranchListVo> branchVos, List<MtPlaceVo> placeVos) {
        List<String> list = new ArrayList<>();
        if (Integer.parseInt(appendedBo.getMailNo()) != 20
                && mailListVos.stream().noneMatch(s -> s.getMailNo() == Integer.parseInt(appendedBo.getMailNo()))) {
            list.add("[便]");
        }
        if (branchVos.stream().noneMatch(s -> s.getBranchcd() == Integer.parseInt(appendedBo.getStoreId()))) {
            list.add("[店舗コード]");
        }
        if (StringUtils.isNotBlank(appendedBo.getPlaceId()) && placeVos.stream().noneMatch(s -> s.getId().toString().equals(appendedBo.getPlaceId()))) {
            list.add("[産地コード]");
        }
        return list;
    }

    private List<String> checkMstListUpdate(AppendedUpdateBo appendedUpdateBo, List<MailListVo> mailListVos, List<MtPlaceVo> placeVos) {
        List<String> list = new ArrayList<>();
        if (Integer.parseInt(appendedUpdateBo.getMailNo()) != 20
                && mailListVos.stream().noneMatch(s -> s.getMailNo() == Integer.parseInt(appendedUpdateBo.getMailNo()))) {
            list.add("[便]");
        }
        if (StringUtils.isNotBlank(appendedUpdateBo.getPlaceId()) && placeVos.stream().noneMatch(s -> s.getId().toString().equals(appendedUpdateBo.getPlaceId()))) {
            list.add("[産地コード]");
        }
        return list;
    }

    private List<String> checkMustList(AppendedBo a) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isBlank(a.getCenterId())) {
            list.add("[センターコード]");
        }
        if (!(StringUtils.isBlank(a.getSlipCode()) && StringUtils.isBlank(a.getLineNo()))) {
            if (StringUtils.isBlank(a.getSlipCode())) {
                list.add("[伝票番号]");
            }
            if (StringUtils.isBlank(a.getLineNo())) {
                list.add("[行No]");
            }
        }
        if (StringUtils.isBlank(a.getDlvschedDate())) {
            list.add("[納品予定日]");
        }
        if (StringUtils.isBlank(a.getItemId()) && StringUtils.isBlank(a.getCallCode())) {
            list.add("[品番とJAN]");
        }
        if (StringUtils.isBlank(a.getMailNo())) {
            list.add("[便]");
        }
        if (StringUtils.isBlank(a.getStoreId())) {
            list.add("[店舗コード]");
        }
        if (StringUtils.isBlank(a.getQuantity())) {
            list.add("[数量]");
        }
        return list;
    }

    private List<String> checkMustListUpdate(AppendedUpdateBo a) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isBlank(a.getCenterId())) {
            list.add("[センターコード]");
        }
        if (!(StringUtils.isBlank(a.getSlipCode()) && StringUtils.isBlank(a.getLineNo()))) {
            if (StringUtils.isBlank(a.getSlipCode())) {
                list.add("[伝票番号]");
            }
            if (StringUtils.isBlank(a.getLineNo())) {
                list.add("[行No]");
            }
        }
        if (StringUtils.isBlank(a.getDlvschedDate())) {
            list.add("[納品予定日]");
        }
        if (StringUtils.isBlank(a.getItemId()) && StringUtils.isBlank(a.getCallCode())) {
            list.add("[品番とJAN]");
        }
        if (StringUtils.isBlank(a.getMailNo())) {
            list.add("[便]");
        }
        if (StringUtils.isBlank(a.getStoreId())) {
            list.add("[店舗コード]");
        }
        if (StringUtils.isBlank(a.getQuantity())) {
            list.add("[数量]");
        }
        return list;
    }

    private HashSet<String> checkFormatList(AppendedBo appendedBo) {
        HashSet<String> list = new HashSet<>();
        if (StringUtils.isNotBlank(appendedBo.getSlipCode()) && (appendedBo.getSlipCode().length() > 9 || !appendedBo.getSlipCode().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("伝票番号");
        }
        if (StringUtils.isNotBlank(appendedBo.getLineNo()) && (!appendedBo.getLineNo().matches(FormatConstants.REGEX_PATTERN_NUMBER) || Integer.parseInt(appendedBo.getLineNo()) > 9 || Integer.parseInt(appendedBo.getLineNo()) < 1)) {
            list.add("行番号");
        }
        if (!appendedBo.getCenterId().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("センターコード");
        }
        if (StringUtils.isNotBlank(appendedBo.getCallCode()) && (appendedBo.getCallCode().length() > 6 || !appendedBo.getCallCode().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("品番");
        }
        if (!appendedBo.getStoreId().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("店舗コード");
        }
        if (!appendedBo.getDlvschedDate().matches(FormatConstants.REGEX_PATTERN_DATE)) {
            list.add("納品予定日（yyyy/MM/dd）");
        }
        if (StringUtils.isNotBlank(appendedBo.getItemId()) && (appendedBo.getItemId().length() > 13 || !appendedBo.getItemId().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("JAN");
        }
        if (StringUtils.isNotBlank(appendedBo.getQuantity()) && (appendedBo.getQuantity().length() > 5 || !appendedBo.getQuantity().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("数量");
        }
        if (StringUtils.isNotBlank(appendedBo.getPlaceId()) && (String.valueOf(appendedBo.getPlaceId()).length() > 4 || !appendedBo.getPlaceId().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("産地コード");
        }
        if (StringUtils.isNotBlank(appendedBo.getLotNo()) && (String.valueOf(appendedBo.getLotNo()).length() > 12 || !appendedBo.getLotNo().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("ロットNo");
        }
        if (StringUtils.isNotBlank(appendedBo.getCostRcp()) && (!appendedBo.getCostRcp().matches(FormatConstants.REGEX_PATTERN_MONEY) || appendedBo.getCostRcp().length() > 9)) {
            list.add("PC原価");
        }
        if (StringUtils.isNotBlank(appendedBo.getCost()) && (!appendedBo.getCost().matches(FormatConstants.REGEX_PATTERN_MONEY) || appendedBo.getCost().length() > 9)) {
            list.add("店原価");
        }
        if (StringUtils.isNotBlank(appendedBo.getPrice()) && (!appendedBo.getPrice().matches(FormatConstants.REGEX_PATTERN_MONEY) || appendedBo.getPrice().length() > 6)) {
            list.add("店売価");
        }
        // JAN、店舗/地域
        if (!list.contains("店舗コード") && !list.contains("JAN")) {
            boolean flag = true;
            if (StringUtils.isNotBlank(appendedBo.getCallCode()) && !list.contains("品番")) {
                List<TmShohinVo> tmShohinVos = tmShoinService.findJanByCallCodeAll(Integer.parseInt(appendedBo.getStoreId()), Integer.parseInt(appendedBo.getCallCode()));
                if (tmShohinVos.isEmpty()) {
                    list.add("品番と店舗");
                    flag = false;
                } else {
                    if (StringUtils.isNotBlank(appendedBo.getItemId()) && !list.contains("JAN")) {
                        List<TmShohinVo> tmShohinVoList = tmShohinVos.stream().filter(s -> s.getItemId().equals(appendedBo.getItemId())).collect(Collectors.toList());
                        if (tmShohinVoList.isEmpty()) {
                            list.add("品番とJAN");
                            flag = false;
                        } else {
                            extracted(appendedBo, list, tmShohinVoList);
                        }
                    } else {
                        List<TmShohinVo> tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId() == Integer.parseInt(appendedBo.getStoreId())).collect(Collectors.toList());
                        if (tmShohinVoList.isEmpty()) {
                            tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId().equals(0)).collect(Collectors.toList());
                        }
                        appendedBo.setItemId(tmShohinVoList.get(0).getItemId());
                        extracted(appendedBo, list, tmShohinVoList);
                    }
                }
            }
            if (flag && StringUtils.isNotBlank(appendedBo.getItemId()) && !list.contains("JAN")) {
                List<TmShohinVo> tmShohinVoList = tmShoinService.findJanStoreAll(Integer.parseInt(appendedBo.getStoreId()), appendedBo.getItemId());
                if (tmShohinVoList.isEmpty()) {
                    list.add("JANと店舗");
                } else {
                    extracted(appendedBo, list, tmShohinVoList);
                }
            }
        }
        return list;
    }

    private static void extracted(AppendedBo appendedBo, HashSet<String> list, List<TmShohinVo> tmShohinVoList) {
        appendedBo.setDepartmentId(tmShohinVoList.get(0).getDepartmentId());
        appendedBo.setTeikanTypeid(String.valueOf(tmShohinVoList.get(0).getTeikanTypeid()));
        appendedBo.setAreaId(tmShohinVoList.get(0).getAreaId());
        appendedBo.setLineId(tmShohinVoList.get(0).getLineId());
        if (!appendedBo.getTeikanTypeid().equals("0") && StringUtil.isBlank(appendedBo.getWeight())) {
            appendedBo.setWeight("0");
        }
        if ((appendedBo.getTeikanTypeid().equals("0") && StringUtil.isBlank(appendedBo.getWeight())) || !appendedBo.getWeight().matches(FormatConstants.REGEX_PATTERN_WEIGHT) || appendedBo.getWeight().length() > 9) {
            list.add("重量");
        }
        if (appendedBo.getTeikanTypeid().equals("0") && !list.contains("重量")) {
            if ((Double.parseDouble(appendedBo.getWeight()) > NumberConstants.NUM_INT_0 && Double.parseDouble(appendedBo.getQuantity()) <= NumberConstants.NUM_INT_0)
                    || (Double.parseDouble(appendedBo.getWeight()) == NumberConstants.NUM_INT_0 && Double.parseDouble(appendedBo.getQuantity()) != NumberConstants.NUM_INT_0)) {
                list.add("数量と重量");
            }
        }
    }

    private static void extractedUpdate(AppendedUpdateBo updateBo, HashSet<String> list, List<TmShohinVo> tmShohinVoList) {
        updateBo.setDepartmentId(tmShohinVoList.get(0).getDepartmentId());
        updateBo.setTeikanTypeid(String.valueOf(tmShohinVoList.get(0).getTeikanTypeid()));
        updateBo.setAreaId(tmShohinVoList.get(0).getAreaId());
        if (!updateBo.getTeikanTypeid().equals("0") && StringUtil.isBlank(updateBo.getWeight())) {
            updateBo.setWeight("0");
        }
        if ((updateBo.getTeikanTypeid().equals("0") && StringUtil.isBlank(updateBo.getWeight())) || !updateBo.getWeight().matches(FormatConstants.REGEX_PATTERN_WEIGHT) || updateBo.getWeight().length() > 9) {
            list.add("重量");
        }
        if (updateBo.getTeikanTypeid().equals("0") && !list.contains("重量")) {
            if ((Double.parseDouble(updateBo.getWeight()) > NumberConstants.NUM_INT_0 && Integer.parseInt(updateBo.getQuantity()) <= NumberConstants.NUM_INT_0)
                    || (Double.parseDouble(updateBo.getWeight()) == NumberConstants.NUM_INT_0 && Integer.parseInt(updateBo.getQuantity()) != NumberConstants.NUM_INT_0)) {
                list.add("数量と重量");
            }
        }
    }

    private HashSet<String> checkFormatListUpdate(AppendedUpdateBo a) {
        HashSet<String> list = new HashSet<>();
        if (StringUtils.isNotBlank(a.getSlipCode()) && (a.getSlipCode().length() > 9 || !a.getSlipCode().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("伝票番号");
        }
        if (StringUtils.isNotBlank(a.getLineNo()) && (!a.getLineNo().matches(FormatConstants.REGEX_PATTERN_NUMBER) || Integer.parseInt(a.getLineNo()) > 9 || Integer.parseInt(a.getLineNo()) < 1)) {
            list.add("行番号");
        }
        if (!a.getCenterId().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("センターコード");
        }
        if (a.getCallCode().length() > 6 || !a.getCallCode().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("品番");
        }
        if (!a.getStoreId().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("店舗コード");
        }
        if (!a.getDlvschedDate().matches(FormatConstants.REGEX_PATTERN_DATE)) {
            list.add("納品予定日（yyyy/MM/dd）");
        }
        if (a.getItemId().length() > 13 || !a.getItemId().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("JAN");
        }
        if (StringUtils.isNotBlank(a.getQuantity()) && (a.getQuantity().length() > 5 || !a.getQuantity().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("数量");
        }
        if (StringUtils.isNotBlank(a.getPlaceId()) && (String.valueOf(a.getPlaceId()).length() > 4 || !a.getPlaceId().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("産地コード");
        }
        if (StringUtils.isNotBlank(a.getLotNo()) && (String.valueOf(a.getLotNo()).length() > 12 || !a.getLotNo().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("ロットNo");
        }
        if (StringUtils.isNotBlank(a.getCostRcp()) && (!a.getCostRcp().matches(FormatConstants.REGEX_PATTERN_MONEY) || a.getCostRcp().length() > 9)) {
            list.add("PC原価");
        }
        if (StringUtils.isNotBlank(a.getCost()) && (!a.getCost().matches(FormatConstants.REGEX_PATTERN_MONEY) || a.getCost().length() > 9)) {
            list.add("店原価");
        }
        if (StringUtils.isNotBlank(a.getPrice()) && (!a.getPrice().matches(FormatConstants.REGEX_PATTERN_MONEY) || a.getPrice().length() > 6)) {
            list.add("店売価");
        }

        // JAN、店舗/地域
        if (!list.contains("店舗コード") && !list.contains("JAN")) {
            boolean flag = true;
            if (StringUtils.isNotBlank(a.getCallCode()) && !list.contains("品番")) {
                List<TmShohinVo> tmShohinVos = tmShoinService.findJanByCallCodeAll(Integer.parseInt(a.getStoreId()), Integer.parseInt(a.getCallCode()));
                if (tmShohinVos.isEmpty()) {
                    list.add("品番と店舗");
                    flag = false;
                } else {
                    if (StringUtils.isNotBlank(a.getItemId()) && !list.contains("JAN")) {
                        List<TmShohinVo> tmShohinVoList = tmShohinVos.stream().filter(s -> s.getItemId().equals(a.getItemId())).collect(Collectors.toList());
                        if (tmShohinVoList.isEmpty()) {
                            list.add("品番とJAN");
                            flag = false;
                        } else {
                            extractedUpdate(a, list, tmShohinVoList);
                        }
                    } else {
                        List<TmShohinVo> tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId() == Integer.parseInt(a.getStoreId())).collect(Collectors.toList());
                        if (tmShohinVoList.isEmpty()) {
                            tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId().equals(0)).collect(Collectors.toList());
                        }
                        a.setItemId(tmShohinVoList.get(0).getItemId());
                        extractedUpdate(a, list, tmShohinVoList);
                    }
                }
            }
            if (flag && StringUtils.isNotBlank(a.getItemId()) && !list.contains("JAN")) {
                List<TmShohinVo> tmShohinVoList = tmShoinService.findJanStoreAll(Integer.parseInt(a.getStoreId()), a.getItemId());
                if (tmShohinVoList.isEmpty()) {
                    list.add("JANと店舗");
                } else {
                    extractedUpdate(a, list, tmShohinVoList);
                }
            }
        }
        return list;
    }

    private String downloadErrorFile(List<AppendedBo> resList) {
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = StrFormatter.format(SysConstantInfo.FMT_FILE_NAME_WITH_TIME_AND_ERROR_MSG, SysConstants.APPENDED_FILE_NAME, curTime, FileTypeConstants.XLSX);
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), AppendedBo.class)
                .withTemplate(simpleFmtTableUtils.getFmtFileInputStream("appended"))
                .registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                ).needHead(false).sheet().doWrite(resList);
        return fileName;
    }

    private String downloadErrorFileUpdate(List<AppendedUpdateBo> resList) {
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = StrFormatter.format(SysConstantInfo.FMT_FILE_NAME_WITH_TIME_AND_ERROR_MSG, SysConstants.APPENDED_UPDATE_FILE_NAME, curTime, FileTypeConstants.XLSX);
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), AppendedUpdateBo.class)
                .withTemplate(simpleFmtTableUtils.getFmtFileInputStream("appendedUpdate"))
                .registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                ).needHead(false).sheet().doWrite(resList);
        return fileName;
    }

    private void downloadFmtDataFile(List<AppendedUpdateVo> appendedUpdateVoList, HttpServletResponse response) throws IOException {
        appendedUpdateVoList.forEach(a -> a.setDlvschedDateFmt(DateUtil.format(a.getDlvschedDate(), "yyyy/MM/dd")));
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = SysConstants.APPENDED_UPDATE_FILE_NAME + "_" + curTime;
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), AppendedUpdateVo.class)
                .withTemplate(simpleFmtTableUtils.getFmtFileInputStream("appendedUpdate")).registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                ).needHead(false).sheet().doWrite(appendedUpdateVoList);
        simpleFmtTableUtils.downloadTempFile(response, fileName, FileTypeConstants.CONTENT_TYPE_XLSX);
    }

    private Map<String, Object> sum(List<TrOutTransitem> trOutTransitems) {
        Map<String, Object> map = new HashMap<>();
        map.put(QY_AM, trOutTransitems.stream().filter(Objects::nonNull).mapToInt(TrOutTransitem::getQy).sum());
        map.put(WEIGHT_AM, trOutTransitems.stream().map(TrOutTransitem::getWeightAm).reduce(BigDecimal.ZERO, BigDecimalUtils::sum));
        map.put(R_AM, trOutTransitems.stream().map(TrOutTransitem::getRecipeAm).reduce(BigDecimal.ZERO, BigDecimalUtils::sum));
        map.put(O_AM, trOutTransitems.stream().map(TrOutTransitem::getOrderAm).reduce(BigDecimal.ZERO, BigDecimalUtils::sum));
        map.put(S_AM, trOutTransitems.stream().filter(Objects::nonNull).mapToLong(TrOutTransitem::getSalesAm).sum());
        return map;
    }

    private Map<String, BigDecimal> calcPrice(int typeId, BigDecimal costRcp, BigDecimal cost, Integer price, BigDecimal weight, Integer quantity) {
        Map<String, BigDecimal> map = new HashMap<>();
        // 不定貫
        if (typeId == 0) {
            map.put(RECIPE_AM, (costRcp.multiply(weight)).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP));
            map.put(ORDER_AM, (cost.multiply(weight)).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP));
            map.put(SALES_AM, (BigDecimal.valueOf(price).multiply(weight)).divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP));
            if (quantity == 0) {
                map.put(RECIPE, BigDecimal.valueOf(0));
                map.put(ORDER, BigDecimal.valueOf(0));
                map.put(SALES, BigDecimal.valueOf(0));
            } else {
                map.put(RECIPE, map.get(RECIPE_AM).divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP));
                map.put(ORDER, map.get(ORDER_AM).divide(BigDecimal.valueOf(quantity), 2, RoundingMode.HALF_UP));
                map.put(SALES, map.get(SALES_AM).divide(BigDecimal.valueOf(quantity), 0, RoundingMode.HALF_UP));

                map.put(RECIPE_AM, map.get(RECIPE).multiply(BigDecimal.valueOf(quantity)).divide(BigDecimal.valueOf(1), 0, RoundingMode.HALF_UP));
                map.put(ORDER_AM, map.get(ORDER).multiply(BigDecimal.valueOf(quantity)).divide(BigDecimal.valueOf(1), 0, RoundingMode.HALF_UP));
                map.put(SALES_AM, map.get(SALES).multiply(BigDecimal.valueOf(quantity)).divide(BigDecimal.valueOf(1), 0, RoundingMode.HALF_UP));
            }
        } else {
            map.put(RECIPE_AM, costRcp.multiply((BigDecimal.valueOf(quantity))).setScale(0, RoundingMode.HALF_UP));
            map.put(ORDER_AM, cost.multiply((BigDecimal.valueOf(quantity))).setScale(0, RoundingMode.HALF_UP));
            map.put(SALES_AM, BigDecimal.valueOf(price).multiply((BigDecimal.valueOf(quantity))).setScale(0, RoundingMode.HALF_UP));
            map.put(RECIPE, costRcp);
            map.put(ORDER, cost);
            map.put(SALES, BigDecimal.valueOf(price));
        }
        return map;
    }

}
