package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.domain.bo.system.DistributionInstructionBo;
import com.tre.centralkitchen.domain.vo.system.DistributionInstructionPoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 10225441
 */
@Mapper
@DS("postgres")
public interface DistributionInstructionMapper {
    Page<DistributionInstructionPoVo> selectDistributionInstructionProdt(Page<DistributionInstructionPoVo> page, @Param("param") DistributionInstructionBo param);

    Page<DistributionInstructionPoVo> selectDistributionInstructionSched(Page<DistributionInstructionPoVo> page, @Param("param") DistributionInstructionBo param);

    List<DistributionInstructionPoVo> selectDistributionInstructionProdt(@Param("param") DistributionInstructionBo param);

    List<DistributionInstructionPoVo> selectDistributionInstructionSched(@Param("param") DistributionInstructionBo param);

    List<DistributionInstructionPoVo> selectDistributionInstructionProdtPdf(@Param("param") DistributionInstructionBo param);

    List<DistributionInstructionPoVo> selectDistributionInstructionSchedPdf(@Param("param") DistributionInstructionBo param);

    List<DistributionInstructionPoVo> selectStoreListWitchIsExceptClosed(@Param("centerId") Integer centerId, @Param("isExceptClosed") Boolean isExceptClosed);
}
