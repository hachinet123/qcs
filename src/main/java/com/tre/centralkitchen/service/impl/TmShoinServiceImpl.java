package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.vo.common.TmShohinVo;
import com.tre.centralkitchen.mapper.TmShohinMapper;
import com.tre.centralkitchen.service.ITmShohinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TmShoinServiceImpl implements ITmShohinService {
    private final TmShohinMapper tmShohinMapper;

    @Override
    public List<TmShohinVo> findJanStoreAll(Integer storeId, String itemId) {
        return tmShohinMapper.findJanStoreAll(storeId, itemId, true);
    }

    @Override
    public List<TmShohinVo> findJanByCallCodeAll(Integer storeId, Integer callCode) {
        return tmShohinMapper.findJanByCallCodeAll(storeId, callCode, true);
    }

    @Override
    public List<TmShohinVo> findJanStore(Integer storeId, String itemId) {
        List<TmShohinVo> tmShohinVos = tmShohinMapper.findJanStoreAll(storeId, itemId, false);
        return tmShohinVos.stream().filter(a -> a.getPctgtTypeid() == 2).collect(Collectors.toList());
    }

    @Override
    public List<TmShohinVo> findJanByCallCode(Integer storeId, Integer callCode) {
        List<TmShohinVo> tmShohinVos = tmShohinMapper.findJanByCallCodeAll(storeId, callCode, false);
        return tmShohinVos.stream().filter(a -> a.getPctgtTypeid() == 2).collect(Collectors.toList());
    }

    @Override
    public TmShohinVo findByKey(Integer storeId, String itemId, Integer areaId, Boolean inactive) {
        return tmShohinMapper.findByKey(storeId, itemId, areaId, inactive);
    }

    @Override
    public List<TmShohinVo> findCenterJanAll(Integer centerId, String itemId) {
        return tmShohinMapper.findCenterJanAll(centerId, itemId, true);

    }

    @Override
    public List<TmShohinVo> findCenterJanBy(Integer centerId, String itemId) {
        List<TmShohinVo> tmShohinVoList = tmShohinMapper.findCenterJanAll(centerId, itemId, true);
        return tmShohinVoList.stream().filter(a -> a.getFritemTypeid() == 4 || a.getFritemTypeid() == 5 || a.getFritemTypeid() == 6 || a.getFritemTypeid() == 7 || a.getFritemTypeid() == 8).collect(Collectors.toList());
    }

    @Override
    public TmShohinVo findItemNameByCenterCallCode(Integer centerId, Integer callCode) {
        return tmShohinMapper.findItemNameByCenterCallCode(centerId, callCode);
    }

}
