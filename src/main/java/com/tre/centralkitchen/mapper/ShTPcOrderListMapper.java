package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.annotation.SqlserverCustomPage;
import com.tre.centralkitchen.common.domain.CustomPageData;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.ListMtMailItemBo;
import com.tre.centralkitchen.domain.po.ShTPcOrderList;
import com.tre.centralkitchen.domain.po.shTOrderQtyTest;
import com.tre.centralkitchen.domain.vo.system.ListMtMailItemVo;
import com.tre.centralkitchen.domain.vo.system.MtMailItemVo;
import com.tre.centralkitchen.domain.vo.system.ShTPcOrderListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


@Mapper
@DS("sqlserver")
public interface ShTPcOrderListMapper extends BaseMapperPlus<ShTPcOrderListMapper, ShTPcOrderList, ShTPcOrderListVo> {
    void updateSqlServerOrderList(@Param("date") Date date, @Param("list") List<ShTPcOrderList> shTPcOrderLists, @Param("mailNo") Integer mailNo, @Param("userId") String userId);

    void updateSqlServerOrderQty(@Param("date") Date date, @Param("list") List<shTOrderQtyTest> shTOrderQtyTests, @Param("mailNo") Integer mailNo, @Param("userId") String userId);

    void mailBasicInsertHistory(@Param("storeId") Integer storeId, @Param("mainNo") Integer mainNo, @Param("vendorId") Integer vendorId);

    void mailItemInsertHistory(@Param("storeId") Integer storeId, @Param("itemId") String itemId, @Param("mailNo") Integer mailNo, @Param("vendorId") Integer vendorId);

    void mailItemFmtInsertHistory(@Param("param") MtMailItemVo vo, @Param("vendorId") Integer vendorId);

    @SqlserverCustomPage
    CustomPageData<ListMtMailItemVo> queryList(@Param("param") ListMtMailItemBo bo, @Param("ip120") String url);
}
