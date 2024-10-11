package com.tre.centralkitchen.controller.system;


import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.annotation.UserIdDict;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.ExaminationBo;
import com.tre.centralkitchen.domain.bo.system.ExaminationSearchBo;
import com.tre.centralkitchen.domain.vo.system.MtItemCenterProductVo;
import com.tre.centralkitchen.service.ExaminationService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 商品(加工品)センター別属性マスタ
 * </p>
 *
 * @author JDev
 * @since 2023-12-12
 */
@Api(value = "検食不要一覧コントローラ", tags = {"検食不要一覧"})
@RestController
@RequiredArgsConstructor
@RequestMapping("examination")
@Validated
public class ExaminationController {

    private final ExaminationService service;

    @ApiOperation(value = "検食不要一覧の検索", tags = {"検食不要一覧"})
    @GetMapping
    @Log(title = "検食不要一覧の検索", businessType = BusinessType.GRANT)
    public ResponseResult<TableDataInfo<MtItemCenterProductVo>> search(@Validated ExaminationSearchBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(service.queryPage(pageQuery, bo));
    }

    @ApiOperation(value = "検食不要一覧のcsv出力", tags = {"検食不要一覧"})
    @GetMapping("downloadCSV")
    @Log(title = "検食不要一覧のcsv出力", businessType = BusinessType.EXPORT)
    public void downloadCSV(@Validated ExaminationSearchBo bo, HttpServletResponse response) {
        service.downloadCSV(bo, response);
    }

    @ApiOperation(value = "検食不要一覧の登録", tags = {"検食不要一覧"})
    @PutMapping
    @Log(title = "検食不要一覧の登録", businessType = BusinessType.UPDATE)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.EXAMINATION, businessType = BusinessType.UPDATE)
    public ResponseResult update(@Validated @RequestBody ExaminationBo bo) {
        service.update(bo);
        return ResponseResult.buildOK();
    }

    @DeleteMapping
    @ApiOperation(value = "検食不要一覧の削除", tags = {"検食不要一覧"})
    @Log(title = "検食不要一覧の削除", businessType = BusinessType.DELETE)
    @FunAndOpe(funType = FunType.REGISTERED, opeType = OpeType.EXAMINATION, businessType = BusinessType.DELETE)
    public ResponseResult<Void> deleteUserMaster(@RequestBody ExaminationBo bo) {
        service.delete(bo);
        return ResponseResult.buildOK();
    }
}
