package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.ActualProductionRecoveryBo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionRecoverySearchBo;
import com.tre.centralkitchen.service.ActualProductionRecoveryService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Validated
@Api(value = "生産実績確定復旧", tags = {"生産実績確定復旧"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/actualProductionRecovery")
public class ActualProductionRecoveryController {

    private final ActualProductionRecoveryService service;
    private final TokenTakeApart tokenTakeApart;

    @ApiOperation("生産実績確定復旧の検索")
    @GetMapping()
    @Log(title = "生産実績確定復旧の検索", businessType = BusinessType.GRANT)
    public ResponseResult search(@Valid ActualProductionRecoverySearchBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.search(bo, pageQuery));
    }

    @ApiOperation("生産実績確定復旧の復旧")
    @PostMapping()
    @Log(title = "生産実績確定復旧の復旧", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.PRODUCTION_OBJECTIVES, businessType = BusinessType.INSERT)
    public ResponseResult actualProductionRecovery(@RequestBody @Valid List<ActualProductionRecoveryBo> bos) throws IOException, InterruptedException {
        service.actualProductionRecovery(bos, tokenTakeApart.takeDecryptedUserId());
        return ResponseResult.buildOK();
    }

    @ApiOperation("生産実績確定復旧のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "生産実績確定復旧のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(@Valid ActualProductionRecoverySearchBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }
}
