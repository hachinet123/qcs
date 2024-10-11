package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.ActualProductionConfirmBo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultConfirmBo;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionBo;
import com.tre.centralkitchen.domain.bo.system.ProducePlanBo;
import com.tre.centralkitchen.service.*;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Validated
@Api(value = "Hinemos用API", tags = {"Hinemos用API"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/jobApi")
public class ApiController {
    private final GetActualProductionService service;
    private final ActualProductionConfirmService actualProductionConfirmService;
    private final FixAmountResultConfirmService fixAmountResultConfirmService;
    private final IMtMailcontrolHistoryService mtMailcontrolHistoryService;
    private final IOrderStatusService orderStatusService;
    private final IMtMailAutoConfirmService mtMailAutoConfirmService;
    @Value("${authentication.key}")
    private String authenticationKey;

    @ApiOperation("API_要求ファイル作成と転送")
    @GetMapping("/fileSend")
    @Log(title = "API_要求ファイル作成と転送", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult fileSend(@Valid GetActualProductionBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) throws Exception {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        try {
            if (mtMailAutoConfirmService.hinemosLock(bo.getCenterId(), bo.getMailNo(), true)) {
                service.fileSend(bo);
            } else {
                throw new SysBusinessException(SysConstantInfo.TIME_OUT_TYPE_ERROR_MSG, HttpStatus.HTTP_CLIENT_TIMEOUT, SysConstantInfo.TIME_OUT_TYPE_ERROR_CODE);
            }
        } catch (Exception ex) {
            mtMailAutoConfirmService.hinemosUnlock(bo.getCenterId());
            throw ex;
        }
        return ResponseResult.buildOK();
    }

    @ApiOperation("API_ファイル取得")
    @GetMapping("/fileRecv")
    @Log(title = "API_ファイル取得", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult fileRecv(@Valid GetActualProductionBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) throws Exception {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        try {
            service.fileRecv(bo);
        } catch (Exception ex) {
            mtMailAutoConfirmService.hinemosUnlock(bo.getCenterId());
            throw ex;
        }
        return ResponseResult.buildOK();
    }

    @ApiOperation("API_ファイルバックアップ")
    @GetMapping("/fileBackup")
    @Log(title = "API_ファイルバックアップ", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult fileBackup(@Valid GetActualProductionBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) throws Exception {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        try {
            service.fileBackup(bo);
        } catch (Exception ex) {
            mtMailAutoConfirmService.hinemosUnlock(bo.getCenterId());
            throw ex;
        }
        return ResponseResult.buildOK();
    }

    @ApiOperation("API_取り込みと反応")
    @GetMapping("/fileRead")
    @Log(title = "API_取り込みと反応", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult fileRead(@Valid GetActualProductionBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) throws Exception {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        try {
            service.fileRead(bo, "0");
        } catch (Exception ex) {
            mtMailAutoConfirmService.hinemosUnlock(bo.getCenterId());
            throw ex;
        }
        return ResponseResult.buildOK();
    }

    @ApiOperation("API_便コントロールバックアップ")
    @GetMapping("/mailBackup")
    @Log(title = "API_便コントロールバックアップ", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult mailBackup(@Valid GetActualProductionBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        mtMailcontrolHistoryService.backup(bo.getCenterId(), bo.getMailNo());
        return ResponseResult.buildOK();
    }

    @ApiOperation("API_生産実績確定")
    @GetMapping("/actualProductionConfirm")
    @Log(title = "API_生産実績確定", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult actualProductionConfirm(@Valid ActualProductionConfirmBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        try {
            List<ActualProductionConfirmBo> bos = new ArrayList<>();
            bos.add(bo);
            actualProductionConfirmService.actualProductionConfirm(bos, "0");
        } catch (Exception exAll) {
            mtMailAutoConfirmService.hinemosUnlock(bo.getCenterId());
            throw exAll;
        }
        return ResponseResult.buildOK();
    }

    @ApiOperation("API_定額実績確定")
    @GetMapping("/fixAmountResultConfirm")
    @Log(title = "API_定額実績確定", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult fixAmountResultConfirm(@Valid FixAmountResultConfirmBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        try {
            List<FixAmountResultConfirmBo> bos = new ArrayList<>();
            bos.add(bo);
            fixAmountResultConfirmService.fixAmountResultConfirm(bos, "0");
        } catch (Exception exAll) {
            mtMailAutoConfirmService.hinemosUnlock(bo.getCenterId());
            throw exAll;
        }
        mtMailAutoConfirmService.hinemosUnlock(bo.getCenterId());
        return ResponseResult.buildOK();
    }

    @ApiOperation("API_受注状況確認")
    @GetMapping("/orderStatusList")
    @Log(title = "API_受注状況確認", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.FOREIGN, opeType = OpeType.FOREIGN, businessType = BusinessType.INSERT)
    public ResponseResult getOrderStatusList(@Valid ProducePlanBo bo, @ApiParam(value = "key", required = true) @NotBlank(message = "keyは必須です。") String key) {
        if (!authenticationKey.equals(key)) {
            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
        }
        orderStatusService.insertProducePlanWk(bo);
        return ResponseResult.buildOK();
    }
}
