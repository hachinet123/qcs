package com.tre.centralkitchen.service;


import com.tre.centralkitchen.domain.vo.system.MtSysparamVo;

/**
 * @author JDev
 * @since 2022-12-27
 */
public interface IMtSysparamService {

    MtSysparamVo getParam(Integer systemId, Integer paramId);
}
