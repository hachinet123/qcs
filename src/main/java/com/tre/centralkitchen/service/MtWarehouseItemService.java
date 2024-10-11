package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.MtWarehouseItemBo;
import com.tre.centralkitchen.domain.bo.system.WareHouseItemBo;
import com.tre.centralkitchen.domain.po.MtWarehouseitem;
import com.tre.centralkitchen.domain.vo.system.MtWarehouseItemVo;

import javax.servlet.http.HttpServletResponse;

public interface MtWarehouseItemService {

    TableDataInfo<MtWarehouseItemVo> search(MtWarehouseItemBo mtWarehouseitemBo, PageQuery pageQuery);

    void downloadCSV(MtWarehouseItemBo mtWarehouseitemBo, HttpServletResponse response);

    void save(WareHouseItemBo wareHouseItemBo);

    MtWarehouseItemVo info(WareHouseItemBo wareHouseItemBo);

    void update(WareHouseItemBo wareHouseItemBo);

    void delete(WareHouseItemBo wareHouseItemBo);
}
