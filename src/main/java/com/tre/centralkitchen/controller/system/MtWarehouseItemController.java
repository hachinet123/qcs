package com.tre.centralkitchen.controller.system;


import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.UserIdDict;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.MtWarehouseItemBo;
import com.tre.centralkitchen.domain.bo.system.WareHouseItemBo;
import com.tre.centralkitchen.domain.vo.system.MtWarehouseItemVo;
import com.tre.centralkitchen.service.MtWarehouseItemService;
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
@RequiredArgsConstructor
@RequestMapping("/warehouse")
@Api(tags = "原材料倉庫割当")
public class MtWarehouseItemController {

    private final MtWarehouseItemService service;

    @GetMapping()
    @ApiOperation("原材料倉庫割当の初期処理")
    public ResponseResult<TableDataInfo<MtWarehouseItemVo>> search(@Valid MtWarehouseItemBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.search(bo, pageQuery));
    }

    @GetMapping("/downloadCSV")
    @ApiOperation("原材料倉庫割当のcsv出力")
    public ResponseResult downloadCSV(@Valid MtWarehouseItemBo mtWarehouseitemBo, HttpServletResponse response) {
        service.downloadCSV(mtWarehouseitemBo, response);
        return ResponseResult.buildOK();
    }

    @PostMapping()
    @ApiOperation("登録はこちら")
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.WAREHOUSEITEM_REQUEST, businessType = BusinessType.INSERT)
    public ResponseResult save(@Valid @RequestBody WareHouseItemBo wareHouseItemBo) {
        service.save(wareHouseItemBo);
        return ResponseResult.buildOK();
    }

    @GetMapping("/info")
    @ApiOperation("単一の検索")
    @UserIdDict
    public ResponseResult info(@Valid WareHouseItemBo bo) {
        return ResponseResult.buildOK(service.info(bo));
    }

    @PutMapping()
    @ApiOperation("編集")
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.WAREHOUSEITEM_REQUEST, businessType = BusinessType.UPDATE)
    public ResponseResult update(@Valid @RequestBody WareHouseItemBo bo) {
        service.update(bo);
        return ResponseResult.buildOK();

    }

    @DeleteMapping()
    @ApiOperation("削除")
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.WAREHOUSEITEM_REQUEST, businessType = BusinessType.DELETE)
    public ResponseResult delete(@Valid @RequestBody WareHouseItemBo bo) {
        service.delete(bo);
        return ResponseResult.buildOK();
    }
}
