package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionBo;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionSearchBo;
import com.tre.centralkitchen.service.GetActualProductionService;
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
@Api(value = "生産実績取得", tags = {"生産実績取得"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/getActualProduction")
public class GetActualProductionController {

    private final GetActualProductionService service;
    private final TokenTakeApart tokenTakeApart;

    @ApiOperation("生産実績取得の検索")
    @GetMapping()
    @Log(title = "生産実績取得の検索", businessType = BusinessType.GRANT)
    public ResponseResult search(@Valid GetActualProductionSearchBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.search(bo, pageQuery));
    }

    @ApiOperation("生産実績取得の取得")
    @PostMapping()
    @Log(title = "生産実績取得の取得", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.ACHIEVEMENTS_PRODUCTION, businessType = BusinessType.INSERT)
    public ResponseResult getActualProduction(@RequestBody @Valid List<GetActualProductionBo> bos) throws IOException, InterruptedException {
        service.getActualProduction(bos, tokenTakeApart.takeDecryptedUserId());
        return ResponseResult.buildOK();
    }

    @ApiOperation("生産実績取得のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "生産実績取得のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(@Valid GetActualProductionSearchBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }
}
