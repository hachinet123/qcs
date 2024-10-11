package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.annotation.UserIdDict;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.UserMasterBo;
import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.domain.vo.system.UserMasterUpSearchVo;
import com.tre.centralkitchen.domain.vo.system.UserMasterVo;
import com.tre.centralkitchen.service.UserMasterService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/userMaster")
@RequiredArgsConstructor
@Api(value = "ユーザー", tags = {"ユーザー"})
public class UserMasterController {


    private final UserMasterService userMasterService;


    @GetMapping()
    @ApiOperation("ユーザーの検索")
    @Log(title = "ユーザーの検索", businessType = BusinessType.GRANT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.GRANT)
    public ResponseResult<UserMasterVo> getUserMasterList(@Valid UserMasterBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(userMasterService.getUserMasterList(bo, pageQuery));
    }

    @GetMapping("/info")
    @ApiOperation("ユーザーの単一の検索")
    @Log(title = "ユーザーの単一の検索", businessType = BusinessType.GRANT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.GRANT)
    @UserIdDict
    public ResponseResult<UserMasterUpSearchVo> getUserMasterUpdateSearch(@Valid UserMasterBo bo) {
        return ResponseResult.buildOK(userMasterService.getUserMasterUpSearch(bo));
    }

    @DeleteMapping()
    @ApiOperation("ユーザーの削除")
    @Log(title = "ユーザーの削除", businessType = BusinessType.DELETE)
    @FunAndOpe(funType = FunType.USER, opeType = OpeType.USER, businessType = BusinessType.DELETE)
    public ResponseResult<Void> deleteUserMaster(@RequestBody UserMasterBo bo) {
        userMasterService.deleteUserMaster(bo);
        return ResponseResult.buildOK();
    }

    @PostMapping()
    @ApiOperation("ユーザーのデータの挿入")
    @Log(title = "ユーザーのデータの挿入", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.USER, opeType = OpeType.USER, businessType = BusinessType.INSERT)
    public ResponseResult<Void> insertUserMaster(@Valid @RequestBody UserMasterBo bo) {
        userMasterService.insertUserMaster(bo);
        return ResponseResult.buildOK();
    }

    @PutMapping()
    @ApiOperation("ユーザーのデータの更新")
    @Log(title = "ユーザーのデータの更新", businessType = BusinessType.UPDATE)
    @FunAndOpe(funType = FunType.USER, opeType = OpeType.USER, businessType = BusinessType.UPDATE)
    public ResponseResult<Void> updateUserMaster(@Valid @RequestBody UserMasterBo bo) {
        userMasterService.updateUserMaster(bo);
        return ResponseResult.buildOK();
    }

    @GetMapping("/csvDownload")
    @ApiOperation("ユーザーのcsv出力")
    @Log(title = "ユーザーのcsv出力", businessType = BusinessType.GRANT)
    public void downloadUserMaster(UserMasterBo bo, HttpServletResponse response) {
        userMasterService.downloadUserMaster(bo, response);
    }

    @GetMapping("/userName")
    @ApiOperation("従業員コードの検索")
    @Log(title = "従業員コードの検索", businessType = BusinessType.GRANT)
    public ResponseResult<MtEmployeesVo> userName(@ApiParam(value = "従業員コード") @Valid
                                                      @NotBlank(message = "社員番号を選択してください")
                                                      @Length(max = 8, message = "社員番号は8桁以内です。")
                                                  String userId) {
        return ResponseResult.buildOK(userMasterService.userName(userId));
    }
}
