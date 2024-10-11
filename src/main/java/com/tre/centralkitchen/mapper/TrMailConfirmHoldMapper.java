package com.tre.centralkitchen.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.TrMailConfirmHoldBo;
import com.tre.centralkitchen.domain.po.TrMailConfirmHold;
import com.tre.centralkitchen.domain.vo.system.TrMailConfirmHoldVo;
import org.apache.ibatis.annotations.Param;

public interface TrMailConfirmHoldMapper extends BaseMapperPlus<TrMailConfirmHoldMapper, TrMailConfirmHold, TrMailConfirmHoldVo> {
    Page<TrMailConfirmHoldVo> queryList(@Param("param") TrMailConfirmHoldBo bo, Page<Object> build);
}
