package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmBo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmSearchBo;
import com.tre.centralkitchen.domain.po.WkDecideProductionResults;
import com.tre.centralkitchen.domain.vo.system.ActualProductionConfirmVo;
import com.tre.centralkitchen.domain.vo.system.DecideProductionResultsVo;
import com.tre.centralkitchen.mapper.MtMailcontrolMapper;
import com.tre.centralkitchen.mapper.TrOutTransbillMapper;
import com.tre.centralkitchen.mapper.WkDecideProductionResultsMapper;
import com.tre.centralkitchen.service.ActualProductionConfirmService;
import com.tre.centralkitchen.service.IMtCenterstatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActualProductionConfirmServiceImpl implements ActualProductionConfirmService {
    private final TrOutTransbillMapper trOutTransbillMapper;
    private final IMtCenterstatusService iMtCenterstatusService;
    private final MtMailcontrolMapper controlMapper;
    private final WkDecideProductionResultsMapper wkDecideProductionResultsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void actualProductionConfirm ( List<ActualProductionConfirmBo> bos, String userId ) {
        for (ActualProductionConfirmBo bo : bos) {
            decideProductionResults(bo, userId);
        }
    }

    @Override
    public TableDataInfo search(ActualProductionConfirmSearchBo bo, PageQuery pageQuery) {
        List<Short> mailNos = new ArrayList<>();
        if (!bo.getMailNo().isBlank()) {
            mailNos = Arrays.stream(bo.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList());
        }
        return TableDataInfo.build(controlMapper.actualProductionConfirmVoPage(pageQuery.build(), bo.getCenterId(), mailNos));
    }

    @Override
    public void downloadCSV(ActualProductionConfirmSearchBo bo, HttpServletResponse response) {
        List<Short> mailNos = new ArrayList<>();
        if (!bo.getMailNo().isBlank()) {
            mailNos = Arrays.stream(bo.getMailNo().split(StringConstants.COMMA)).distinct().map(Short::parseShort).collect(Collectors.toList());
        }
        List<ActualProductionConfirmVo> records = controlMapper.actualProductionConfirmVoPage(bo.getCenterId(), mailNos);
        SimpleCsvTableUtils.easyCsvExport(response, MailConstants.ACTUAL_PRODUCTION_CONFIRM_CSV_NAME, records, ActualProductionConfirmVo.class);
    }

    private void decideProductionResults(ActualProductionConfirmBo bo, String userId) {

        List<DecideProductionResultsVo> decideProductionResultsVos = trOutTransbillMapper.decideProductionResultsTemp(bo.getCenterId(), bo.getMailNo(), 1);

        Collections.sort(decideProductionResultsVos, Comparator.comparingInt(DecideProductionResultsVo::getMailNo).thenComparingInt(DecideProductionResultsVo::getStoreId).thenComparing(DecideProductionResultsVo::getDlvschedDate));
        List<WkDecideProductionResults> pos = new ArrayList<>();

        UUID uuid = UUID.randomUUID();
        /*-------------------------------------------------------------------
        発注が発注区分違うことより複数件以上がある場合（生産実績は１件しかない）、
        発注に相応する発注振替伝票作成を対応するためend
        ・1件だけある場合そのまま継承
        ・2件以上ある場合生産数は本社発注を優先に分配する
        -------------------------------------------------------------------*/
        // Step2_生産実績取得済みファイルカーソル準備(生産実績数<0)
        AtomicInteger dLineNo = new AtomicInteger(1);
        AtomicReference<Date> dDlvschedDate = new AtomicReference<>(new Date());
        AtomicInteger dMailNo = new AtomicInteger();
        AtomicInteger dStoreId = new AtomicInteger();
        AtomicInteger dDepartmentId = new AtomicInteger();
        AtomicInteger dFirst = new AtomicInteger(1);
        AtomicInteger dSlipNo = new AtomicInteger();
        decideProductionResultsVos.stream().filter(a -> a.getActQy() < 0 && a.getSlipNo() == 0).forEach(a -> {
            if (dLineNo.get() > 9 || !dDlvschedDate.get().equals(a.getDlvschedDate()) || dMailNo.get() != a.getMailNo() || dStoreId.get() != a.getStoreId() || dDepartmentId.get() != a.getDepartmentId() || dFirst.get() == 1) {
                dLineNo.set(1);
                dSlipNo.set(iMtCenterstatusService.getSlip(bo.getCenterId()));
                dFirst.set(0);
            }
            a.setSlipNo(dSlipNo.get());
            a.setLineNo(dLineNo.get());

            dLineNo.getAndIncrement();
            dDlvschedDate.set(a.getDlvschedDate());
            dMailNo.set(a.getMailNo());
            dStoreId.set(a.getStoreId());
            dDepartmentId.set(a.getDepartmentId());

            WkDecideProductionResults po = new WkDecideProductionResults();
            BeanUtil.copyProperties(a, po);
            po.setSlipCode(a.getSlipNo());
            po.setId(uuid.toString());
            po.setUserId(Convert.toInt(userId));
            pos.add(po);
        });

        // Step5_生産実績取得済みファイルカーソル準備(生産実績数 >= 0)
        AtomicInteger lLineNo = new AtomicInteger(1);
        AtomicReference<Date> lDlvschedDate = new AtomicReference<>(new Date());
        AtomicInteger lMailNo = new AtomicInteger();
        AtomicInteger lStoreId = new AtomicInteger();
        AtomicInteger lDepartmentId = new AtomicInteger();
        AtomicInteger lFirst = new AtomicInteger(1);
        AtomicInteger lSlipNo = new AtomicInteger();
        decideProductionResultsVos.stream().filter(a -> a.getActQy() >= 0 && a.getSlipNo() == 0).forEach(a -> {
            if (lLineNo.get() > 9 || !lDlvschedDate.get().equals(a.getDlvschedDate()) || lMailNo.get() != a.getMailNo() || lStoreId.get() != a.getStoreId() || lDepartmentId.get() != a.getDepartmentId() || lFirst.get() == 1) {
                lLineNo.set(1);
                lSlipNo.set(iMtCenterstatusService.getSlip(bo.getCenterId()));
                lFirst.set(0);
            }
            a.setSlipNo(lSlipNo.get());
            a.setLineNo(lLineNo.get());

            lLineNo.getAndIncrement();
            lDlvschedDate.set(a.getDlvschedDate());
            lMailNo.set(a.getMailNo());
            lStoreId.set(a.getStoreId());
            lDepartmentId.set(a.getDepartmentId());

            WkDecideProductionResults po = new WkDecideProductionResults();
            BeanUtil.copyProperties(a, po);
            po.setSlipCode(a.getSlipNo());
            po.setId(uuid.toString());
            po.setUserId(Convert.toInt(userId));
            pos.add(po);
        });

        wkDecideProductionResultsMapper.insertBatch(pos);
        trOutTransbillMapper.decideProductionResults(bo.getCenterId(), bo.getMailNo(), userId, bo.getInsFuncId(), bo.getInsOpeId(), uuid.toString());
        trOutTransbillMapper.decideProductionResultsPacks(bo.getCenterId(), bo.getMailNo(), 1, userId, bo.getInsFuncId(), bo.getInsOpeId(), uuid.toString());
        if(!pos.isEmpty()) {
            iMtCenterstatusService.checkSlip(bo.getCenterId(), pos.get(pos.size() - 1).getSlipCode());
        }
    }
}
