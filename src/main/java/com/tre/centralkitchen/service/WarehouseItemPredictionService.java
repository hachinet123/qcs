package com.tre.centralkitchen.service;


import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.WarehouseItemModifyBo;
import com.tre.centralkitchen.domain.bo.system.WarehouseItemPredictionBo;
import com.tre.centralkitchen.domain.vo.system.WarehouseItemPredictionVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface WarehouseItemPredictionService {

    TableDataInfo<WarehouseItemPredictionVo> search(WarehouseItemPredictionBo bo, PageQuery page);

    void downloadCSV(WarehouseItemPredictionBo bo, HttpServletResponse response);

    void update(List<WarehouseItemModifyBo> bos);
}
