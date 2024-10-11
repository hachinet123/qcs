package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaResultBo;
import com.tre.centralkitchen.domain.vo.system.BacteriaCheckResultVo;
import com.tre.centralkitchen.service.TrBacteriaCheckResultService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Validated
@RequiredArgsConstructor
@RestController
@Api(tags = "菌検査判定")
@RequestMapping("/bacteriaCheckResult")
public class TrBacteriaCheckResultController {

    private final TrBacteriaCheckResultService service;

    @ApiOperation("初期処理")
    @GetMapping("/bacteriaCheckItem/{id}")
    public ResponseResult<BacteriaCheckResultVo> search(@PathVariable Integer id) {
        return ResponseResult.buildOK( service.search(id));
    }

    @ApiOperation("一時保存")
    @PostMapping("/save")
    @FunAndOpe(funType = FunType.BACTERIACHECK, opeType = OpeType.BACTERIACHECK_REQUEST, businessType = BusinessType.UPDATE)
    public ResponseResult save(@RequestBody TrBacteriaResultBo bo) {
        service.save(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation("判定終了")
    @PostMapping("/resultSave")
    @FunAndOpe(funType = FunType.BACTERIACHECK, opeType = OpeType.BACTERIACHECK_REQUEST, businessType = BusinessType.INSERT)
    public ResponseResult saveCheck(@RequestBody TrBacteriaResultBo bo) {
        service.saveCheck(bo);
         service.sendMail(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation("微生物検査結果出力")
    @GetMapping("/pdfExport")
    @Log(title = "生産指示書のPDF出力", businessType = BusinessType.GRANT)
    public void pdfExport(Integer id, HttpServletResponse response) throws  Exception {
        service.bacteriaCheckItemResultPdf(id,response);

    }
}
