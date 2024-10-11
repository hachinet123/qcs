package com.tre.centralkitchen.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.TmShohinBo;
import com.tre.centralkitchen.domain.po.TmShohin;
import com.tre.centralkitchen.domain.vo.common.TmShohinVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2023-01-28
 */
public interface TmShohinMapper extends BaseMapperPlus<TmShohinMapper, TmShohin, TmShohinVo> {

    List<TmShohinVo> findJanStoreAll(@Param("storeId") Integer storeId, @Param("itemId") String itemId, @Param("inactive") Boolean inactive);

    TmShohinVo findByKey(@Param("storeId") Integer storeId, @Param("itemId") String itemId, @Param("areaId") Integer areaId, @Param("inactive") Boolean inactive);

    List<TmShohinVo> findJanByCallCodeAll(@Param("storeId") Integer storeId, @Param("callCode") Integer callCode, @Param("inactive") Boolean inactive);

    List<TmShohinVo> findCenterJanAll(@Param("centerId") Integer centerId, @Param("itemId") String itemId, @Param("inactive") Boolean inactive);

    TmShohinVo findItemNameByCenterCallCode(@Param("centerId") Integer centerId, @Param("callCode") Integer callCode);

    Page<TmShohinVo> queryPage(Page<TmShohinVo> page, @Param("param") TmShohinBo bo);

    List<TmShohinVo> queryPage(@Param("param") TmShohinBo bo);
}
