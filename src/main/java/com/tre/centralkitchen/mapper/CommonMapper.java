package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.vo.common.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface CommonMapper extends BaseMapperPlus {
    List<CenterListVo> getCenterList();

    List<LineListVo> getLineAllList();

    List<BranchListVo> getBranchList();

    List<BranchListVo> getStoreName(@Param("centerId") Integer centerId, @Param("storeId") Integer storeId);

    List<BranchListVo> queryMailBasicStoreList(@Param("centerId") Integer centerId);

    List<WarehouseVo> getWarehouse(@Param("centerId") Integer centerId);

    List<BacteriacheckListVo> getBacteriacheckLList(Integer typeId);

    List<StoreShortNameVo> getStoreShortName(@Param("centerId") Integer centerId, @Param("isExceptClosed") Boolean isExceptClosed);

}
