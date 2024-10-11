package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.WorkReportBo;
import com.tre.centralkitchen.domain.vo.system.IssueCheckDataVo;
import com.tre.centralkitchen.service.WorkReportService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>
 * 作業報告書
 * </p>
 *
 * @author 10253955
 * @since 2023-12-19 13:08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("workReport")
@Api(value = "作業報告書", tags = {"作業報告書"})
public class WorkReportController {

    private final WorkReportService workReportService;

    @ApiOperation(value = "作業報告書の検索", tags = {"作業報告書"})
    @GetMapping
    @Log(title = "作業報告書の検索", businessType = BusinessType.OTHER)
    public ResponseResult<IssueCheckDataVo> search(@Valid WorkReportBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(workReportService.search(pageQuery, bo));
    }

    @ApiOperation(value = "作業報告書のCSVサマリ", tags = {"作業報告書"})
    @GetMapping(value = "csvSummary")
    @Log(title = "作業報告書のCSVサマリ", businessType = BusinessType.EXPORT)
    public void downloadCsvSummary(@Validated WorkReportBo bo, HttpServletResponse response) {
        workReportService.downloadCsvSummary(bo, response);
    }

    @ApiOperation(value = "作業報告書のPDF印刷", tags = {"作業報告書"})
    @GetMapping(value = "pdfExport")
    @Log(title = "作業報告書のPDF印刷", businessType = BusinessType.EXPORT)
    public void pdfExport(@Validated WorkReportBo bo, HttpServletResponse response) {
        if(Objects.isNull(bo.getPrintIds())) {
            throw new SysBusinessException(SysConstantInfo.NO_DATA_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.NO_DATA_ERROR_CODE);
        }
        workReportService.downloadPdf(bo, response);
    }
    
    @ApiOperation(value = "作業報告書のCSV出力", tags = {"作業報告書"})
    @GetMapping(value = "csvOutput")
    @Log(title = "作業報告書のCSV出力", businessType = BusinessType.EXPORT)
    public void downloadCsvOutput(@Validated WorkReportBo bo, HttpServletResponse response) {
        workReportService.downloadCsvOutput(bo, response);
    }
}
