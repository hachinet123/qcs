package com.tre.centralkitchen.controller.system;


import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckItemsBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckTimeResultBo;
import com.tre.centralkitchen.domain.vo.system.BacteriaCheckVo;
import com.tre.centralkitchen.domain.vo.system.TrBacteriaCheckVo;
import com.tre.centralkitchen.service.TrBacteriaCheckService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@Api(tags = "菌検査")
@RequestMapping("/bacteriaCheck")
public class TrBacteriaCheckController {

    private final TrBacteriaCheckService service;

    @ApiOperation("菌検査検索")
    @Log(title = "菌検査検索", businessType = BusinessType.GRANT)
    @GetMapping("")
    public ResponseResult<TableDataInfo<TrBacteriaCheckVo>> search(@Valid TrBacteriaCheckBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.search(bo, pageQuery));
    }

    @ApiOperation("テスト菌検査のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "テスト菌検査のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(@Valid TrBacteriaCheckBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }

    @ApiOperation("一時保存")
    @PostMapping("/import")
    @FunAndOpe(funType = FunType.BACTERIACHECK, opeType = OpeType.BACTERIACHECK_REQUEST, businessType = BusinessType.INSERT)
    public ResponseResult save(@RequestBody TrBacteriaCheckTimeResultBo bo) {
        bo.setCheckStatTypeId(1);
        if (bo.getId() == null) {
            Integer id = service.save(bo);
            return ResponseResult.buildOK(id);
        } else {
            service.update(bo);
            return ResponseResult.buildOK(bo.getId());
        }
    }
    @ApiOperation("削除")
    @PostMapping("/del/{id}")
    @FunAndOpe(funType = FunType.BACTERIACHECK, opeType = OpeType.BACTERIACHECK_REQUEST, businessType = BusinessType.UPDATE)
    public ResponseResult delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseResult.buildOK();

    }

    @ApiOperation("初期処理")
    @GetMapping("/bacteriaCheckItem/{id}")
    public ResponseResult<BacteriaCheckVo> bacteriaCheckItemSelect(@PathVariable Integer id) {
        BacteriaCheckVo bacteriaCheckVo = service.bacteriaCheckItemSelect(id);
        return ResponseResult.buildOK(bacteriaCheckVo);
    }

    @ApiOperation("検査依頼")
    @PostMapping("/bacteriaCheckItem")
    @FunAndOpe(funType = FunType.BACTERIACHECK, opeType = OpeType.BACTERIACHECK_REQUEST, businessType = BusinessType.INSERT)
    public ResponseResult bacteriaCheckItemDev(@Validated({TrBacteriaCheckTimeResultBo.save.class, TrBacteriaCheckItemsBo.save.class
    }) @RequestBody TrBacteriaCheckTimeResultBo bo) {
        Integer id = bo.getId();
        bo.setCheckStatTypeId(2);
        if (id == null) {
            id = service.save(bo);
        } else {
            id = service.update(bo);
        }
            bo.setId(id);
            service.bacteriaCheckItemResultImport(bo);
            return ResponseResult.buildOK();
    }
}
