package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.TrOutTransbill;
import com.tre.centralkitchen.domain.vo.system.DecideProductionResultsVo;
import com.tre.centralkitchen.domain.vo.system.TrOutTransbillVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 出庫伝票トラン Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2022-12-15
 */
@Mapper
@DS("postgres")
public interface TrOutTransbillMapper extends BaseMapperPlus<TrOutTransbillMapper, TrOutTransbill, TrOutTransbillVo> {
    List<DecideProductionResultsVo> decideProductionResultsTemp(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo, @Param("pctgtTypeId") Integer pctgtTypeId);

    Boolean decideProductionResultsOnFixAmount(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                               @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId, @Param("uuid") String uuid);

    Boolean decideProductionResults(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                    @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId, @Param("uuid") String uuid);

    Boolean decideProductionResultsPacks(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo, @Param("pctgtTypeId") Integer pctgtTypeId,
                                         @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId, @Param("uuid") String uuid);

    Integer getReturnNO(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo);

    Boolean productionResultsReturnBack(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                        @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    Boolean productionResultsReturn(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo,
                                    @Param("userId") String userId, @Param("funcId") Long funcId, @Param("opeId") Long opeId);
}
