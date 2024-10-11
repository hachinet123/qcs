package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.ProductionInstructionBo;
import com.tre.centralkitchen.domain.vo.system.ProductionInstructionPoVo;
import com.tre.centralkitchen.service.ProductionInstructionService;
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

/**
 * @author 10225441
 */
@Api(value = "生産指示書", tags = {"生産指示書"})
@Slf4j
@RestController
@RequestMapping("productionInstruction")
public class ProductionInstructionController {
    @Autowired
    private ProductionInstructionService productionInstructionService;

    @ApiOperation(value = "生産指示書の検索", tags = {"生産指示書"})
    @GetMapping(value = "search")
    @Log(title = "生産指示書の検索", businessType = BusinessType.GRANT)
    public ResponseResult<TableDataInfo<ProductionInstructionPoVo>> search(@Validated ProductionInstructionBo param, PageQuery pageQuery) {
        return ResponseResult.buildOK(productionInstructionService.queryProductionInstruction(pageQuery, param));
    }

    @ApiOperation(value = "生産指示書のCSV出力", tags = {"生産指示書"})
    @GetMapping(value = "csvExport")
    @Log(title = "生産指示書のCSV出力", businessType = BusinessType.GRANT)
    public void csvExport(@Validated ProductionInstructionBo param, HttpServletResponse response) {
        productionInstructionService.downloadProductionInstructionCsv(new PageQuery(), param, response);
    }

    @ApiOperation(value = "生産指示書のPDF出力", tags = {"生産指示書"})
    @GetMapping(value = "pdfExport")
    @Log(title = "生産指示書のPDF出力", businessType = BusinessType.GRANT)
    public void pdfExport(@Validated ProductionInstructionBo param, HttpServletResponse response) {
        productionInstructionService.downloadProductionInstructionPdf(param, response);
    }

}
