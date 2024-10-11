package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.WkRecievelcuProduced;
import com.tre.centralkitchen.domain.vo.system.WkRecievelcuProducedVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * LCU受信生産実績ワーク Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2022-11-23
 */
@Mapper
@DS("postgres")
public interface WkRecievelcuProducedMapper extends BaseMapperPlus<WkRecievelcuProducedMapper, WkRecievelcuProduced, WkRecievelcuProducedVo> {

    int deleteByCenterId(@Param("centerId") Integer centerId);

    int backUpData(@Param("centerId") Integer centerId);

    int insertData(@Param("arr") List<WkRecievelcuProduced> arr);
}
