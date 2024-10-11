package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmBo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmSearchBo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ActualProductionConfirmService {

    void actualProductionConfirm(List<ActualProductionConfirmBo> bos, String userId);

    TableDataInfo search(ActualProductionConfirmSearchBo bo, PageQuery pageQuery);

    void downloadCSV(ActualProductionConfirmSearchBo bo, HttpServletResponse response);
}
