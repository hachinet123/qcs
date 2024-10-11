package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.annotation.SqlserverCustomPage;
import com.tre.centralkitchen.common.domain.CustomPageData;
import com.tre.centralkitchen.domain.dto.OrderStatusDto;
import com.tre.centralkitchen.domain.vo.system.OrderStatusStoreVo;
import com.tre.centralkitchen.domain.vo.system.OrderStatusVo;
import com.tre.centralkitchen.domain.vo.system.ProducePlanVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * order mapper
 *
 * @date 2022-11-25
 */
@Mapper
@DS("sqlserver")
public interface OrderStatusMapper {
    @SqlserverCustomPage
    CustomPageData<OrderStatusVo> getOrderStatusList(@Param("bo") OrderStatusDto orderStatusBo, @Param("ip20") String ip20, @Param("ip120") String ip120);

    List<ProducePlanVo> getProducePlan(@Param("bo") OrderStatusDto orderStatusBo, @Param("ip20") String ip20, @Param("ip120") String ip120);

    @SqlserverCustomPage
    CustomPageData<OrderStatusStoreVo> getOrderStatusListStore(@Param("bo") OrderStatusDto orderStatusBo, @Param("ip20") String ip20, @Param("ip120") String ip120);
}
