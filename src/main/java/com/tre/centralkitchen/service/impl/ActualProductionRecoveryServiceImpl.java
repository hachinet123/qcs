package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmBo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionRecoveryBo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionRecoverySearchBo;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionBo;
import com.tre.centralkitchen.domain.vo.system.ActualProductionRecoveryVo;
import com.tre.centralkitchen.domain.vo.system.MtMailcontrolVo;
import com.tre.centralkitchen.mapper.MtMailcontrolMapper;
import com.tre.centralkitchen.mapper.TrOutTransbillMapper;
import com.tre.centralkitchen.service.ActualProductionConfirmService;
import com.tre.centralkitchen.service.ActualProductionRecoveryService;
import com.tre.centralkitchen.service.GetActualProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActualProductionRecoveryServiceImpl implements ActualProductionRecoveryService {

    private final GetActualProductionService getActualProductionService;
    private final ActualProductionConfirmService actualProductionConfirmService;
    private final MtMailcontrolMapper controlMapper;
    private final TrOutTransbillMapper trOutTransbillMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void actualProductionRecovery(List<ActualProductionRecoveryBo> bos, String userId) throws IOException, InterruptedException {

        List<GetActualProductionBo> getActualProductionBos = new ArrayList<>();
        List<ActualProductionConfirmBo> actualProductionConfirmBos = new ArrayList<>();
        bos.forEach(bo -> {
            controlMapper.pcUpBin(bo.getCenterId(), bo.getMailNo());
            if (trOutTransbillMapper.getReturnNO(bo.getCenterId(), bo.getMailNo()) > 0) {
                trOutTransbillMapper.productionResultsReturnBack(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());
            } else {
                trOutTransbillMapper.productionResultsReturn(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId());
            }

            GetActualProductionBo getActualProductionBo = new GetActualProductionBo();
            ActualProductionConfirmBo actualProductionConfirmBo = new ActualProductionConfirmBo();

            BeanUtils.copyProperties(bo, getActualProductionBo);
            BeanUtils.copyProperties(bo, actualProductionConfirmBo);
            getActualProductionBos.add(getActualProductionBo);
            actualProductionConfirmBos.add(actualProductionConfirmBo);
        });
        getActualProductionService.getActualProduction(getActualProductionBos, userId);
        actualProductionConfirmService.actualProductionConfirm(actualProductionConfirmBos, userId);
    }

    @Override
    public TableDataInfo search(ActualProductionRecoverySearchBo bo, PageQuery pageQuery) {
        List<Short> mailNos = new ArrayList<>();
        if (!bo.getMailNo().isBlank()) {
            mailNos = Arrays.stream(bo.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList());
        }
        return TableDataInfo.build(controlMapper.actualProductionRecoveryVoPage(pageQuery.build(), bo.getCenterId(), mailNos));
    }

    @Override
    public void downloadCSV(ActualProductionRecoverySearchBo bo, HttpServletResponse response) {
        List<Short> mailNos = new ArrayList<>();
        if (!bo.getMailNo().isBlank()) {
            mailNos = Arrays.stream(bo.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList());
        }
        List<ActualProductionRecoveryVo> records = controlMapper.actualProductionRecoveryVoPage(bo.getCenterId(), mailNos);
        SimpleCsvTableUtils.easyCsvExport(response, MailConstants.ACTUAL_PRODUCTION_RECOVERY_CSV_NAME, records, ActualProductionRecoveryVo.class);
    }

}
