package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.domain.bo.system.DailyInventoryBo;
import com.tre.centralkitchen.domain.bo.system.DailyInventorySearchBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.system.DailyInventoryVo;
import com.tre.centralkitchen.service.DailyInventoryService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 在庫_日次棚卸
 * </p>
 *
 * @author 10253955
 * @since 2023-12-22 9:22
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("dailyInventory")
@Api(value = "日次棚卸", tags = {"在庫"})
public class DailyInventoryController {

    private final DailyInventoryService dailyInventoryService;

    @ApiOperation(value = "日次棚卸の検索", tags = {"在庫"})
    @GetMapping("search")
    @Log(title = "ラベルチェックリスト", businessType = BusinessType.OTHER)
    public ResponseResult<DailyInventoryVo> search(@Valid DailyInventorySearchBo bo, PageQuery pageQuery) {
        return ResponseResult.buildOK(dailyInventoryService.search(pageQuery, bo));
    }

    @ApiOperation(value = "日次棚卸の棚卸登録", tags = {"在庫"})
    @PostMapping(value = "update")
    @Log(title = "日次棚卸の棚卸登録", businessType = BusinessType.UPDATE)
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.DAILY_INVENTORY, businessType = BusinessType.UPDATE)
    public ResponseResult update(@Validated @RequestBody List<DailyInventoryBo> dataList) {
        dailyInventoryService.update(dataList);
        return ResponseResult.buildOK();
    }

    @ApiOperation(value = "日次棚卸のPDF出力", tags = {"在庫"})
    @GetMapping(value = "pdfExport")
    @Log(title = "日次棚卸のPDF出力", businessType = BusinessType.EXPORT)
    public void pdfExport(@Validated DailyInventorySearchBo bo, HttpServletResponse response) {
        dailyInventoryService.downloadDailyInventoryPdf(bo, response);
    }

    @ApiOperation(value = "日次棚卸のCSV出力", tags = {"在庫"})
    @GetMapping(value = "csvOutput")
    @Log(title = "日次棚卸のCSV出力", businessType = BusinessType.EXPORT)
    public void downloadCsvOutput(@Validated DailyInventorySearchBo bo, HttpServletResponse response) {
        dailyInventoryService.downloadCsvOutput(bo, response);
    }

    @ApiOperation(value = "日次棚卸のExcel出力", tags = {"在庫"})
    @GetMapping(value = "excelOutput")
    @Log(title = "日次棚卸のExcel出力", businessType = BusinessType.EXPORT)
    public void downloadExcelOutput(@Validated DailyInventorySearchBo bo, HttpServletResponse response) {
        try {
            dailyInventoryService.downloadExcelOutput(bo, response);
        } catch (Exception e) {
            throw new SysBusinessException(SysConstantInfo.FILE_FMT_EXPORT_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_FMT_EXPORT_ERROR_CODE);
        }
    }

    @ApiOperation(value = "日次棚卸のFMT取込", tags = {"在庫"})
    @PostMapping(value = "fmtImport")
    @Log(title = "日次棚卸のFMT取込", businessType = BusinessType.IMPORT)
    @FunAndOpe(funType = FunType.WAREHOUSEITEM, opeType = OpeType.DAILY_INVENTORY, businessType = BusinessType.IMPORT)
    public ResponseResult fmtImport(@Validated UploadBo bo,
                          @NotNull(message = SysConstantInfo.FILE_ERROR) MultipartFile file,
                          HttpServletResponse response) {
        try {
            return ResponseResult.buildOK(dailyInventoryService.fmtImport(bo, file, response));
        } catch (SysBusinessException e) {
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        } catch (Exception e) {
            throw new SysBusinessException(SysConstantInfo.FILE_UPLOAD_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_UPLOAD_ERROR_CODE);
        }
    }
}
