package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.OrderStatusBo;
import com.tre.centralkitchen.domain.vo.system.OrderStatusVo;
import com.tre.centralkitchen.service.IOrderStatusService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/orderStatus")
@Api(value = "受注状況", tags = {"受注状況"})
public class OrderStatusController {

    private final IOrderStatusService orderStatusService;

    @ApiOperation("受注状況確認(ページ)")
    @GetMapping("")
    @Log(title = "受注状況確認(ページ)", businessType = BusinessType.GRANT)
    public ResponseResult<TableDataInfo<OrderStatusVo>> getOrderStatusList(@Valid OrderStatusBo orderStatusBo, PageQuery pageQuery) {
        return ResponseResult.buildOK(orderStatusService.getOrderStatusList(orderStatusBo, pageQuery));
    }

    @ApiOperation("受注状況確認csv")
    @GetMapping("/csv")
    @Log(title = "受注状況確認csv", businessType = BusinessType.GRANT)
    public void csvDownload(@Valid OrderStatusBo orderStatusBo, HttpServletResponse response) {
        orderStatusService.downloadCsv(orderStatusBo, response);
    }

    @ApiOperation("受注状況確認csvSummary")
    @GetMapping("/csvSummary")
    @Log(title = "受注状況確認csv", businessType = BusinessType.GRANT)
    public void csvSummaryDownload(@Valid OrderStatusBo orderStatusBo, HttpServletResponse response) {
        orderStatusService.downloadCsvSummary(orderStatusBo, response);
    }

}
