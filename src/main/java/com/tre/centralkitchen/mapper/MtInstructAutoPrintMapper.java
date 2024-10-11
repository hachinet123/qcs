package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtCenterLineAnyField;
import com.tre.centralkitchen.domain.vo.system.MtInstructAutoPrintVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 10225441
 */
@Mapper
@DS("postgres")
public interface MtInstructAutoPrintMapper extends BaseMapperPlus {

    List<MtInstructAutoPrintVo> selectAllList(@Param(value = "centerId") Integer centerId, @Param(value = "autoPrintTypeId") Integer autoPrintTypeId);

    List<MtCenterLineAnyField> selectLineList(@Param(value = "centerId") Integer centerId);
}