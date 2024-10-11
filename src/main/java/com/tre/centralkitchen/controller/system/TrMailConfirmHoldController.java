package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.TrMailConfirmHoldBo;
import com.tre.centralkitchen.domain.vo.system.TrMailConfirmHoldVo;
import com.tre.centralkitchen.service.TrMailConfirmHoldService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@Api(value = "自動確定保留", tags = {"自動確定保留"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/extensionPlan")
public class TrMailConfirmHoldController {
    private final TrMailConfirmHoldService trMailConfirmHoldService;

    @ApiOperation("自動確定保留の検索")
    @GetMapping()
    @Log(title = "自動確定保留の検索", businessType = BusinessType.GRANT)
    public ResponseResult<TrMailConfirmHoldVo> queryList(@Valid TrMailConfirmHoldBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(trMailConfirmHoldService.queryList(bo, pageQuery));
    }

    @ApiOperation("自動確定保留の更新")
    @PutMapping()
    @Log(title = "自動確定保留の更新", businessType = BusinessType.UPDATE)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.EXTENSION_PRODUCTION, businessType = BusinessType.INSERT)
    public ResponseResult<Void> update(@RequestBody TrMailConfirmHoldBo bo) {
        trMailConfirmHoldService.update(bo);
        return ResponseResult.buildOK();
    }
}
