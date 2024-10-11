package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfoExtend;
import com.tre.centralkitchen.domain.bo.system.LabelCheckBo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * ラベルチェックリスト
 * </p>
 *
 * @author 10253955
 * @since 2023-12-11 16:42
 */
public interface LabelCheckService {

    /**
     * ラベルチェックリスト検索
     *
     * @param pageParam Paging parameter
     * @param param     Query parameter
     * @return Json object of data list and total data
     */
    TableDataInfoExtend getLabelCheckList(PageQuery pageParam, LabelCheckBo param);

    /**
     * ラベルチェックリストのPDF出力
     *
     * @param param    Query parameter
     * @param response response
     */
    void downloadLabelCheckPdf(LabelCheckBo param, HttpServletResponse response);

    /**
     * ラベルチェックリストのPDF印刷
     *
     * @param centerId center id
     */
    void printLabelCheckInstructionsPdf(Integer centerId) throws IOException;
}
