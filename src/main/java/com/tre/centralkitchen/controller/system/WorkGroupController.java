package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.annotation.UserIdDict;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.WorkGroupBo;
import com.tre.centralkitchen.domain.vo.system.WorkGroupVo;
import com.tre.centralkitchen.service.WorkGroupService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/workGroup")
@Api(value = "作業グループ", tags = {"作業グループ"})
public class WorkGroupController {


    private final WorkGroupService workGroupService;

    @GetMapping()
    @ApiOperation("作業グループの検索")
    @Log(title = "作業グループの検索", businessType = BusinessType.GRANT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.GRANT)
    public ResponseResult<WorkGroupVo> getWorkGroupList(@Valid WorkGroupBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(workGroupService.getWorkGroupList(bo, pageQuery));
    }

    @GetMapping("/info")
    @ApiOperation("作業グループの単一の検索")
    @Log(title = "作業グループの単一の検索", businessType = BusinessType.GRANT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.GRANT)
    @UserIdDict
    public ResponseResult<WorkGroupVo> getUpWorkGroup(@Valid WorkGroupBo bo) {
        return ResponseResult.buildOK(workGroupService.getUpWorkGroup(bo));
    }

    @DeleteMapping()
    @ApiOperation("作業グループの削除")
    @Log(title = "作業グループの削除", businessType = BusinessType.DELETE)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.DELETE)
    public ResponseResult<Void> deleteWorkGroup(@Valid @RequestBody WorkGroupBo bo) {
        workGroupService.deleteWorkGroup(bo);
        return ResponseResult.buildOK();
    }

    @PutMapping()
    @ApiOperation("作業グループの更新")
    @Log(title = "作業グループの更新", businessType = BusinessType.UPDATE)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.UPDATE)
    public ResponseResult<Void> updateWorkGroup(@RequestBody @Valid WorkGroupBo bo) {
        workGroupService.updateWorkGroup(bo);
        return ResponseResult.buildOK();
    }

    @PostMapping("/bigGroup")
    @ApiOperation("作業グループの大分類の保存")
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.INSERT)
    @Log(title = "作業グループの大分類の保存", businessType = BusinessType.INSERT)
    public ResponseResult<WorkGroupVo> insertBigGroup(@RequestBody @Valid WorkGroupBo bo) {
        return ResponseResult.buildOK(workGroupService.insertBigGroup(bo));
    }

    @PostMapping("/smallGroup")
    @ApiOperation("作業グループの小分類の保存")
    @Log(title = "作業グループの小分類の保存", businessType = BusinessType.INSERT)
    @FunAndOpe(funType = FunType.MASTER, opeType = OpeType.WORKING_GROUP, businessType = BusinessType.INSERT)
    public ResponseResult<Void> insertSmallGroup(@RequestBody @Valid WorkGroupBo bo) {
        workGroupService.insertSmallGroup(bo);
        return ResponseResult.buildOK();
    }

    @GetMapping("/csvDownload")
    @ApiOperation("作業グループのcsv出力")
    @Log(title = "作業グループのcsv出力", businessType = BusinessType.GRANT)
    public void downloadWorkGroupList(WorkGroupBo bo, HttpServletResponse response) {
        workGroupService.downloadWorkGroup(bo, response);
    }
}
