package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.WarehouseItemModifyBo;
import com.tre.centralkitchen.domain.bo.system.WarehouseItemPredictionBo;
import com.tre.centralkitchen.domain.vo.system.WarehouseItemPredictionVo;
import com.tre.centralkitchen.service.WarehouseItemPredictionService;
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
@RequiredArgsConstructor
@RestController
@Api(tags = "在庫予測")
@RequestMapping("/WarehouseItemPrediction")
public class WarehouseItemPredictionController {

    private final WarehouseItemPredictionService service;

    @GetMapping()
    @ApiOperation("在庫予測の検索")
    public ResponseResult<TableDataInfo<WarehouseItemPredictionVo>> search(@Validated WarehouseItemPredictionBo bo, PageQuery page) {
        return ResponseResult.buildOK(service.search(bo, page));
    }

    @ApiOperation("在庫予測のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "在庫予測のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(@Valid WarehouseItemPredictionBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }

    @ApiOperation("在庫予測の在庫修正")
    @PutMapping()
    public ResponseResult update(@Valid @RequestBody List<WarehouseItemModifyBo> bos) {
        service.update(bos);
        return ResponseResult.buildOK();
    }
}
