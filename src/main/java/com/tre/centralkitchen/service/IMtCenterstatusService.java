package com.tre.centralkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tre.centralkitchen.domain.po.MtCenterstatus;

/**
 * <p>
 * センター別環境設定 Service
 * </p>
 *
 * @author JDev
 * @since 2022-12-12
 */
public interface IMtCenterstatusService extends IService<MtCenterstatus> {

    int getSlip ( Integer centerId );

    void checkSlip ( Integer centerId, int slip );
}
