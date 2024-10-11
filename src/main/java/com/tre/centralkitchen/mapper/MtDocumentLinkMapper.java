package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.DocumentLinkSearchBo;
import com.tre.centralkitchen.domain.po.MtDocumentLink;
import com.tre.centralkitchen.domain.vo.system.WkRecievelcuProducedEscVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface MtDocumentLinkMapper extends BaseMapperPlus<MtDocumentLinkMapper, MtDocumentLink, MtDocumentLink> {
    List<WkRecievelcuProducedEscVo> downloadCSV(@Param("bo") DocumentLinkSearchBo bo);
}
