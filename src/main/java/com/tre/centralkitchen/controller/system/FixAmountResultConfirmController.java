package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultConfirmBo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultConfirmSearchBo;
import com.tre.centralkitchen.service.FixAmountResultConfirmService;
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
@Api(value = "定額実績確定", tags = {"定額実績確定"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/fixAmountResultConfirm")
public class FixAmountResultConfirmController {
    private final FixAmountResultConfirmService service;
    private final TokenTakeApart tokenTakeApart;


    @ApiOperation("定額実績確定の検索")
    @GetMapping()
    @Log(title = "定額実績確定の検索", businessType = BusinessType.GRANT)
    public ResponseResult search(@Valid FixAmountResultConfirmSearchBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.search(bo, pageQuery));
    }

    @ApiOperation("定額実績確定の確定")
    @PostMapping()
    @Log(title = "定額実績確定の確定", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.DETERMINATION_OBJECTIVE, businessType = BusinessType.INSERT)
    public ResponseResult fixAmountResultConfirm(@RequestBody @Valid List<FixAmountResultConfirmBo> bos) {
        synchronized (this) {
            service.fixAmountResultConfirm(bos, tokenTakeApart.takeDecryptedUserId());
        }
        return ResponseResult.buildOK();
    }

    @ApiOperation("定額実績確定のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "定額実績確定のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(@Valid FixAmountResultConfirmSearchBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }
}
