package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.IssueCheckDataBo;
import com.tre.centralkitchen.domain.vo.system.IssueCheckDataVo;
import com.tre.centralkitchen.service.IssueCheckDataService;
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

@Validated
@Api(value = "出庫データ確認", tags = {"出庫データ確認"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/issueCheckData")
public class IssueCheckDataController {
    private final IssueCheckDataService issueCheckDataService;

    @ApiOperation("出庫データ確認の検索")
    @GetMapping()
    @Log(title = "出庫データ確認の検索", businessType = BusinessType.GRANT)
    public ResponseResult<IssueCheckDataVo> queryList(@Valid IssueCheckDataBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(issueCheckDataService.queryList(bo, pageQuery));
    }

    @ApiOperation("出庫データ確認のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "出庫データ確認のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(IssueCheckDataBo bo, HttpServletResponse response) {
        issueCheckDataService.downloadCSV(bo, response);
    }
}
