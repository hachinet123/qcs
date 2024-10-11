package com.tre.centralkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.domain.po.MtSysparam;
import com.tre.centralkitchen.domain.vo.system.MtSysparamVo;
import com.tre.centralkitchen.mapper.MtSysparamMapper;
import com.tre.centralkitchen.service.IMtSysparamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author JDev
 * @since 2022-12-27
 */
@Service
public class MtSysparamServiceImpl implements IMtSysparamService {
    @Resource
    private MtSysparamMapper mtSysparamMapper;

    @Override
    public MtSysparamVo getParam(Integer systemId, Integer paramId) {
        QueryWrapper<MtSysparam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("system_id", systemId);
        queryWrapper.eq("param_id", paramId);
        queryWrapper.eq("f_del", 0);
        return mtSysparamMapper.selectVoOne(queryWrapper);
    }
}
