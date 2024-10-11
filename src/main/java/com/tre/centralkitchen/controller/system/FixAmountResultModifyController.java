package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.FixAmountResultModifyConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.utils.FileUtils;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultModifyBo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultModifyUploadBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.system.FixAmountResultModifyPoVo;
import com.tre.centralkitchen.service.FixAmountResultModifyService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 10225441
 */
@Api(value = "定額指示修正", tags = {"定額指示修正"})
@Slf4j
@RestController
@RequestMapping("fixAmountResultModify")
public class FixAmountResultModifyController {
    @Autowired
    private FixAmountResultModifyService fixAmountResultModifyService;

    @ApiOperation(value = "定額指示修正の検索", tags = {"定額指示修正"})
    @GetMapping(value = "search")
    @Log(title = "定額指示修正の取得", businessType = BusinessType.GRANT)
    public ResponseResult<TableDataInfo<FixAmountResultModifyPoVo>> search(@Validated FixAmountResultModifyBo param, PageQuery pageQuery) {
        return ResponseResult.buildOK(fixAmountResultModifyService.queryFixAmountResultModify(pageQuery, param));
    }

    @ApiOperation(value = "定額指示修正の合計", tags = {"定額指示修正"})
    @GetMapping(value = "sum")
    @Log(title = "定額指示修正の合計", businessType = BusinessType.GRANT)
    public ResponseResult<Map<String, Integer>> sum(@Validated FixAmountResultModifyBo param) {
        return ResponseResult.buildOK(fixAmountResultModifyService.sum(param));
    }

    @ApiOperation(value = "定額指示修正の修正確定", tags = {"定額指示修正"})
    @PostMapping(value = "update")
    @Log(title = "定額指示修正の修正確定", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.CORRECTION_INDICATION, businessType = BusinessType.UPDATE)
    public ResponseResult update(@Validated @RequestBody List<FixAmountResultModifyPoVo> paramList) {
        fixAmountResultModifyService.updateFixAmountResultModify(paramList);
        return ResponseResult.buildOK();
    }

    @ApiOperation(value = "定額指示修正のFMT取込", tags = {"定額指示修正"})
    @PostMapping("fmtImport")
    @Log(title = "定額指示修正FMT取込", businessType = BusinessType.IMPORT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.ADDITIONAL_DATA, businessType = BusinessType.IMPORT)
    public ResponseResult fmtImport(@Validated UploadBo bo,
                                    Boolean warningCheck,
                                    @NotNull(message = SysConstantInfo.FILE_ERROR) MultipartFile file, HttpServletResponse response) throws Exception {
        if (!FileUtils.checkFileType(file)) {
            throw new SysBusinessException(SysConstantInfo.FILE_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_TYPE_ERROR_CODE);
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(FixAmountResultModifyConstants.FMT_ERROR_FILE_NAME)) {
            throw new SysBusinessException(SysConstantInfo.FILE_FMT_NAME_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_FMT_NAME_ERROR_CODE);
        }

        try {
            return ResponseResult.buildOK(fixAmountResultModifyService.fmtImport(bo, file, warningCheck, response));
        } catch (SysBusinessException e) {
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        } catch (Exception e) {
            throw new SysBusinessException(SysConstantInfo.FILE_UPLOAD_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        }
    }

    @ApiOperation(value = "定額指示修正のCSV出力", tags = {"定額指示修正"})
    @GetMapping(value = "csvExport")
    @Log(title = "定額指示修正のCSV出力", businessType = BusinessType.GRANT)
    public void csvExport(@Validated FixAmountResultModifyBo param, HttpServletResponse response) {
        fixAmountResultModifyService.downloadFixAmountResultModifyCsv(new PageQuery(), param, response);
    }

    @ApiOperation(value = "定額指示修正の登録", tags = {"定額指示修正"})
    @PostMapping
    @Log(title = "定額指示修正の登録", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.CORRECTION_INDICATION, businessType = BusinessType.INSERT)
    public ResponseResult add(@Validated @RequestBody FixAmountResultModifyUploadBo bo) {
        fixAmountResultModifyService.add(bo);
        return ResponseResult.buildOK();
    }
}
