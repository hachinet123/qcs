package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrFormatter;
import com.alibaba.excel.EasyExcelFactory;
import com.google.common.base.Joiner;
import com.tre.centralkitchen.common.constant.*;
import com.tre.centralkitchen.common.constant.business.FixAmountResultModifyConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.excel.GetResultListReadListener;
import com.tre.centralkitchen.common.utils.*;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultModifyBo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultModifyUploadBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.po.MtMailcontrol;
import com.tre.centralkitchen.domain.vo.common.*;
import com.tre.centralkitchen.domain.vo.system.FixAmountResultModifyPoVo;
import com.tre.centralkitchen.domain.vo.system.FixAmountResultModifyVo;
import com.tre.centralkitchen.domain.vo.system.MtMailcontrolVo;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.mapper.FixAmountResultModifyMapper;
import com.tre.centralkitchen.mapper.MtMailcontrolMapper;
import com.tre.centralkitchen.service.AuthorityService;
import com.tre.centralkitchen.service.FixAmountResultModifyService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 10225441
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FixAmountResultModifyServiceImpl implements FixAmountResultModifyService {

    private final TmShoinServiceImpl tmShoinService;
    private final FixAmountResultModifyMapper fixAmountResultModifyMapper;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final TokenTakeApart tokenTakeApart;
    private final MasterService masterService;
    private final MtMailcontrolMapper controlMapper;
    private final AuthorityService authorityService;

    //    1　計画あり
    //    2　計画なし
    //    3　修正済
    //    4　計画あり　OR　計画なし
    //    5　計画あり　OR　修正済
    //    6　計画なし　OR　修正済
    private static int getFlag(FixAmountResultModifyBo param) {
        int flag = 0;
        if (param.getHasProductPlan() == NumberConstants.NUM_INT_1 && param.getHasNoProductPlan() == NumberConstants.NUM_INT_0 && param.getHasAlreadyUpdate() == NumberConstants.NUM_INT_0) {
            flag = 1;
        } else if (param.getHasProductPlan() == NumberConstants.NUM_INT_0 && param.getHasNoProductPlan() == NumberConstants.NUM_INT_1 && param.getHasAlreadyUpdate() == NumberConstants.NUM_INT_0) {
            flag = 2;
        } else if (param.getHasProductPlan() == NumberConstants.NUM_INT_0 && param.getHasNoProductPlan() == NumberConstants.NUM_INT_0 && param.getHasAlreadyUpdate() == NumberConstants.NUM_INT_1) {
            flag = 3;
        } else if (param.getHasProductPlan() == NumberConstants.NUM_INT_1 && param.getHasNoProductPlan() == NumberConstants.NUM_INT_1 && param.getHasAlreadyUpdate() == NumberConstants.NUM_INT_0) {
            flag = 4;
        } else if (param.getHasProductPlan() == NumberConstants.NUM_INT_1 && param.getHasNoProductPlan() == NumberConstants.NUM_INT_0 && param.getHasAlreadyUpdate() == NumberConstants.NUM_INT_1) {
            flag = 5;
        } else if (param.getHasProductPlan() == NumberConstants.NUM_INT_0 && param.getHasNoProductPlan() == NumberConstants.NUM_INT_1 && param.getHasAlreadyUpdate() == NumberConstants.NUM_INT_1) {
            flag = 6;
        }
        return flag;
    }

    private static void replaceComma(FixAmountResultModifyUploadBo fixAmountResultModifyUploadBo) {
        if (StringUtil.isNotBlank(fixAmountResultModifyUploadBo.getQty())) {
            fixAmountResultModifyUploadBo.setQty(fixAmountResultModifyUploadBo.getQty().replace(",", ""));
        }
        if (StringUtil.isNotBlank(fixAmountResultModifyUploadBo.getWeight())) {
            fixAmountResultModifyUploadBo.setWeight(fixAmountResultModifyUploadBo.getWeight().replace(",", ""));
        }
        if (StringUtil.isNotBlank(fixAmountResultModifyUploadBo.getPcPrice())) {
            fixAmountResultModifyUploadBo.setPcPrice(fixAmountResultModifyUploadBo.getPcPrice().replace(",", ""));
        }
        if (StringUtil.isNotBlank(fixAmountResultModifyUploadBo.getItemPrice())) {
            fixAmountResultModifyUploadBo.setItemPrice(fixAmountResultModifyUploadBo.getItemPrice().replace(",", ""));
        }
        if (StringUtil.isNotBlank(fixAmountResultModifyUploadBo.getPrice())) {
            fixAmountResultModifyUploadBo.setPrice(fixAmountResultModifyUploadBo.getPrice().replace(",", ""));
        }
        fixAmountResultModifyUploadBo.setErrorMsg(null);
    }

    @Override
    public TableDataInfo<FixAmountResultModifyPoVo> queryFixAmountResultModify(PageQuery pageParam, FixAmountResultModifyBo param) {
        queryParameter(param);
        int flag = getFlag(param);
        return TableDataInfo.build(fixAmountResultModifyMapper.selectFixAmountResultModify(pageParam.build(), param, flag));
    }

    private void queryParameter(FixAmountResultModifyBo param) {
        if (!param.getMailNo().isBlank()) {
            param.setMailIdList(Arrays.stream(param.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList()));
        } else {
            param.setMailIdList(new ArrayList<>());
        }
        if (!param.getLineId().isBlank()) {
            param.setLineIdList(Arrays.stream(param.getLineId().split(StringConstants.COMMA)).distinct().map(Integer::parseInt).collect(Collectors.toList()));
        } else {
            param.setLineIdList(new ArrayList<>());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFixAmountResultModify(List<FixAmountResultModifyPoVo> paramList) {
        paramList.forEach(a -> {
            if (StringUtils.isBlank(a.getQty())) {
                a.setQty("0");
            }
            if (StringUtils.isBlank(a.getWeight())) {
                a.setWeight("0");
            }
            if (StringUtils.isBlank(a.getPcPrice())) {
                a.setPcPrice("0");
            }
            if (StringUtils.isBlank(a.getItemPrice())) {
                a.setItemPrice("0");
            }
            if (StringUtils.isBlank(a.getPrice())) {
                a.setPrice("0");
            }

            List<TmShohinVo> tmShohinVoList = tmShoinService.findJanStore(Integer.parseInt(a.getStoreId()), a.getItemId());
            if (tmShohinVoList.isEmpty()) {
                throw new SysBusinessException(SysConstantInfo.JAN_STORE_ERROR);
            } else {
                if (tmShohinVoList.get(0).getTeikanTypeid() == 0) {
                    if (Double.parseDouble(a.getWeight()) > NumberConstants.NUM_INT_0 && Integer.parseInt(a.getQty()) <= NumberConstants.NUM_INT_0) {
                        throw new SysBusinessException(StrFormatter.format(FixAmountResultModifyConstants.ERROR_MSG_FILL_EXIST, FixAmountResultModifyConstants.EXCEL_COL_QTY));
                    }
                    if (Double.parseDouble(a.getWeight()) == NumberConstants.NUM_INT_0 && Integer.parseInt(a.getQty()) != NumberConstants.NUM_INT_0) {
                        throw new SysBusinessException(StrFormatter.format(FixAmountResultModifyConstants.ERROR_MSG_FILL_EXIST_0, FixAmountResultModifyConstants.EXCEL_COL_QTY));
                    }
                }
            }
        });
        fixAmountResultModifyMapper.updateFixAmountResultModifyPoBatch(tokenTakeApart.takeDecryptedUserId(), paramList, FunType.REGISTERED.getCode(), OpeType.CORRECTION_INDICATION.getCode());
    }

    @Override
    public void downloadFixAmountResultModifyCsv(PageQuery pageParam, FixAmountResultModifyBo param, HttpServletResponse response) {
        queryParameter(param);
        int flag = getFlag(param);
        List<FixAmountResultModifyPoVo> res = fixAmountResultModifyMapper.selectFixAmountResultModify(param, flag);
        SimpleCsvTableUtils.easyCsvExport(response, FixAmountResultModifyConstants.WEB_PAGE_TITLE, res, FixAmountResultModifyPoVo.class);
    }

    @Override
    public Map<String, Integer> sum(FixAmountResultModifyBo param) {
        Map<String, Integer> map = new HashMap(2);
        queryParameter(param);
        int flag = getFlag(param);
        List<FixAmountResultModifyPoVo> afterList = fixAmountResultModifyMapper.selectFixAmountResultModify(param, flag);
        Integer afterSum = afterList.stream().reduce(0, (sum, bo) -> sum + Integer.parseInt(StringUtils.isBlank(bo.getQty()) ? NumberConstants.NUM_0 : bo.getQty()), Integer::sum);
        map.put("after", afterSum);
        List<FixAmountResultModifyPoVo> beaforeList = fixAmountResultModifyMapper.selectFixAmountResultModifyBefore(param, flag);
        Integer beforeSum = beaforeList.stream().reduce(0, (sum, bo) -> sum + Integer.parseInt(StringUtils.isBlank(bo.getQty()) ? NumberConstants.NUM_0 : bo.getQty()), Integer::sum);
        map.put("before", beforeSum);
        return map;
    }

    @Override
    public void add(FixAmountResultModifyUploadBo bo) {
        List<FixAmountResultModifyUploadBo> resList = new ArrayList<>();
        List<String> integerList = Arrays.stream(bo.getStoreIds().split(",")).collect(Collectors.toList());
        for (String storeId : integerList) {
            FixAmountResultModifyUploadBo boNew = BeanUtil.toBean(bo, FixAmountResultModifyUploadBo.class);
            boNew.setStoreId(storeId);
            resList.add(boNew);
        }
        List<FixAmountResultModifyVo> vos = checkAndResetFileData(Integer.parseInt(bo.getCenterId()), resList, false, new FileBackErrorVo());
        if (!vos.isEmpty()) {
            fixAmountResultModifyMapper.updateOrInsertFixAmountResultModifyPoBatch(Integer.parseInt(bo.getCenterId()), tokenTakeApart.takeDecryptedUserId(), vos, FunType.REGISTERED.getCode(), OpeType.CORRECTION_INDICATION.getCode());
        } else {
            for (FixAmountResultModifyUploadBo bo1 : resList) {
                if (!bo1.getErrorMsg().equals("")) {
                    throw new SysBusinessException(bo1.getErrorMsg());
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public FileBackErrorVo fmtImport(UploadBo bo, MultipartFile multipartFile, Boolean warningCheck, HttpServletResponse response) throws Exception {
        FileBackErrorVo fileBackErrorVo = new FileBackErrorVo();
        List<FixAmountResultModifyUploadBo> resList = new ArrayList<>();
        GetResultListReadListener<FixAmountResultModifyUploadBo> listener = new GetResultListReadListener<>(resList);
        EasyExcelFactory.read(multipartFile.getInputStream(), FixAmountResultModifyUploadBo.class, listener).sheet().headRowNumber(3).doRead();
        if (!resList.isEmpty()) {
            List<FixAmountResultModifyVo> vos = checkAndResetFileData(bo.getCenterId(), resList, warningCheck, fileBackErrorVo);
            if (!vos.isEmpty()) {
                fixAmountResultModifyMapper.updateOrInsertFixAmountResultModifyPoBatch(bo.getCenterId(), tokenTakeApart.takeDecryptedUserId(), vos, FunType.REGISTERED.getCode(), OpeType.CORRECTION_INDICATION.getCode());
            }
        } else {
            throw new SysBusinessException(SysConstantInfo.FILE_DATA_EMPTY_MSG);
        }
        return fileBackErrorVo;
    }

    private List<FixAmountResultModifyVo> checkAndResetFileData(Integer centerId, List<FixAmountResultModifyUploadBo> resList, Boolean warningCheck, FileBackErrorVo fileBackErrorVo) {
        // getMstList
        CommonMailNoBo bo = new CommonMailNoBo();
        bo.setCenterId(centerId);
        List<MailListVo> mailListVos = masterService.getMailList(bo);
        List<MtMailcontrol> mtMailcontrolList = fixAmountResultModifyMapper.selectMailNoWithCenterId(centerId);
        List<MtPlaceVo> mtPlaceVos = masterService.getPlaceList();
        List<BranchListVo> branchVos = masterService.queryMailBasicStoreList(centerId);
        UserInfoVo userInfoVo = authorityService.getUserInfoByTokenID();
        List<String> mailNos = resList.stream().map(FixAmountResultModifyUploadBo::getMailNo).filter(Objects::nonNull).collect(Collectors.toList());
        List<Short> mailNosNew = new ArrayList<>();
        try {
            mailNosNew = mailNos.stream().map(Short::parseShort).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("The file data type is incorrect");
        }
        List<MtMailcontrolVo> mtMailcontrolVoList = controlMapper.getActualProductionList(centerId, mailNosNew);
        // start check
        Set<Boolean> checkResult = new HashSet<>();
        Set<Boolean> priceFlag = new HashSet<>();
        Set<Boolean> otherFlag = new HashSet<>();
        for (FixAmountResultModifyUploadBo fixAmountResultModifyUploadBo : resList) {
            replaceComma(fixAmountResultModifyUploadBo);
            // required
            List<String> list = checkMustList(fixAmountResultModifyUploadBo);
            if (!list.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                fixAmountResultModifyUploadBo.setErrorMsg(SysConstantInfo.PARAM_NOT_NULL_MSG);
                continue;
            }
            // format
            HashSet<String> formatList = checkFormatList(fixAmountResultModifyUploadBo);
            if (!formatList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                fixAmountResultModifyUploadBo.setErrorMsg(StrFormatter.format(SysConstantInfo.INCORRECT, Joiner.on("、").join(formatList)));
                continue;
            }
            // 20
            if (fixAmountResultModifyUploadBo.getMailNo().equals("20")) {
                checkResult.add(true);
                otherFlag.add(true);
                fixAmountResultModifyUploadBo.setErrorMsg(SysConstantInfo.FMT_20_ERROR_MSG);
                continue;
            }
            // center as
//            if (!centerId.toString().equals(fixAmountResultModifyUploadBo.getCenterId())) {
//                checkResult.add(true);
//                otherFlag.add(true);
//                fixAmountResultModifyUploadBo.setErrorMsg(SysConstantInfo.CENTER_FRAME_ERROR);
//                continue;
//            }
            // authority
            List<String> authorityList = AuthorityUtils.checkAuthority(fixAmountResultModifyUploadBo.getCenterId(), fixAmountResultModifyUploadBo.getLineId(), userInfoVo);
            if (!authorityList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                fixAmountResultModifyUploadBo.setErrorMsg(StrFormatter.format(SysConstantInfo.AUTHORITY, Joiner.on("、").join(authorityList)));
                continue;
            }
            // mail confirmed
            List<MtMailcontrolVo> filterArray = mtMailcontrolVoList.stream().filter(s -> String.valueOf(s.getMailNo()).equals(fixAmountResultModifyUploadBo.getMailNo()) && String.valueOf(s.getCenterId()).equals(fixAmountResultModifyUploadBo.getCenterId()) && s.getThroughConfirmedDate() != null).collect(Collectors.toList());
            if (!filterArray.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                fixAmountResultModifyUploadBo.setErrorMsg(SysConstantInfo.FMT_MAIL_CONFIRMED_ERROR_MSG);
                continue;
            }
            //resetData
            if (StringUtils.isBlank(fixAmountResultModifyUploadBo.getQty())) {
                fixAmountResultModifyUploadBo.setQty(NumberConstants.NUM_0);
            }
            if (StringUtils.isBlank(fixAmountResultModifyUploadBo.getWeight())) {
                fixAmountResultModifyUploadBo.setWeight(NumberConstants.NUM_0);
            }

            // mst
            List<String> mstList = checkMstList(fixAmountResultModifyUploadBo, mailListVos, mtPlaceVos, branchVos, mtMailcontrolList);
            if (!mstList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                fixAmountResultModifyUploadBo.setErrorMsg(StrFormatter.format(SysConstantInfo.PARAM_NOT_EXISTENT_MSG, Joiner.on("、").join(mstList)));
                continue;
            }

            if (warningCheck) {
                // priceCheck
                if (StringUtils.isNotBlank(fixAmountResultModifyUploadBo.getPcPrice()) &&
                        StringUtils.isNotBlank(fixAmountResultModifyUploadBo.getItemPrice()) &&
                        new BigDecimal(fixAmountResultModifyUploadBo.getPcPrice()).compareTo(new BigDecimal(fixAmountResultModifyUploadBo.getItemPrice())) > 0) {
                    priceFlag.add(true);
                    checkResult.add(true);
                    fixAmountResultModifyUploadBo.setErrorMsg((StringUtil.isNotBlank(fixAmountResultModifyUploadBo.getErrorMsg()) ? fixAmountResultModifyUploadBo.getErrorMsg() : "") + SysConstantInfo.PRICE_CHECK_1);
                }

                if (StringUtils.isNotBlank(fixAmountResultModifyUploadBo.getItemPrice()) &&
                        StringUtils.isNotBlank(fixAmountResultModifyUploadBo.getPrice()) &&
                        new BigDecimal(fixAmountResultModifyUploadBo.getItemPrice()).compareTo(new BigDecimal(fixAmountResultModifyUploadBo.getPrice())) > 0) {
                    priceFlag.add(true);
                    checkResult.add(true);
                    fixAmountResultModifyUploadBo.setErrorMsg((StringUtil.isNotBlank(fixAmountResultModifyUploadBo.getErrorMsg()) ? fixAmountResultModifyUploadBo.getErrorMsg() : "") + SysConstantInfo.PRICE_CHECK_2);
                }
            }

            TmShohinVo tmShohinVo = tmShoinService.findByKey(Integer.parseInt(fixAmountResultModifyUploadBo.getStoreId()), fixAmountResultModifyUploadBo.getItemId(), fixAmountResultModifyUploadBo.getAreaId(), false);
            if (Objects.isNull(tmShohinVo)) {
                tmShohinVo = tmShoinService.findByKey(0, fixAmountResultModifyUploadBo.getItemId(), fixAmountResultModifyUploadBo.getAreaId(), false);
            }

            if (StringUtils.isBlank(fixAmountResultModifyUploadBo.getPlaceId())) {
                fixAmountResultModifyUploadBo.setPlaceId(tmShohinVo.getPlaceId() == null ? "0" : tmShohinVo.getPlaceId().toString());
            }

            if (StringUtils.isBlank(fixAmountResultModifyUploadBo.getPcPrice())) {
                fixAmountResultModifyUploadBo.setPcPrice(tmShohinVo.getCostPc() == null ? "0.00" : tmShohinVo.getCostPc().toString());
            }
            if (StringUtils.isBlank(fixAmountResultModifyUploadBo.getItemPrice())) {
                fixAmountResultModifyUploadBo.setItemPrice(tmShohinVo.getCost() == null ? "0.00" : tmShohinVo.getCost().toString());
            }
            if (StringUtils.isBlank(fixAmountResultModifyUploadBo.getPrice())) {
                fixAmountResultModifyUploadBo.setPrice(tmShohinVo.getPrice() == null ? "0" : String.valueOf(Double.valueOf(tmShohinVo.getPrice().toString()).intValue()));
            } else {
                fixAmountResultModifyUploadBo.setPrice(String.valueOf(Double.valueOf(fixAmountResultModifyUploadBo.getPrice()).intValue()));
            }
        }

        List<FixAmountResultModifyVo> fixAmountResultModifyVos = new ArrayList<>();
        if (checkResult.contains(true)) {
            fileBackErrorVo.setFileError(downloadErrorFile(resList));
        } else {
            for (FixAmountResultModifyUploadBo a : resList) {
                FixAmountResultModifyVo fixAmountResultModifyVo = BeanUtil.toBean(a, FixAmountResultModifyVo.class);
                if (StringUtils.isNotBlank(a.getQty())) {
                    fixAmountResultModifyVo.setQty(Integer.valueOf(a.getQty()));
                }
                if (StringUtils.isNotBlank(a.getWeight())) {
                    fixAmountResultModifyVo.setWeight(new BigDecimal(a.getWeight()));
                }
                if (StringUtils.isNotBlank(a.getPcPrice())) {
                    fixAmountResultModifyVo.setPcPrice(new BigDecimal(a.getPcPrice()));
                }
                if (StringUtils.isNotBlank(a.getItemPrice())) {
                    fixAmountResultModifyVo.setItemPrice(new BigDecimal(a.getItemPrice()));
                }
                if (StringUtils.isNotBlank(a.getPrice())) {
                    fixAmountResultModifyVo.setPrice(Integer.valueOf(a.getPrice()));
                }
                fixAmountResultModifyVos.add(fixAmountResultModifyVo);
            }
        }

        fileBackErrorVo.setOtherFlag(otherFlag.contains(true));
        fileBackErrorVo.setPriceFlag(priceFlag.contains(true));
        fileBackErrorVo.setDateFlag(false);
        return fixAmountResultModifyVos;
    }

    private List<String> checkMstList(FixAmountResultModifyUploadBo uploadBo, List<MailListVo> mailListVos, List<MtPlaceVo> mtPlaceVos, List<BranchListVo> branchVos, List<MtMailcontrol> mtMailcontrolList) {
        List<String> list = new ArrayList<>();
        if (mailListVos.stream().noneMatch(s -> s.getMailNo().toString().equals(uploadBo.getMailNo())) || mtMailcontrolList.stream().noneMatch(s -> s.getMailNo().toString().equals(uploadBo.getMailNo()))) {
            list.add("[便]");
        }
        if (branchVos.stream().noneMatch(s -> s.getBranchcd().toString().equals(uploadBo.getStoreId()))) {
            list.add("[店舗コード]");
        }
        if (StringUtils.isNotBlank(uploadBo.getPlaceId()) && mtPlaceVos.stream().noneMatch(s -> s.getId().toString().equals(uploadBo.getPlaceId()))) {
            list.add("[産地コード]");
        }
        return list;
    }

    private HashSet<String> checkFormatList(FixAmountResultModifyUploadBo a) {
        HashSet<String> list = new HashSet<>();
        if (!a.getCenterId().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("センターコード");
        }
        if (StringUtils.isNotBlank(a.getCallCode()) && (a.getCallCode().length() > 6 || !a.getCallCode().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("品番");
        }
        if (StringUtils.isNotBlank(a.getItemId()) && (a.getItemId().length() > 13 || !a.getItemId().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("JAN");
        }
        if (!a.getMailNo().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("便");
        }
        if (!a.getStoreId().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("店舗コード");
        }
        if (StringUtils.isNotBlank(a.getQty()) && (String.valueOf(a.getQty()).length() > 5 || !a.getQty().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("数量");
        }
        if (StringUtils.isNotBlank(a.getPlaceId()) && (String.valueOf(a.getPlaceId()).length() > 4 || !a.getPlaceId().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("産地コード");
        }
        if (StringUtils.isNotBlank(a.getLotNo()) && (String.valueOf(a.getLotNo()).length() > 12 || !a.getLotNo().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("ロットNo");
        }
        if (StringUtils.isNotBlank(a.getPcPrice()) && (!a.getPcPrice().matches(FormatConstants.REGEX_PATTERN_MONEY) || a.getPcPrice().length() > 9)) {
            list.add("PC原価");
        }
        if (StringUtils.isNotBlank(a.getItemPrice()) && (!a.getItemPrice().matches(FormatConstants.REGEX_PATTERN_MONEY) || a.getItemPrice().length() > 9)) {
            list.add("店原価");
        }
        if (StringUtils.isNotBlank(a.getPrice()) && (!a.getPrice().matches(FormatConstants.REGEX_PATTERN_MONEY) | a.getPrice().length() > 6)) {
            list.add("店売価");
        }
        // JAN、店舗/地域
        if (!list.contains("店舗コード") && !list.contains("JAN")) {
            boolean flag = true;
            if (StringUtils.isNotBlank(a.getCallCode()) && !list.contains("品番")) {
                List<TmShohinVo> tmShohinVos = tmShoinService.findJanByCallCode(Integer.parseInt(a.getStoreId()), Integer.parseInt(a.getCallCode()));
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
                            extracted(a, list, tmShohinVoList);
                        }
                    } else {
                        List<TmShohinVo> tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId() == Integer.parseInt(a.getStoreId())).collect(Collectors.toList());
                        if (tmShohinVoList.isEmpty()) {
                            tmShohinVoList = tmShohinVos.stream().filter(s -> s.getStoreId().equals(0)).collect(Collectors.toList());
                        }
                        a.setItemId(tmShohinVoList.get(0).getItemId());
                        extracted(a, list, tmShohinVoList);
                    }
                }
            }
            if (flag && StringUtils.isNotBlank(a.getItemId()) && !list.contains("JAN")) {
                List<TmShohinVo> tmShohinVoList = tmShoinService.findJanStore(Integer.parseInt(a.getStoreId()), a.getItemId());
                if (tmShohinVoList.isEmpty()) {
                    list.add("JANと店舗");
                } else {
                    extracted(a, list, tmShohinVoList);
                }
            }
        }
        return list;
    }

    private static void extracted(FixAmountResultModifyUploadBo modifyUploadBo, HashSet<String> list, List<TmShohinVo> tmShohinVoList) {
        modifyUploadBo.setAreaId(tmShohinVoList.get(0).getAreaId());
        modifyUploadBo.setLineId(tmShohinVoList.get(0).getLineId());
        if (!String.valueOf(tmShohinVoList.get(0).getTeikanTypeid()).equals("0") && StringUtil.isBlank(modifyUploadBo.getWeight())) {
            modifyUploadBo.setWeight("0");
        }
        if ((String.valueOf(tmShohinVoList.get(0).getTeikanTypeid()).equals("0") && StringUtil.isBlank(modifyUploadBo.getWeight())) || !modifyUploadBo.getWeight().matches(FormatConstants.REGEX_PATTERN_WEIGHT) || modifyUploadBo.getWeight().length() > 9) {
            list.add("重量");
        }
        if (String.valueOf(tmShohinVoList.get(0).getTeikanTypeid()).equals("0") && !list.contains("重量")) {
            if ((Double.parseDouble(modifyUploadBo.getWeight()) > NumberConstants.NUM_INT_0 && Integer.parseInt(modifyUploadBo.getQty()) <= NumberConstants.NUM_INT_0)
                    || (Double.parseDouble(modifyUploadBo.getWeight()) == NumberConstants.NUM_INT_0 && Integer.parseInt(modifyUploadBo.getQty()) != NumberConstants.NUM_INT_0)) {
                list.add("数量と重量");
            }
        }
    }

    private List<String> checkMustList(FixAmountResultModifyUploadBo a) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isBlank(a.getCenterId())) {
            list.add("[センターコード]");
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
        if (StringUtils.isBlank(a.getQty())) {
            list.add("[数量]");
        }
        return list;
    }

    private String downloadErrorFile(List<FixAmountResultModifyUploadBo> resList) {
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = StrFormatter.format(SysConstantInfo.FMT_FILE_NAME_WITH_TIME_AND_ERROR_MSG, FixAmountResultModifyConstants.FMT_ERROR_FILE_NAME, curTime, FileTypeConstants.XLSX);
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), FixAmountResultModifyUploadBo.class)
                .withTemplate(simpleFmtTableUtils.getFmtFileInputStream(FixAmountResultModifyConstants.BUSINESS_NAME))
                .registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                ).needHead(false).sheet().doWrite(resList);
        return fileName;
    }
}
