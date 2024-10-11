package com.tre.centralkitchen.service.commom;

import com.tre.centralkitchen.domain.bo.common.CommonCenterLineBo;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.po.MtHome;
import com.tre.centralkitchen.domain.po.Products;
import com.tre.centralkitchen.domain.vo.common.*;

import java.util.List;

public interface MasterService {

    List<CenterListVo> getCenterList();

    List<LineListVo> getLineAllList();

    List<MtProductwkgrpVo> getBigGroupList(CommonCenterLineBo bo);

    List<MailListVo> getMailList(CommonMailNoBo bo);

    List<WorkGroupListVo> getWorkGroupList(String centerId, String lineId);

    List<BranchListVo> getBranchList();

    List<MtPlaceVo> getPlaceList();

    Products getItemName(String itemId);

    List<BranchListVo> getStoreName(Integer centerId, Integer storeId);

    List<MtHome> getHomeData();

    List<BranchListVo> queryMailBasicStoreList(Integer centerId);

    TmShohinVo getItemNameByStoreCallCode(String storeIds, String callCode);

    TmShohinVo getItemNameByStore(String storeIds, String itemId);

    List<WarehouseVo> getWarehouse(Integer centerId);

    List<BacteriacheckListVo> getTemperature();

    List<BacteriacheckListVo> getCheckObj();

    List<BacteriacheckListVo> getCheckTime();

    List<StoreShortNameVo> getStoreShortName(Integer centerId, Boolean isExceptClosed);

    TmShohinVo getItemNameByCenterCallCode(Integer centerId, Integer callCode);
}
