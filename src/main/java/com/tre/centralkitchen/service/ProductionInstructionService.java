package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.ProductionInstructionBo;
import com.tre.centralkitchen.domain.vo.system.ProductionInstructionPoVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 10225441
 */
public interface ProductionInstructionService {

    TableDataInfo<ProductionInstructionPoVo> queryProductionInstruction(PageQuery pageParam, ProductionInstructionBo param);

    void downloadProductionInstructionCsv(PageQuery pageParam, ProductionInstructionBo param, HttpServletResponse response);

    void downloadProductionInstructionPdf(ProductionInstructionBo param, HttpServletResponse response);

    void printProductionInstructionPdf(Integer centerId) throws IOException;

}
