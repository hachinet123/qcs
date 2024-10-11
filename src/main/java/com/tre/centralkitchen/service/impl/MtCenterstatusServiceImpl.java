package com.tre.centralkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tre.centralkitchen.domain.po.MtCenterstatus;
import com.tre.centralkitchen.mapper.MtCenterstatusMapper;
import com.tre.centralkitchen.service.IMtCenterstatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * センター別環境設定 ServiceImpl
 * </p>
 *
 * @author JDev
 * @since 2022-12-12
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class MtCenterstatusServiceImpl extends ServiceImpl<MtCenterstatusMapper, MtCenterstatus> implements IMtCenterstatusService {

    @Override
    public int getSlip ( Integer centerId ) {
        int intSlipNo = 0;
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("center_id", centerId);
        log.info("centerId:" + centerId);
        if (baseMapper.selectCount(queryWrapper) > 0) {
            MtCenterstatus mtCenterstatusPo = baseMapper.selectOne(queryWrapper);
            intSlipNo = mtCenterstatusPo.getSlipCode() * 1000000 + mtCenterstatusPo.getSlipNo();
            log.info("intSlipNo:" + intSlipNo);
            if (mtCenterstatusPo.getSlipNo() >= mtCenterstatusPo.getMaxSlipNo()) {
                mtCenterstatusPo.setSlipNo(mtCenterstatusPo.getMinSlipNo());
                baseMapper.update(mtCenterstatusPo, queryWrapper);
                log.info("SlipNo1:" + mtCenterstatusPo.getSlipNo());
            } else {
                mtCenterstatusPo.setSlipNo(mtCenterstatusPo.getSlipNo() + 1);
                baseMapper.update(mtCenterstatusPo, queryWrapper);
                log.info("SlipNo2:" + mtCenterstatusPo.getSlipNo());
            }
        } else {
            MtCenterstatus model = new MtCenterstatus();
            model.setCenterId(centerId);
            model.setSlipNo(1);
            model.setSlipCode(1);
            baseMapper.insert(model);
            log.info("SlipNo3:" + model.getSlipNo());
        }
        return intSlipNo;
    }

    @Override
    public void checkSlip ( Integer centerId, int slip ) {
        log.info("checkSlip:" + slip);
        QueryWrapper<MtCenterstatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("center_id", centerId);
        MtCenterstatus mtCenterstatusPo = baseMapper.selectOne(queryWrapper);
        slip = slip - mtCenterstatusPo.getSlipCode() * 1000000;
        if (mtCenterstatusPo.getSlipNo() != slip + 1) {
            mtCenterstatusPo.setSlipNo(slip + 1);
            baseMapper.update(mtCenterstatusPo, queryWrapper);
        }
    }
}
