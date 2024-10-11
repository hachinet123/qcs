package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.MtMailItemBo;
import com.tre.centralkitchen.domain.po.MtMailItem;
import com.tre.centralkitchen.domain.vo.system.MtMailItemVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 便設定個別マスタ
 */
@Mapper
@DS("postgres")
public interface MtMailitemMapper extends BaseMapperPlus<MtMailitemMapper, MtMailItem, MtMailItemVo> {
    Page<MtMailItemVo> queryList(Page<Object> build, @Param("param") MtMailItemBo bo);
    List<MtMailItemVo> queryList(@Param("param") MtMailItemBo bo);

    MtMailItemVo queryByList(@Param("param") MtMailItemBo bo);

    void insertFmt(@Param("centerId") Integer centerId, @Param("userId") String userId, @Param("list") List<MtMailItemVo> paramList, @Param("insFuncId") Long insFuncId, @Param("insOpeId") Long insOpeId);

    void recordImport(@Param("userId") String userId, @Param("list") List<MtMailItemVo> paramList, @Param("insFuncId") Long insFuncId, @Param("insOpeId") Long insOpeId);

    Integer recordImportCheck(@Param("itemId") String itemId, @Param("storeId") Integer storeId, @Param("centerId") Integer centerId);

    @MapKey("id")
    List<Map<String, Object>> selectMailNoWithCenterId(@Param("centerId") Integer centerId);

    @MapKey("id")
    List<Map<String, Object>> selectItemId(@Param("centerId") Integer centerId, @Param("itemIdList") List<String> itemIdList);

    List<MtMailItemVo> selectByIds(@Param("centerId") Integer centerId);

    List<MtMailItemVo> selectBasicMailNo(@Param("param") MtMailItemBo paramList);
}