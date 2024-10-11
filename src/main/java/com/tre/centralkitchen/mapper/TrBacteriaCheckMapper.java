package com.tre.centralkitchen.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckBo;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.system.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrBacteriaCheckMapper extends BaseMapperPlus<TrBacteriaCheckMapper, TrBacteriaCheck, TrBacteriaCheckVo> {
    Page<TrBacteriaCheckVo> search(Page<TrBacteriaCheckVo> page, @Param("param") TrBacteriaCheckBo param);

    List<TrBacteriaCheckCSVVo> selectTrBacteriaCheckCSVVo(@Param("param") TrBacteriaCheckBo param);

    List<TrBacteriaCheckTimeCheckDateVo> selectTrBacteriacheckTimeCheckDateVo(@Param("param") List<TrBacteriaCheckCSVVo> param);

    SysparamVo createId();

    void updateParamVal1(String param1);

    TrBacteriaCheck selectTrBacteriaCheck(Integer id);

    List<BacteriaCheckItemVo> bacteriaCheckItemSelect(Integer id);

    List<TrBacteriaCheckTime> bacteriaCheckTimeSelect(Integer id);

    EmailReceiver selectEmailReceiver();

    BacteriaCheckVo selectreqName(Integer id);
}
