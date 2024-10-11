package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.annotation.UserIdDict;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.utils.FileUtils;
import com.tre.centralkitchen.domain.bo.system.MtMailItemBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.system.MtMailItemVo;
import com.tre.centralkitchen.service.IMtMailItemService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * 便設定個別マスタ
 */

@Api(value = "便設定個別マスタ", tags = {"便設定個別マスタ"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/mtMailItem")
public class MtMailItemController {
    private final IMtMailItemService mtMailItemService;

    @ApiOperation("便設定個別マスタの検索")
    @GetMapping()
    @Log(title = "便設定個別マスタの検索", businessType = BusinessType.GRANT)
    public ResponseResult<MtMailItemVo> queryList(@Valid MtMailItemBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(mtMailItemService.queryList(bo, pageQuery));
    }

    @ApiOperation("便設定個別マスタのcsv出力")
    @GetMapping("/downloadCSV")
    @Log(title = "便設定個別マスタのcsv出力", businessType = BusinessType.GRANT)
    public void downloadCSV(MtMailItemBo bo, HttpServletResponse response) {
        mtMailItemService.downloadCSV(bo, response);
    }

    @ApiOperation("便設定個別マスタの更新")
    @PutMapping()
    @Log(title = "便設定個別マスタの更新", businessType = BusinessType.UPDATE)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.MAIL, businessType = BusinessType.UPDATE)
    public ResponseResult<Void> update(@Valid @RequestBody MtMailItemBo bo) {
        if (bo.getMailNo() != null && !bo.getMailNo().equals("")) {
            mtMailItemService.updateSqlserverPcOrderList(bo);
            mtMailItemService.updateSqlserverOrderQty(bo);
            return ResponseResult.buildOK(mtMailItemService.updateByBo(bo) ? 1 : 0);
        } else {
            throw new SysBusinessException(SysConstantInfo.MAIL_NO_NOT_EMPTY, HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    @ApiOperation("便設定個別マスタの取得の更新")
    @GetMapping("/info")
    @Log(title = "便設定個別マスタの取得の更新", businessType = BusinessType.GRANT)
    @UserIdDict
    public ResponseResult<MtMailItemVo> queryById(@Valid MtMailItemBo bo) {
        return ResponseResult.buildOK(mtMailItemService.queryByList(bo));
    }

    @ApiOperation("便設定個別マスタの削除")
    @DeleteMapping()
    @Log(title = "便設定個別マスタの削除", businessType = BusinessType.DELETE)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.MAIL, businessType = BusinessType.DELETE)
    public ResponseResult<Void> deleteById(@Valid @RequestBody MtMailItemBo bo) {
        mtMailItemService.deleteSqlserverOrderList(bo);
        mtMailItemService.deleteSqlserverOrderQty(bo);
        return ResponseResult.buildOK(mtMailItemService.deleteWithValidByIds(bo, true) ? 1 : 0);
    }

    @ApiOperation(value = "便設定個別マスタのFMT取込", tags = {"便設定個別マスタ"})
    @PostMapping("fmtImport")
    @Log(title = "便設定個別マスタFMT取込", businessType = BusinessType.IMPORT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.ADDITIONAL_DATA, businessType = BusinessType.IMPORT)
    public ResponseResult fmtImport(@Validated UploadBo bo,
                          @NotNull(message = SysConstantInfo.FILE_ERROR) MultipartFile file, HttpServletResponse response) throws Exception {
        if (!FileUtils.checkFileType(file)) {
            throw new SysBusinessException(SysConstantInfo.FILE_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_TYPE_ERROR_CODE);
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(MailConstants.MT_MAIL_ITEM_CSV_NAME)) {
            throw new SysBusinessException(SysConstantInfo.FILE_FMT_NAME_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_FMT_NAME_ERROR_CODE);
        }
        try {
            return ResponseResult.buildOK(mtMailItemService.fmtImport(bo, file, response));
        } catch (SysBusinessException e) {
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        } catch (Exception e) {
            throw new SysBusinessException(SysConstantInfo.FILE_UPLOAD_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        }
    }

    @ApiOperation("便設定個別マスタの新規ログイン")
    @PostMapping("/record")
    @Log(title = "便設定個別マスタの新規ログイン", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.MAIL, businessType = BusinessType.INSERT)
    public ResponseResult<JSONObject> recordData(@RequestBody List<MtMailItemVo> list) {
        return ResponseResult.buildOK(mtMailItemService.recordImport(list));
    }

    @ApiOperation("便設定個別マスタの新規ログインcheck")
    @GetMapping("/recordCheck")
    @Log(title = "便設定個別マスタの新規ログイン", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.MAIL, businessType = BusinessType.INSERT)
    public ResponseResult<Boolean> recordDataCheck(String itemId, Integer storeId, Integer centerId) {
        return ResponseResult.buildOK(mtMailItemService.recordImportCheck(itemId, storeId, centerId));
    }
}
