package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.vo.system.MailListSearchVo;
import com.tre.centralkitchen.service.IMailGlanceService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/mailGlance")
@RequiredArgsConstructor
@Api(value = "便一覧", tags = {"便一覧"})
public class MailGlanceController {

    private final IMailGlanceService iMailGlanceService;

    /**
     * 便覧する
     */
    @ApiOperation("便一覧の検索")
    @GetMapping()
    @Log(title = "便一覧の検索", businessType = BusinessType.GRANT)
    public ResponseResult<MailListSearchVo> getMailList(@ApiParam(value = "センター", required = true)
                                                        @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY) Integer centerId, PageQuery pageQuery) {
        return ResponseResult.buildOK(iMailGlanceService.getMailList(centerId, pageQuery));
    }

    @ApiOperation("便一覧のcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "便一覧のcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(@ApiParam(value = "センター", required = true) Integer centerId, HttpServletResponse response) {
        iMailGlanceService.downloadCSV(centerId, response);
    }
}
