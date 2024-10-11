package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmBo;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmSearchBo;
import com.tre.centralkitchen.service.ActualProductionConfirmService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Validated
@Api(value = "生産実績確定", tags = {"生産実績確定"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/actualProductionConfirm")
public class ActualProductionConfirmController {
    private final ActualProductionConfirmService service;
    private final TokenTakeApart tokenTakeApart;

    @ApiOperation("生産実績確定の検索")
    @GetMapping()
    @Log(title = "生産実績確定の検索", businessType = BusinessType.GRANT)
    public ResponseResult search(@Valid ActualProductionConfirmSearchBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.search(bo, pageQuery));
    }

    @ApiOperation("生産実績確定の確定")
    @PostMapping()
    @Log(title = "生産実績確定の確定", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.DETERMINATION_OBJECTIVES, businessType = BusinessType.INSERT)
    public ResponseResult actualProductionConfirm(@RequestBody @Valid List<ActualProductionConfirmBo> bos) {
        synchronized (this) {
            service.actualProductionConfirm(bos, tokenTakeApart.takeDecryptedUserId());
        }
        return ResponseResult.buildOK();
    }

    @ApiOperation("生産実績確定のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "生産実績確定のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(@Valid ActualProductionConfirmSearchBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }
}
