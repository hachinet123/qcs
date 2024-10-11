package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.ActualProductionRecoveryBo;
import com.tre.centralkitchen.domain.po.MtMailcontrol;
import com.tre.centralkitchen.domain.vo.system.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 便コントロール Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2022-11-25
 */
@Mapper
@DS("postgres")
public interface MtMailcontrolMapper extends BaseMapperPlus<MtMailcontrolMapper, MtMailcontrol, MtMailcontrolVo> {

    List<ActualProductionRecoveryBo> getConfirmBo();

    //Page<MtMailcontrolVo> getActualProductionList(Page<MtMailcontrolVo> page, @Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    List<MtMailcontrolVo> getActualProductionList(@Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    Page<GetActualProductionVo> getActualProductionVoPage(Page<GetActualProductionVo> page, @Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    Page<ActualProductionConfirmVo> actualProductionConfirmVoPage(Page<ActualProductionConfirmVo> page, @Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    Page<FixAmountResultConfirmVo> fixAmountResultConfirmVoPage(Page<FixAmountResultConfirmVo> page, @Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    Page<ActualProductionRecoveryVo> actualProductionRecoveryVoPage(Page<ActualProductionRecoveryVo> page, @Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    List<GetActualProductionVo> getActualProductionVoPage(@Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    List<ActualProductionConfirmVo> actualProductionConfirmVoPage(@Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    List<FixAmountResultConfirmVo> fixAmountResultConfirmVoPage(@Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    List<ActualProductionRecoveryVo> actualProductionRecoveryVoPage(@Param("centerId") Integer centerId, @Param("mailNo") List<Short> mailNo);

    void pcUpBin(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo);

    MtMailcontrolVo getHoldingMail(@Param("centerId") Integer centerId, @Param("mailNo") Integer mailNo);

    List<OrderStatusMailControlVo> getOrderStatus(@Param("centerId") Integer centerId, @Param("mailNos") List<Integer> mailNos);
}
