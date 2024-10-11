package com.tre.centralkitchen.mapper;

import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtWarehouseitem;
import com.tre.centralkitchen.domain.po.TrStock;
import com.tre.centralkitchen.domain.vo.system.MtWarehouseItemVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TrStockMapper extends BaseMapperPlus<TrStockMapper, TrStock, TrStock> {

}
