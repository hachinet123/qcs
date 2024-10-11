package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionRecoveryBo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionRecoverySearchBo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ActualProductionRecoveryService {
    void actualProductionRecovery(List<ActualProductionRecoveryBo> bos, String userId) throws IOException, InterruptedException;

    TableDataInfo search(ActualProductionRecoverySearchBo bo, PageQuery pageQuery);

    void downloadCSV(ActualProductionRecoverySearchBo bo, HttpServletResponse response);
}
