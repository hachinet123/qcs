package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.ListMtMailItemBo;
import com.tre.centralkitchen.domain.vo.system.ListMtMailItemVo;
import com.tre.centralkitchen.service.ListMtMailItemService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 商品別便
 */
@Api(value = "商品別便", tags = {"商品別便"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/listMtMailItem")
public class ListMtMailItemController {
    private final ListMtMailItemService listMtMailItemService;

    @ApiOperation("商品別便のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "商品別便のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(ListMtMailItemBo bo, HttpServletResponse response) {
        listMtMailItemService.downloadCSV(bo, response);
    }

    @ApiOperation("商品別便の検索")
    @GetMapping()
    @Log(title = "商品別便の検索", businessType = BusinessType.GRANT)
    public ResponseResult<ListMtMailItemVo> queryList(@Valid ListMtMailItemBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(listMtMailItemService.queryList(bo, pageQuery));
    }
}
