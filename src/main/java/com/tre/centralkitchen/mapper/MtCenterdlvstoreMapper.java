package com.tre.centralkitchen.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.CenterdlvstoreBo;
import com.tre.centralkitchen.domain.bo.system.MtCenterdlvstoreBo;
import com.tre.centralkitchen.domain.po.MtCenterdlvstore;
import com.tre.centralkitchen.domain.vo.system.CenterdlvstoreMasterVo;
import com.tre.centralkitchen.domain.vo.system.CenterdlvstoreVo;
import com.tre.centralkitchen.domain.vo.system.MtCenterdlvstoreVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface MtCenterdlvstoreMapper extends BaseMapperPlus<MtCenterdlvstoreMapper, MtCenterdlvstore, MtCenterdlvstoreVo> {

    Page<MtCenterdlvstoreVo> search(Page<MtCenterdlvstoreVo> page, @Param("param") MtCenterdlvstoreBo param);

    CenterdlvstoreVo centerdlvstoreSelect(CenterdlvstoreBo centerdlvstoreBo);

    List<CenterdlvstoreMasterVo> centerSelect();

}
