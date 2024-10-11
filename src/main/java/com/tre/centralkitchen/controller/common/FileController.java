package com.tre.centralkitchen.controller.common;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.service.commom.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
@Api(value = "ファイルアクションンコントローラ", tags = {"ファイル"})
@Validated
public class FileController {

    private final FileService fileService;

    @GetMapping("/downloadFmt")
    @ApiOperation(value = "Fmt出力", tags = {"ファイル"})
    @Log(title = "テンプレートファイルのダウンロード", businessType = BusinessType.EXPORT)
    public void downloadFmt(@ApiParam(value = "テンプレートID", required = true)
                            @NotBlank(message = "テンプレートIDを入力してください。") String fmtId,
                            HttpServletResponse response) {
        fileService.downloadFmt(fmtId, response);
    }

    @GetMapping("/download/{fileName}")
    @ApiOperation(value = "生産指示_生産指示書印刷", tags = {"印刷"})
    @Log(title = "テンプレートファイルのダウンロード", businessType = BusinessType.OTHER)
    public void downloadFile(@PathVariable(required = false) String fileName, HttpServletResponse response) {
        fileService.downloadFile(fileName, response);
    }
}