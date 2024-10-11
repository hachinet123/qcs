package com.tre.centralkitchen.controller.common;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.InstructionsConstants;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.service.DistributionInstructionService;
import com.tre.centralkitchen.service.LabelCheckService;
import com.tre.centralkitchen.service.ProductionInstructionService;
import com.tre.centralkitchen.service.WorkReportService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/print")
@RequiredArgsConstructor
@Api(value = "印刷コントローラ", tags = {"印刷"})
@Validated
@Slf4j
public class PrintController {

    private final ProductionInstructionService productionInstructionService;
    private final DistributionInstructionService distributionInstructionService;
    private final LabelCheckService labelCheckService;
    private final WorkReportService workReportService;

    @Value("${authentication.key}")
    private String authenticationKey;

    @GetMapping("/production-instructions")
    @ApiOperation(value = "生産指示書印刷", tags = {"印刷"})
    @Log(title = "生産指示書印刷", businessType = BusinessType.OTHER)
    public ResponseResult printProductionInstructionPdf(@ApiParam(value = "key", required = true)
                                                        @NotBlank(message = "keyは必須です。") String key,
                                                        @NotNull(message = "センターコードを入力してください。")
                                                        @ApiParam(value = "centerId") Integer centerId) throws IOException {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        log.info("Auto Print Start:" + InstructionsConstants.WEB_PAGE_TITLE_1);
        productionInstructionService.printProductionInstructionPdf(centerId);
        log.info("Auto Print Finish");
        return ResponseResult.buildOK();
    }

    @GetMapping("/distribution-instructions")
    @ApiOperation(value = "振分指示書印刷", tags = {"印刷"})
    @Log(title = "振分指示書印刷", businessType = BusinessType.OTHER)
    public ResponseResult printDistributionInstructionPdf(@ApiParam(value = "key", required = true)
                                                          @NotBlank(message = "keyは必須です。") String key,
                                                          @NotNull(message = "センターコードを入力してください。")
                                                          @ApiParam(value = "centerId") Integer centerId,
                                                          @NotNull(message = "指示書印刷時閉店除く入力してください。")
                                                          @ApiParam(value = "isClosed") Integer isClosed) throws IOException {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        log.info("Auto Print Start:" + InstructionsConstants.WEB_PAGE_TITLE_2);
        distributionInstructionService.printDistributionInstructionPdf(centerId, isClosed);
        log.info("Auto Print Finish");
        return ResponseResult.buildOK();
    }

    @GetMapping("/label-check-instructions")
    @ApiOperation(value = "ラベルチェックリストPDF印刷", tags = {"印刷"})
    @Log(title = "ラベルチェックリストPDF印刷", businessType = BusinessType.OTHER)
    public ResponseResult printLabelCheckInstructionsPdf(@ApiParam(value = "key", required = true)
                                                         @NotBlank(message = "keyは必須です。") String key,
                                                         @NotNull(message = "センターコードを入力してください。")
                                                         @ApiParam(value = "centerId") Integer centerId) throws IOException {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        log.info("Auto Print Start:" + InstructionsConstants.WEB_PAGE_TITLE_3);
        labelCheckService.printLabelCheckInstructionsPdf(centerId);
        log.info("Auto Print Finish");
        return ResponseResult.buildOK();
    }

    @GetMapping("/work-report")
    @ApiOperation(value = "作業報告書印刷", tags = {"印刷"})
    @Log(title = "作業報告書印刷", businessType = BusinessType.OTHER)
    public ResponseResult printWorkReportPdf(@ApiParam(value = "key", required = true)
                                                         @NotBlank(message = "keyは必須です。") String key,
                                                         @NotNull(message = "センターコードを入力してください。")
                                                         @ApiParam(value = "centerId") Integer centerId) throws IOException {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        log.info("Auto Print Start:" + InstructionsConstants.WEB_PAGE_TITLE_4);
        workReportService.printWorkReportPdf(centerId);
        log.info("Auto Print Finish");
        return ResponseResult.buildOK();
    }
}