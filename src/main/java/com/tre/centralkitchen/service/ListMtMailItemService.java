package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.ListMtMailItemBo;
import com.tre.centralkitchen.domain.vo.system.ListMtMailItemVo;

import javax.servlet.http.HttpServletResponse;

/**
 * 商品別便
 */
public interface ListMtMailItemService {
    void downloadCSV(ListMtMailItemBo bo, HttpServletResponse response);

    TableDataInfo<ListMtMailItemVo> queryList(ListMtMailItemBo bo, PageQuery pageQuery);
}