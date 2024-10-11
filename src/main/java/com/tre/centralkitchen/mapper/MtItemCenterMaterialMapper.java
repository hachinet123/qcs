package com.tre.centralkitchen.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialSearchBo;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialBo;
import com.tre.centralkitchen.domain.po.MtItemCenterMaterial;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterSearchVo;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterialVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MtItemCenterMaterialMapper extends BaseMapperPlus<MtItemCenterMaterialMapper, MtItemCenterMaterial, ItemCenterMaterialVo> {

    Page<ItemCenterMaterialVo> search(Page<ItemCenterMaterialVo> page, @Param("bo") MtItemCenterMaterialSearchBo bo);

    List<ItemCenterMaterialVo> search(@Param("bo") MtItemCenterMaterialSearchBo bo);

     ItemCenterMaterSearchVo queryItemCanterMaterialOne(@Param("bo") MtItemCenterMaterialBo bo);
}
