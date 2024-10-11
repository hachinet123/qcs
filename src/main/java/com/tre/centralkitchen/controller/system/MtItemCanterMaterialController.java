package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.annotation.UserIdDict;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.utils.FileUtils;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialBo;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialSearchBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.bo.system.WareHouseItemBo;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterSearchVo;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterialVo;
import com.tre.centralkitchen.service.MtItemCanterMaterialService;
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
import java.util.Objects;

@RestController
@RequestMapping("/MtItemCanterMaterial")
@RequiredArgsConstructor
@Api(tags = "原材料別安全在庫管理")
public class MtItemCanterMaterialController {

    private final MtItemCanterMaterialService service;

    @ApiOperation("安全在庫管理の検索")
    @GetMapping("")
    public ResponseResult<TableDataInfo<ItemCenterMaterialVo>> search(@Valid MtItemCenterMaterialSearchBo bo, PageQuery pageQuery){
        return ResponseResult.buildOK(service.search(bo,pageQuery));
    }

    @ApiOperation("安全在庫管理のcsv出力")
    @GetMapping("/downloadCSV")
    public ResponseResult  downloadCSV(@Valid MtItemCenterMaterialSearchBo bo, HttpServletResponse response){
        service.downloadCSV(bo,response);
        return ResponseResult.buildOK();
    }

    @ApiOperation("安全在庫管理の登録")
    @PostMapping()
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.ITEM_CENTER_MATERIAL_REQUEST, businessType = BusinessType.INSERT)
    public ResponseResult save(@Valid @RequestBody MtItemCenterMaterialBo bo){
        service.save(bo);
        return ResponseResult.buildOK();
    }

    @GetMapping("/itemName")
    @ApiOperation("安全在庫管理の単一商品名")
    public ResponseResult selectItemName(Integer centerId, String itemId){
       String itemName =  service.selectItemName(centerId,itemId);
        return ResponseResult.buildOK(itemName);
    }


    @GetMapping("/info")
    @ApiOperation("単一の検索")
    @UserIdDict
    public ResponseResult  info(@Valid MtItemCenterMaterialBo bo) {
        ItemCenterMaterSearchVo vo = service.info(bo);
        return ResponseResult.buildOK(vo);
    }

    @PutMapping()
    @ApiOperation("編集")
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.ITEM_CENTER_MATERIAL_REQUEST, businessType = BusinessType.UPDATE)
    public ResponseResult update(@Valid @RequestBody MtItemCenterMaterialBo bo){
        service.update(bo);
        return ResponseResult.buildOK();
    }

    @DeleteMapping()
    @ApiOperation("削除")
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.ITEM_CENTER_MATERIAL_REQUEST, businessType = BusinessType.DELETE)
    public ResponseResult delete(@Valid @RequestBody MtItemCenterMaterialBo bo) {
        service.delete(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation("安全在庫管理のFMT取込")
    @PostMapping("/fmtImport")
    @Log(title = "安全在庫管理のFMT取込", businessType = BusinessType.IMPORT)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.ADDITIONAL_DATA, businessType = BusinessType.IMPORT)
    public ResponseResult fmtImport(@Validated @NotNull(message = SysConstantInfo.FILE_ERROR) MultipartFile file,
                         HttpServletResponse response) throws Exception {
        if (!FileUtils.checkFileType(file)) {
            throw new SysBusinessException(SysConstantInfo.FILE_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_TYPE_ERROR_CODE);
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(SysConstants.ITEM_CENTER_MATERIAL_FILE_NAME)) {
            throw new SysBusinessException(SysConstantInfo.FILE_FMT_NAME_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_FMT_NAME_ERROR_CODE);
        }
        try {
            return ResponseResult.buildOK(service.fmtImport( file, response));
        } catch (SysBusinessException e) {
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        } catch (Exception e) {
            throw new SysBusinessException(SysConstantInfo.FILE_UPLOAD_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        }
    }
}
