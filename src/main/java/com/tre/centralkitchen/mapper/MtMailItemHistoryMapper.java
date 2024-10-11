package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtMailItemHistory;
import com.tre.centralkitchen.domain.vo.system.MtMailItemHistoryVo;
import com.tre.centralkitchen.domain.vo.system.MtMailItemVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
@DS("postgres")
public interface MtMailItemHistoryMapper extends BaseMapperPlus<MtMailItemHistoryMapper, MtMailItemHistory, MtMailItemHistoryVo> {
    void insertFmtHistory(@Param("backupDate") Date backupDate, @Param("centerId") Integer centerId, @Param("userId") String userId, @Param("list") List<MtMailItemVo> paramList, @Param("insFuncId") Long insFuncId, @Param("insOpeId") Long insOpeId);

    void recordImportHistory(@Param("backupDate") Date backupDate,@Param("userId") String userId, @Param("list") List<MtMailItemVo> paramList, @Param("insFuncId") Long insFuncId, @Param("insOpeId") Long insOpeId);
}
