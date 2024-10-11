package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.TrMailConfirmHoldBo;
import com.tre.centralkitchen.domain.vo.system.TrMailConfirmHoldVo;

public interface TrMailConfirmHoldService {
    TableDataInfo<TrMailConfirmHoldVo> queryList(TrMailConfirmHoldBo bo, PageQuery pageQuery);

    void update(TrMailConfirmHoldBo bo);
}
