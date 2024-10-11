package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.TmShohinBo;
import com.tre.centralkitchen.domain.vo.common.TmShohinVo;
import com.tre.centralkitchen.service.TmShohinService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(value = "商品コントローラ", tags = {"商品"})
@RestController
@RequestMapping("/shohin")
@RequiredArgsConstructor
public class TmShohinController {

    private final TmShohinService service;

    @ApiOperation("商品の取得")
    @GetMapping()
    @Log(title = "商品の取得", businessType = BusinessType.GRANT)
    public ResponseResult<TableDataInfo<TmShohinVo>> queryPage(@Validated TmShohinBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.queryPage(bo, pageQuery));
    }

    @ApiOperation(value = "csv出力")
    @GetMapping("downloadCSV")
    @Log(title = "csv出力", businessType = BusinessType.EXPORT)
    public void downloadCSV(@Validated TmShohinBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }
}
