package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.system.AppendedSearchBo;
import com.tre.centralkitchen.domain.bo.system.DocumentLinkSearchBo;
import com.tre.centralkitchen.domain.po.MtDocumentLink;
import com.tre.centralkitchen.service.MtDocumentLinkService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "ドキュメントリンクマスタントローラ", tags = {"リンク"})
@RestController
@RequestMapping("/documentLink")
@RequiredArgsConstructor
public class MtDocumentLinkController {

    private final MtDocumentLinkService service;

    @ApiOperation("リンクの取得")
    @GetMapping()
    @Log(title = "リンクの取得", businessType = BusinessType.GRANT)
    public ResponseResult<List<MtDocumentLink>> getDocumentLinkList() {
        return ResponseResult.buildOK(service.getDocumentLinkList());
    }

    @ApiOperation(value = "csv出力")
    @GetMapping("downloadCSV")
    @Log(title = "csv出力", businessType = BusinessType.EXPORT)
    public void downloadCSV(@Validated DocumentLinkSearchBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }
}
