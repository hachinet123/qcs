package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.domain.bo.system.ProductionInstructionBo;
import com.tre.centralkitchen.domain.vo.system.ProductionInstructionPoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 10225441
 */
@Mapper
@DS("postgres")
public interface ProductionInstructionMapper {
    Page<ProductionInstructionPoVo> selectProductionInstructionProdt(Page<ProductionInstructionPoVo> page, @Param("param") ProductionInstructionBo param);

    Page<ProductionInstructionPoVo> selectProductionInstructionSched(Page<ProductionInstructionPoVo> page, @Param("param") ProductionInstructionBo param);

    List<ProductionInstructionPoVo> selectProductionInstructionProdt(@Param("param") ProductionInstructionBo param);

    List<ProductionInstructionPoVo> selectProductionInstructionSched(@Param("param") ProductionInstructionBo param);
}
