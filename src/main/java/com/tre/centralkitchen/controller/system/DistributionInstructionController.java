package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.DistributionInstructionBo;
import com.tre.centralkitchen.domain.vo.system.DistributionInstructionPoVo;
import com.tre.centralkitchen.service.DistributionInstructionService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 10225441
 */
@Api(value = "振分指示書", tags = {"振分指示書"})
@Slf4j
@RestController
@RequestMapping("distributionInstruction")
@Validated
public class DistributionInstructionController {
    @Autowired
    private DistributionInstructionService distributionInstructionService;

    @ApiOperation(value = "振分指示書の検索", tags = {"振分指示書"})
    @GetMapping(value = "search")
    @Log(title = "振分指示書の検索", businessType = BusinessType.GRANT)
    public ResponseResult<TableDataInfo<DistributionInstructionPoVo>> search(@Validated DistributionInstructionBo param, PageQuery pageQuery) {
        return ResponseResult.buildOK(distributionInstructionService.queryDistributionInstruction(pageQuery, param));
    }

    @ApiOperation(value = "振分指示書のCSV出力", tags = {"振分指示書"})
    @GetMapping(value = "csvExport")
    @Log(title = "振分指示書のCSV出力", businessType = BusinessType.EXPORT)
    public void csvExport(@Validated DistributionInstructionBo param, HttpServletResponse response) {
        distributionInstructionService.downloadDistributionInstructionCsv(param, response);
    }

    @ApiOperation(value = "振分指示書のPDF出力", tags = {"振分指示書"})
    @GetMapping(value = "pdfExport")
    @Log(title = "振分指示書のPDF出力", businessType = BusinessType.EXPORT)
    public void pdfExport(@Validated DistributionInstructionBo bo, HttpServletResponse response) throws IOException {
        distributionInstructionService.downloadDistributionInstructionPdf(bo, response);
    }
}
