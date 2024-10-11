package com.tre.centralkitchen.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.MtWarehouseItemBo;
import com.tre.centralkitchen.domain.bo.system.WareHouseItemBo;
import com.tre.centralkitchen.domain.po.MtWarehouseitem;
import com.tre.centralkitchen.domain.vo.system.MtWarehouseItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MtWarehouseItemMapper extends BaseMapperPlus<MtWarehouseItemMapper, MtWarehouseitem, MtWarehouseItemVo> {

    Page<MtWarehouseItemVo> search(@Param("bo") MtWarehouseItemBo bo, Page<MtWarehouseItemVo> page);

    List<MtWarehouseItemVo> search(@Param("bo") MtWarehouseItemBo bo);

    MtWarehouseItemVo queryWarehouseItem(@Param("bo") WareHouseItemBo bo);
}
