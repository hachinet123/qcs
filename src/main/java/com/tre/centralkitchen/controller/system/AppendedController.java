package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.utils.FileUtils;
import com.tre.centralkitchen.domain.bo.system.*;
import com.tre.centralkitchen.domain.vo.system.AppendedUpdateVo;
import com.tre.centralkitchen.service.AppendedService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Api(value = "出庫データ追加修正コントローラ", tags = {"出庫データ追加修正"})
@RestController
@RequiredArgsConstructor
@RequestMapping("appended")
@Validated
public class AppendedController {

    private final AppendedService appendedService;

    @ApiOperation(value = "出庫データ追加修正の検索", tags = {"出庫データ追加修正"})
    @GetMapping
    @Log(title = "出庫データ追加修正の検索", businessType = BusinessType.GRANT)
    public ResponseResult<TableDataInfo<AppendedUpdateVo>> search(@Validated AppendedSearchBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(appendedService.queryAppended(pageQuery, bo));
    }

    @ApiOperation(value = "出庫データ追加修正のcsv出力", tags = {"出庫データ追加修正"})
    @GetMapping("downloadCSV")
    @Log(title = "出庫データ追加修正csv出力", businessType = BusinessType.EXPORT)
    public void downloadCSV(@Validated AppendedSearchBo bo, HttpServletResponse response) {
        appendedService.downloadCSV(bo, response);
    }

    @ApiOperation(value = "出庫データ追加修正の修正確定", tags = {"出庫データ追加修正"})
    @PutMapping
    @Log(title = "出庫データ追加修正修正確定", businessType = BusinessType.UPDATE)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.ADDITIONAL_DATA, businessType = BusinessType.UPDATE)
    public ResponseResult update(@Validated @RequestBody List<AppendedUpdateBo> boList) {
        appendedService.update(boList);
        return ResponseResult.buildOK();
    }

    @ApiOperation(value = "出庫データ追加修正のFMT取込", tags = {"出庫データ追加修正"})
    @PostMapping("fmtImport")
    @Log(title = "出庫データ追加修正FMT取込", businessType = BusinessType.IMPORT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.ADDITIONAL_DATA, businessType = BusinessType.IMPORT)
    public ResponseResult fmtImport(@Validated UploadBo bo,
                                    @NotNull(message = SysConstantInfo.FILE_ERROR) MultipartFile file,
                                    Boolean warningCheck,
                                    HttpServletResponse response) throws Exception {
        if (!FileUtils.checkFileType(file)) {
            throw new SysBusinessException(SysConstantInfo.FILE_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_TYPE_ERROR_CODE);
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(SysConstants.APPENDED_FILE_NAME)
                && !Objects.requireNonNull(file.getOriginalFilename()).contains(SysConstants.APPENDED_UPDATE_FILE_NAME)) {
            throw new SysBusinessException(SysConstantInfo.FILE_FMT_NAME_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_FMT_NAME_ERROR_CODE);
        }
        try {
            return ResponseResult.buildOK(appendedService.fmtImport(bo, file, warningCheck, response));
        } catch (SysBusinessException e) {
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        } catch (Exception e) {
            throw new SysBusinessException(SysConstantInfo.FILE_UPLOAD_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        }
    }


    @ApiOperation(value = "返品登録はこちら", tags = {"出庫データ追加修正"})
    @PostMapping("mailNo20")
    @Log(title = "返品登録はこちら", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.ADDITIONAL_DATA, businessType = BusinessType.INSERT)
    public ResponseResult saveMailNo20(@RequestBody @Validated AppendedMailNo20Bo bo) {
        appendedService.saveMailNo20(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation(value = "出庫データ追加修正の既存修正", tags = {"出庫データ追加修正"})
    @GetMapping("fmtExport")
    @Log(title = "出庫データ追加修正既存修正", businessType = BusinessType.EXPORT)
    public void fmtExport(@Validated AppendedSearchBo bo, HttpServletResponse response) throws Exception {
        try {
            appendedService.fmtExport(bo, response);
        } catch (Exception e) {
            throw new SysBusinessException(SysConstantInfo.FILE_FMT_EXPORT_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_FMT_EXPORT_ERROR_CODE);
        }
    }
}
