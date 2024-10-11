package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.DistributionInstructionBo;
import com.tre.centralkitchen.domain.vo.system.DistributionInstructionPoVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 10225441
 */
public interface DistributionInstructionService {
    /**
     * Query distribution instruction data interface
     *
     * @param pageParam Paging parameter
     * @param param     Query parameter
     * @return Json object of data list and total data
     */
    TableDataInfo<DistributionInstructionPoVo> queryDistributionInstruction(PageQuery pageParam, DistributionInstructionBo param);

    /**
     * Export distribution instruction data csv file interface
     *
     * @param param     Query parameter
     * @param response  Http servlet response object
     */
    void downloadDistributionInstructionCsv(DistributionInstructionBo param, HttpServletResponse response);

    /**
     * Export distribution instruction data pdf file interface
     *
     * @param bo       Query parameter
     * @param response Http servlet response object
     */
    void downloadDistributionInstructionPdf(DistributionInstructionBo bo, HttpServletResponse response) throws IOException;

    /**
     * printDistributionInstructionPdf
     * @param centerId
     * @param isClosed
     */
    void printDistributionInstructionPdf(Integer centerId, Integer isClosed) throws IOException;
}
