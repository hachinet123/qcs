package com.tre.centralkitchen.controller.system;

import com.alibaba.fastjson.JSONObject;
import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.MailBasicSettingBo;
import com.tre.centralkitchen.domain.vo.common.BranchListVo;
import com.tre.centralkitchen.service.IMailBasicSettingService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mailBasicSetting")
@RequiredArgsConstructor
@Api(value = "便基本設定", tags = {"便基本設定"})
public class MailBasicSettingController {

    private final IMailBasicSettingService iMailBasicSettingService;

    /**
     * 基本設定
     */
    @ApiOperation("便基本設定の検索")
    @GetMapping()
    @Log(title = "便基本設定の検索", businessType = BusinessType.GRANT)
    public ResponseResult<JSONObject> getBasicSettingList(MailBasicSettingBo bo) {
        return ResponseResult.buildOK(iMailBasicSettingService.getBasicSettingList(bo));
    }

    @ApiOperation("便基本設定の削除")
    @DeleteMapping()
    @Log(title = "便基本設定の削除", businessType = BusinessType.DELETE)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.MAIL)
    public ResponseResult<Void> deleteStore(@RequestBody @Valid MailBasicSettingBo bo) {
        iMailBasicSettingService.deleteStore(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation("便基本設定の挿入")
    @PostMapping()
    @Log(title = "便基本設定の挿入", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.MAIL, businessType = BusinessType.INSERT)
    public ResponseResult<Void> insertStore(@Valid @RequestBody MailBasicSettingBo bo) {
        iMailBasicSettingService.updateSqlserverOrderList(bo);
        iMailBasicSettingService.updateSqlserverOrderQty(bo);
        iMailBasicSettingService.insertStore(bo);
        return ResponseResult.buildOK();
    }

    @ApiOperation("検索プロセスセンターID")
    @GetMapping("/queryCenterName")
    @Log(title = "検索プロセスセンターID", businessType = BusinessType.GRANT)
    public ResponseResult<List<BranchListVo>> queryCenterName() {
        return ResponseResult.buildOK(iMailBasicSettingService.queryCenterName());
    }
}
