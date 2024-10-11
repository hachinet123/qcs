package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.CustomPageData;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.domain.bo.system.OrderStatusBo;
import com.tre.centralkitchen.domain.bo.system.ProducePlanBo;

import javax.servlet.http.HttpServletResponse;

public interface IOrderStatusService {
    CustomPageData getOrderStatusList(OrderStatusBo orderStatusBo, PageQuery pageQuery);

    void downloadCsvSummary(OrderStatusBo orderStatusBo, HttpServletResponse response);

    void downloadCsv(OrderStatusBo orderStatusBo, HttpServletResponse response);

    void insertProducePlanWk(ProducePlanBo orderStatusBo);
}
