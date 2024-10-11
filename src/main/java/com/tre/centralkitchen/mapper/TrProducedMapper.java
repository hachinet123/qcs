package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.TrProduced;
import com.tre.centralkitchen.domain.vo.system.TrProducedVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 生産実績トラン Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2022-11-24
 */
@Mapper
@DS("postgres")
public interface TrProducedMapper extends BaseMapperPlus<TrProducedMapper, TrProduced, TrProducedVo> {

    Boolean acquireMeterActualResults(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                      @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    Boolean acquireMeterActualResultsNoDirectiOnData(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                                     @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    Boolean acquireMeterActualResultsLot(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                         @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    Boolean decideActualFixAmountProducts(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                          @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    Boolean acquireMeterActualResultsForHinemos(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                      @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    Boolean acquireMeterActualResultsNoDirectiOnDataForHinemos(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                                     @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    Boolean acquireMeterActualResultsLotForHinemos(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                         @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);
}
