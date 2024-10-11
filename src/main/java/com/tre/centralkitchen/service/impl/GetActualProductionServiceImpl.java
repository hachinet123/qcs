package com.tre.centralkitchen.service.impl;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionBo;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionSearchBo;
import com.tre.centralkitchen.domain.dto.MtLcu7controlDto;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.system.GetActualProductionVo;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.GetActualProductionService;
import com.tre.centralkitchen.service.IFTPService;
import com.tre.jdevtemplateboot.common.util.JasyptUtils;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GetActualProductionServiceImpl implements GetActualProductionService {
    private final IFTPService iftpservice;
    private final WkRecievelcuProducedMapper mapper;

    private final TrProducedMapper producedMapper;

    private final TrProduceplanMapper trProduceplanMapper;

    private final MtMailcontrolMapper controlMapper;

    private final MtLcu7controlMapper lcu7controlMapper;

    private final MtCenterstatusMapper centerstatusMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getActualProduction(List<GetActualProductionBo> bos, String userId) throws IOException, InterruptedException {
        MtLcu7controlDto dto = getMtLcu7controlDto(bos.get(0));
        if (!iftpservice.lcuFileCheck(dto)) {
            throw new SysBusinessException(SysConstantInfo.LCU_FILE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.LCU_FILE_ERROR_CODE);
        }
        MtMailcontrol mc = controlMapper.selectOne(new QueryWrapper<MtMailcontrol>()
                .eq("center_id", bos.get(0).getCenterId())
                .eq("mail_no", bos.get(0).getMailNo()));
        List<TrProducePlan> trProducePlans = trProduceplanMapper.selectList(new QueryWrapper<TrProducePlan>()
                .eq("center_id", mc.getCenterId())
                .eq("mail_no", mc.getMailNo())
                .eq("dlvsched_date", mc.getDlvschedDate()));
        mapper.deleteByCenterId(bos.get(0).getCenterId());
        List<WkRecievelcuProduced> ts = iftpservice.receiveData(dto, bos,trProducePlans);
        List<WkRecievelcuProduced> arr = new ArrayList<>();

        ts.forEach(t -> {
            WkRecievelcuProduced produced = new WkRecievelcuProduced();
            BeanUtils.copyProperties(t, produced);

            produced.setInsFuncId(bos.get(0).getInsFuncId());
            produced.setInsOpeId(bos.get(0).getInsOpeId());
            produced.setUpdFuncId(bos.get(0).getUpdFuncId());
            produced.setUpdOpeId(bos.get(0).getUpdOpeId());
            arr.add(produced);
        });
        mapper.insertBatch(arr);

        mapper.backUpData(bos.get(0).getCenterId());
        bos.forEach(bo -> acquireMeterActualResults(bo, userId));
    }

    @Override
    public TableDataInfo search(GetActualProductionSearchBo bo, PageQuery pageQuery) {
        List<Short> mailNos = new ArrayList<>();
        if (!bo.getMailNo().isBlank()) {
            mailNos = Arrays.stream(bo.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList());
        }
        return TableDataInfo.build(controlMapper.getActualProductionVoPage(pageQuery.build(), bo.getCenterId(), mailNos));
    }

    @Override
    public void downloadCSV(GetActualProductionSearchBo bo, HttpServletResponse response) {
        List<Short> mailNos = new ArrayList<>();
        if (!bo.getMailNo().isBlank()) {
            mailNos = Arrays.stream(bo.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList());
        }
        List<GetActualProductionVo> records = controlMapper.getActualProductionVoPage(bo.getCenterId(), mailNos);
        SimpleCsvTableUtils.easyCsvExport(response, MailConstants.GET_ACTUAL_PRODUCTION_CSV_NAME, records, GetActualProductionVo.class);
    }

    @Override
    public void fileSend(GetActualProductionBo bo) throws IOException {
        MtLcu7controlDto dto = getMtLcu7controlDto(bo);
        List<GetActualProductionBo> bos = new ArrayList<>();
        bos.add(bo);
        iftpservice.sendLCUFile(dto, bos);
    }

    @Override
    public void fileRecv(GetActualProductionBo bo) throws IOException, InterruptedException {
        MtLcu7controlDto dto = getMtLcu7controlDto(bo);
        List<GetActualProductionBo> bos = new ArrayList<>();
        bos.add(bo);
        MtMailcontrol mc = controlMapper.selectOne(new QueryWrapper<MtMailcontrol>()
                .eq("center_id", bo.getCenterId())
                .eq("mail_no", bo.getMailNo()));
        List<TrProducePlan> trProducePlans = trProduceplanMapper.selectList(new QueryWrapper<TrProducePlan>()
                .eq("center_id", mc.getCenterId())
                .eq("mail_no", mc.getMailNo())
                .eq("dlvsched_date", mc.getDlvschedDate()));
        iftpservice.getLCUFile(dto, bos, trProducePlans);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fileBackup(GetActualProductionBo bo) throws IOException {
        mapper.deleteByCenterId(bo.getCenterId());
        MtLcu7controlDto dto = getMtLcu7controlDto(bo);
        iftpservice.backUpLCUFile(dto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void fileRead(GetActualProductionBo bo, String userId) throws IOException {
        MtLcu7controlDto dto = getMtLcu7controlDto(bo);
        List<GetActualProductionBo> bos = new ArrayList<>();
        bos.add(bo);
        List<WkRecievelcuProduced> ts = iftpservice.getWkRecieveLCUProduceds(dto, bos);
        List<WkRecievelcuProduced> arr = new ArrayList<>();

        ts.forEach(t -> {
            WkRecievelcuProduced produced = new WkRecievelcuProduced();
            BeanUtils.copyProperties(t, produced);

            produced.setInsFuncId(bo.getInsFuncId());
            produced.setInsOpeId(bo.getInsOpeId());
            produced.setUpdFuncId(bo.getUpdFuncId());
            produced.setUpdOpeId(bo.getUpdOpeId());
            arr.add(produced);
        });
        mapper.insertBatch(arr);

        mapper.backUpData(bo.getCenterId());
        acquireMeterActualResultsForHinemos(bo, userId);
    }

    @NotNull
    private MtLcu7controlDto getMtLcu7controlDto(GetActualProductionBo bo) {
        QueryWrapper<MtCenterstatus> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("center_id", bo.getCenterId());
        MtCenterstatus mtCenterstatusPo = centerstatusMapper.selectOne(queryWrapper1);

        QueryWrapper<MtLcu7control> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("op_type", 4);
        queryWrapper2.eq("seq", 1);
        MtLcu7control mtLcu7ControlPo = lcu7controlMapper.selectOne(queryWrapper2);
        MtLcu7controlDto dto = new MtLcu7controlDto();

        BeanUtils.copyProperties(mtLcu7ControlPo, dto);
        dto.setCenterCd(mtCenterstatusPo.getCenterId());
        dto.setIp(mtCenterstatusPo.getLcuIp());
        dto.setUser(mtCenterstatusPo.getLcuId());
        dto.setPwd("".equals(mtCenterstatusPo.getLcuPass()) ? "" : JasyptUtils.decyptPwd(mtCenterstatusPo.getLcuPass()));
        dto.setPath("");
        return dto;
    }

    private void acquireMeterActualResults(GetActualProductionBo bo, String userId) {

        producedMapper.acquireMeterActualResults(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());

        producedMapper.acquireMeterActualResultsNoDirectiOnData(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());

        producedMapper.acquireMeterActualResultsLot(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());

        QueryWrapper<MtMailcontrol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("center_Id", bo.getCenterId());
        queryWrapper.eq("mail_no", bo.getMailNo());
        MtMailcontrol mtMailcontrolPo = controlMapper.selectOne(queryWrapper);
        mtMailcontrolPo.setProducedImportDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        controlMapper.update(mtMailcontrolPo, queryWrapper);
    }

    private void acquireMeterActualResultsForHinemos(GetActualProductionBo bo, String userId) {

        producedMapper.acquireMeterActualResultsForHinemos(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());

        producedMapper.acquireMeterActualResultsNoDirectiOnDataForHinemos(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());

        producedMapper.acquireMeterActualResultsLotForHinemos(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());

        QueryWrapper<MtMailcontrol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("center_Id", bo.getCenterId());
        queryWrapper.eq("mail_no", bo.getMailNo());
        MtMailcontrol mtMailcontrolPo = controlMapper.selectOne(queryWrapper);
        mtMailcontrolPo.setProducedImportDate(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        controlMapper.update(mtMailcontrolPo, queryWrapper);
    }
}
