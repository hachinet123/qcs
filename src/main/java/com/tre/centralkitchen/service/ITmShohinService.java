package com.tre.centralkitchen.service;


import com.tre.centralkitchen.domain.vo.common.TmShohinVo;

import java.util.List;

public interface ITmShohinService {

    List<TmShohinVo> findJanStoreAll(Integer storeId, String itemId);

    List<TmShohinVo> findJanByCallCodeAll(Integer storeId, Integer callCode);

    List<TmShohinVo> findJanStore(Integer storeId, String itemId);

    List<TmShohinVo> findJanByCallCode(Integer storeId, Integer callCode);

    TmShohinVo findByKey(Integer storeId, String itemId, Integer areaId, Boolean inactive);

    List<TmShohinVo> findCenterJanAll(Integer centerId, String itemId);

    List<TmShohinVo> findCenterJanBy(Integer centerId, String itemId);

    TmShohinVo findItemNameByCenterCallCode(Integer centerId, Integer callCode);
}