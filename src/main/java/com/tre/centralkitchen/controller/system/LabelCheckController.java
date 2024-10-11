package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.LabelCheckBo;
import com.tre.centralkitchen.domain.vo.system.IssueCheckDataVo;
import com.tre.centralkitchen.service.LabelCheckService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * <p>
 * ラベルチェックリスト
 * </p>
 *
 * @author 10253955
 * @since 2023-12-11 16:40
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("labelCheck")
@Api(value = "ラベルチェックリスト", tags = {"ラベルチェックリスト"})
public class LabelCheckController {

    private final LabelCheckService labelCheckService;

    @ApiOperation(value = "ラベルチェックリストの検索", tags = {"ラベルチェックリスト"})
    @GetMapping("search")
    @Log(title = "ラベルチェックリスト", businessType = BusinessType.OTHER)
    public ResponseResult<IssueCheckDataVo> search(@Valid LabelCheckBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(labelCheckService.getLabelCheckList(pageQuery, bo));
    }

    @ApiOperation(value = "ラベルチェックリストのPDF出力", tags = {"ラベルチェックリスト"})
    @GetMapping(value = "pdfExport")
    @Log(title = "ラベルチェックリストのPDF出力", businessType = BusinessType.EXPORT)
    public void pdfExport(@Validated LabelCheckBo bo, HttpServletResponse response) {
        labelCheckService.downloadLabelCheckPdf(bo, response);
    }
}
