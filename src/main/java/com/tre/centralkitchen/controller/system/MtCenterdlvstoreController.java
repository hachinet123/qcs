package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.CenterdlvstoreBo;
import com.tre.centralkitchen.domain.bo.system.MtCenterdlvstoreBo;
import com.tre.centralkitchen.domain.vo.system.CenterdlvstoreVo;
import com.tre.centralkitchen.domain.vo.system.MtCenterdlvstoreVo;
import com.tre.centralkitchen.service.MtCenterdlvstoreService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/centerdlvstore")
@RequiredArgsConstructor
@Api(tags ="センター別ライン別管轄店舗")
public class MtCenterdlvstoreController {

    private final MtCenterdlvstoreService service;

    @ApiOperation("検索")
    @GetMapping("/search")
    public ResponseResult<TableDataInfo<MtCenterdlvstoreVo>> search(MtCenterdlvstoreBo bo, PageQuery pageQuery){
            return ResponseResult.buildOK(service.search(bo,pageQuery));
    }

    @ApiOperation("csv出力")
    @GetMapping("/downloadCSV")
    public ResponseResult downloadCSV(MtCenterdlvstoreBo bo,  HttpServletResponse response){
        service.downloadCSV(bo,response);
        return ResponseResult.buildOK();
    }

    @ApiOperation("登録はこちら")
    @PostMapping("/save")
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.CENTERD_LVSTORE_REQUEST, businessType = BusinessType.INSERT)
    public ResponseResult save(@Valid @RequestBody CenterdlvstoreBo bo){
        service.save(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation("単一の検索。")
    @GetMapping("/centerdlvstoreSelect")

    public ResponseResult info(CenterdlvstoreBo bo){
        return ResponseResult.buildOK(service.info(bo));
    }

    @ApiOperation("編集")
    @PutMapping("/centerdlvstoreUpdate")
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.CENTERD_LVSTORE_REQUEST, businessType = BusinessType.UPDATE)
    public ResponseResult update(@Valid @RequestBody CenterdlvstoreBo bo){
         service.update(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation("削除")
    @PutMapping("/centerdlvstoreDel")
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.CENTERD_LVSTORE_REQUEST, businessType = BusinessType.UPDATE)
    public ResponseResult delete(@Valid @RequestBody CenterdlvstoreBo bo){
        service.delete(bo);
        return ResponseResult.buildOK();
    }
}
