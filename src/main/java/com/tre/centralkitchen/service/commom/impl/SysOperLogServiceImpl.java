package com.tre.centralkitchen.service.commom.impl;

import cn.hutool.core.bean.BeanUtil;
import com.tre.centralkitchen.common.domain.OperLog;
import com.tre.centralkitchen.common.utils.AddressUtils;
import com.tre.centralkitchen.domain.po.SysOperLog;
import com.tre.centralkitchen.mapper.SysOperLogMapper;
import com.tre.centralkitchen.service.commom.OperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class SysOperLogServiceImpl implements OperLogService {

    private final SysOperLogMapper baseMapper;

    @Async
    @Override
    public void recordOper(final OperLog operLog) {
        SysOperLog sysOperLog = BeanUtil.toBean(operLog, SysOperLog.class);
        sysOperLog.setOperLocation(AddressUtils.getRealAddressByIP(sysOperLog.getOperIp()));
        sysOperLog.setInsTime(new Date());
        baseMapper.insert(sysOperLog);
    }

}
