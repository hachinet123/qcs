package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.WorkReportBo;
import com.tre.centralkitchen.domain.vo.system.WorkReportVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 作業報告書
 * </p>
 *
 * @author 10253955
 * @since 2023-12-19 13:11
 */
public interface WorkReportService {

    /**
     * 作業報告書の検索
     *
     * @param pageParam Paging parameter
     * @param param     Query parameter
     * @return Json object of data list and total data
     */
    TableDataInfo<WorkReportVo> search(PageQuery pageParam, WorkReportBo param);

    /**
     * 作業報告書のCSVサマリ
     *
     * @param param    Query parameter
     * @param response response
     */
    void downloadCsvSummary(WorkReportBo param, HttpServletResponse response);

    /**
     * downloadPdf
     * @param bo
     * @param response
     */
    void downloadPdf(WorkReportBo bo, HttpServletResponse response);
    
    /**
     * 作業報告書のCSV出力
     *
     * @param param    Query parameter
     * @param response response
     */
    void downloadCsvOutput(WorkReportBo param, HttpServletResponse response);

    /**
     * 作業報告書の印刷
     * @param centerId
     */
    void printWorkReportPdf(Integer centerId) throws IOException;
}
