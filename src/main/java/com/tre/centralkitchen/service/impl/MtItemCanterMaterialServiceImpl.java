package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import com.tre.centralkitchen.common.constant.FileTypeConstants;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.excel.GetResultListReadListener;
import com.tre.centralkitchen.common.utils.AuthorityUtils;
import com.tre.centralkitchen.common.utils.HeadContentCellStyle;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.domain.bo.system.AppendedBo;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialBo;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialSearchBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.po.MtItemCenterMaterial;
import com.tre.centralkitchen.domain.vo.common.CenterListVo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.common.TmShohinVo;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterSearchVo;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterialVo;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.mapper.MtItemCenterMaterialMapper;
import com.tre.centralkitchen.service.AuthorityService;
import com.tre.centralkitchen.service.MtItemCanterMaterialService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MtItemCanterMaterialServiceImpl implements MtItemCanterMaterialService {

    private final MtItemCenterMaterialMapper mapper;
    private final MasterService masterService;
    private final AuthorityService authorityService;
    private final SimpleFmtTableUtils simpleFmtTableUtils;
    private final TmShoinServiceImpl tmShoinService;

    @Override
    public TableDataInfo<ItemCenterMaterialVo> search(MtItemCenterMaterialSearchBo bo, PageQuery pageQuery) {
        bo.build();
        Page<ItemCenterMaterialVo> page = mapper.search(pageQuery.build(), bo);

        return TableDataInfo.build(page);
    }

    @Override
    public void downloadCSV(MtItemCenterMaterialSearchBo bo, HttpServletResponse response) {
        List<ItemCenterMaterialVo> vo = mapper.search(bo);
        SimpleCsvTableUtils.easyCsvExport(response, MailConstants.MT_ITEM_CENTER_MATERIAL_CSV_NAME, vo, ItemCenterMaterialVo.class);

    }

    @Override
    public ItemCenterMaterSearchVo info(MtItemCenterMaterialBo bo) {
        return mapper.queryItemCanterMaterialOne(bo);
    }

    @Override
    public String selectItemName(Integer centerId, String itemId) {
        List<TmShohinVo> tmShoHinVos = tmShoinService.findCenterJanBy(centerId, itemId);
        for (TmShohinVo tmShoHinVo : tmShoHinVos) {
            String sItemId = tmShoHinVo.getItemId();
            if (sItemId.equals(itemId)) {
                return tmShoHinVo.getItemName();
            }
        }
        return null;
    }


    @Override
    public void save(MtItemCenterMaterialBo bo) {
        MtItemCenterMaterial mtItemCenterMaterial = BeanUtil.toBean(bo, MtItemCenterMaterial.class);
        mtItemCenterMaterial.setExptimeAlarm(0);
        mapper.insert(mtItemCenterMaterial);
    }

    @Override
    public void update(MtItemCenterMaterialBo bo) {
        MtItemCenterMaterial mtItemCenterMaterial = BeanUtil.toBean(bo, MtItemCenterMaterial.class);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("center_id", bo.getCenterId());
        wrapper.eq("item_id", bo.getItemId());
        mapper.update(mtItemCenterMaterial, wrapper);
    }

    @Override
    public void delete(MtItemCenterMaterialBo bo) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("center_id", bo.getCenterId());
        wrapper.eq("item_id", bo.getItemId());
        wrapper.set("f_del", 1);
        mapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileBackErrorVo fmtImport(MultipartFile file, HttpServletResponse response) throws IOException {
        FileBackErrorVo fileBackErrorVo = new FileBackErrorVo();
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.contains(SysConstants.ITEM_CENTER_MATERIAL_FILE_NAME)) {
            List<MtItemCenterMaterialBo> boList = new ArrayList<>();
            GetResultListReadListener<MtItemCenterMaterialBo> listener = new GetResultListReadListener<>(boList);
            EasyExcelFactory.read(file.getInputStream(), MtItemCenterMaterialBo.class, listener).sheet().headRowNumber(2).doRead();
            if (!boList.isEmpty()) {
                List<MtItemCenterMaterialBo> boListNew = checkAddFileData(boList, fileBackErrorVo);
                if (!boListNew.isEmpty()) {
                    for (MtItemCenterMaterialBo centerMaterialBo : boListNew) {
                        QueryWrapper<MtItemCenterMaterial> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("center_id", centerMaterialBo.getCenterId());
                        queryWrapper.eq("item_id", centerMaterialBo.getItemId());
                        Long num = mapper.selectCount(queryWrapper);
                        if (num == 0) {
                            MtItemCenterMaterial mtItemCenterMaterial = BeanUtil.toBean(centerMaterialBo, MtItemCenterMaterial.class);
                            mtItemCenterMaterial.setExptimeAlarm(0);
                            mapper.insert(mtItemCenterMaterial);
                        } else {
                            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.APPENDED_NOT_EXISTENT_MSG, centerMaterialBo.getCenterId(), centerMaterialBo.getItemId()));
                        }
                    }
                }
            }

        }
        return fileBackErrorVo;
    }


    private List<MtItemCenterMaterialBo> checkAddFileData(List<MtItemCenterMaterialBo> boList, FileBackErrorVo fileBackErrorVo) {

        // getMstList
        List<CenterListVo> centerList = masterService.getCenterList();

        UserInfoVo userInfoVo = authorityService.getUserInfoByTokenID();

        // start check
        Set<Boolean> checkResult = new HashSet<>();
        Set<Boolean> otherFlag = new HashSet<>();
        for (MtItemCenterMaterialBo centerMaterialBo : boList) {

            // required
            List<String> list = checkMustList(centerMaterialBo);
            if (!list.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                centerMaterialBo.setErrorMsg(SysConstantInfo.PARAM_NOT_NULL_MSG);
                continue;
            }

            // format
            HashSet<String> formatList = checkFormatList(centerMaterialBo);
            if (!formatList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                centerMaterialBo.setErrorMsg(StrFormatter.format(SysConstantInfo.INCORRECT, Joiner.on("、").join(formatList)));
                continue;
            }
            //authority
            List<String> authorityList = AuthorityUtils.checkAuthority(centerMaterialBo.getCenterId().toString(), null, userInfoVo);
            if (!authorityList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                centerMaterialBo.setErrorMsg(StrFormatter.format(SysConstantInfo.AUTHORITY, Joiner.on("、").join(authorityList)));
                continue;
            }

            // mst
            List<String> mstList = checkMstList(centerMaterialBo, centerList);
            if (!mstList.isEmpty()) {
                checkResult.add(true);
                otherFlag.add(true);
                centerMaterialBo.setErrorMsg(StrFormatter.format(SysConstantInfo.PARAM_NOT_EXISTENT_MSG, Joiner.on("、").join(mstList)));
                continue;
            }


        }
        if (checkResult.contains(true)) {
            fileBackErrorVo.setFileError(downloadErrorFile(boList));
            boList = new ArrayList<>();
        }
        fileBackErrorVo.setOtherFlag(otherFlag.contains(true));

        return boList;
    }

    private HashSet<String> checkFormatList(MtItemCenterMaterialBo centerMaterialBo) {

        HashSet<String> list = new HashSet<>();

        if (!centerMaterialBo.getCenterId().toString().matches(FormatConstants.REGEX_PATTERN_NUMBER)) {
            list.add("センターコード");
        }
        if (StringUtils.isNotBlank(centerMaterialBo.getItemId()) && (centerMaterialBo.getItemId().length() > 13 || !centerMaterialBo.getItemId().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("JAN");
        }
        if (ObjectUtil.isEmpty(centerMaterialBo.getSafetyStockQy()) && (centerMaterialBo.getSafetyStockQy().toString().length() > 5 || !centerMaterialBo.getSafetyStockQy().toString().matches(FormatConstants.REGEX_PATTERN_NUMBER))) {
            list.add("安全在庫");
        }

        if (!list.contains("センターコード") && !list.contains("JAN")) {
            boolean flag = true;

            if (flag && StringUtils.isNotBlank(centerMaterialBo.getItemId()) && !list.contains("JAN")) {
                List<TmShohinVo> tmShohinVoList = tmShoinService.findCenterJanBy((centerMaterialBo.getCenterId()), centerMaterialBo.getItemId());
                if (!tmShohinVoList.isEmpty()) {
                    list.add("JANとセンターコード");
                }
            }
        }
        return list;

    }

    private String downloadErrorFile(List<MtItemCenterMaterialBo> boList) {
        String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR));
        String fileName = StrFormatter.format(SysConstantInfo.FMT_FILE_NAME_WITH_TIME_AND_ERROR_MSG, SysConstants.ITEM_CENTER_MATERIAL_FILE_NAME, curTime, FileTypeConstants.XLSX);
        EasyExcelFactory.write(simpleFmtTableUtils.getTempFilePath(fileName).toFile(), MtItemCenterMaterialBo.class)
                .withTemplate(simpleFmtTableUtils.getFmtFileInputStream("itemCenterMaterial"))
                .registerWriteHandler(
                        HeadContentCellStyle.myHorizontalCellStyleStrategy()
                ).needHead(false).sheet().doWrite(boList);
        return fileName;

    }

    private List<String> checkMstList(MtItemCenterMaterialBo centerMaterialBo, List<CenterListVo> centerList) {
        List<String> list = new ArrayList<>();
        if (centerList.stream().noneMatch(s -> s.getCenterId().equals(centerMaterialBo.getCenterId()))) {
            list.add("[センターコード]");
        }
        return list;
    }


    private List<String> checkMustList(MtItemCenterMaterialBo centerMaterialBo) {
        List<String> list = new ArrayList<>();
        if (ObjectUtil.isEmpty(centerMaterialBo.getCenterId())) {
            list.add("[センターコード]");
        }

        return list;
    }
}

