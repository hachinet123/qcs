package com.tre.centralkitchen.controller.common;

import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.bo.common.CommonCenterLineBo;
import com.tre.centralkitchen.domain.bo.common.CommonMailNoBo;
import com.tre.centralkitchen.domain.po.MtHome;
import com.tre.centralkitchen.domain.po.MtLanguage;
import com.tre.centralkitchen.domain.po.Products;
import com.tre.centralkitchen.domain.vo.common.*;
import com.tre.centralkitchen.domain.vo.system.CenterdlvstoreMasterVo;
import com.tre.centralkitchen.domain.vo.system.MtCenterdlvstoreVo;
import com.tre.centralkitchen.domain.vo.system.MtSectionVo;
import com.tre.centralkitchen.domain.vo.system.MtSysparamVo;
import com.tre.centralkitchen.service.IMtSysparamService;
import com.tre.centralkitchen.service.MtCenterdlvstoreService;
import com.tre.centralkitchen.service.MtLanguageService;
import com.tre.centralkitchen.service.MtSectionService;
import com.tre.centralkitchen.service.commom.MasterService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/master")
@RequiredArgsConstructor
@Api(value = "共通ンコントローラ", tags = {"MST"})
@Slf4j
public class MasterController {

    private final MasterService masterService;
    private final IMtSysparamService iMtSysparamService;
    private final MtCenterdlvstoreService mtCenterdlvstoreService;
    private final MtSectionService mtSectionService;
    private final MtLanguageService mtLanguageService;

    @ApiOperation("産地名称の取得")
    @GetMapping("/placeList")
    public ResponseResult<List<MtPlaceVo>> getPlaceList() {
        return ResponseResult.buildOK(masterService.getPlaceList());
    }

    @ApiOperation("プロセスセンターIDの取得")
    @GetMapping("/centerList")
    public ResponseResult<List<CenterListVo>> getCenterList() {
        return ResponseResult.buildOK(masterService.getCenterList());
    }

    @ApiOperation("ラインの取得")
    @GetMapping("/lineAllList")
    public ResponseResult<List<LineListVo>> getLineAllList() {
        return ResponseResult.buildOK(masterService.getLineAllList());
    }

    @ApiOperation("大分類の取得")
    @GetMapping("/bigGroupList")
    public ResponseResult<List<MtProductwkgrpVo>> getBigGroupList(@Valid CommonCenterLineBo bo) {
        return ResponseResult.buildOK(masterService.getBigGroupList(bo));
    }

    @ApiOperation("便の取得")
    @GetMapping("/mailList")
    public ResponseResult<List<MailListVo>> getMailList(@Valid CommonMailNoBo bo) {
        return ResponseResult.buildOK(masterService.getMailList(bo));
    }

    @ApiOperation("作業グループの取得")
    @GetMapping("/workGroupList")
    public ResponseResult<List<WorkGroupListVo>> getWorkGroupList(
            @ApiParam(value = "センター", required = true)
            @NotBlank(message = SysConstantInfo.CENTER_NOT_EMPTY) String centerId,
            @ApiParam(value = "ライン", required = true)
            @NotBlank(message = SysConstantInfo.LINE_NOT_EMPTY) String lineId) {
        return ResponseResult.buildOK(masterService.getWorkGroupList(centerId, lineId));
    }

    @ApiOperation("店舗の取得")
    @GetMapping("/branchList")
    public ResponseResult<List<BranchListVo>> getBranchList() {
        return ResponseResult.buildOK(masterService.getBranchList());
    }

    @ApiOperation("JANの名前")
    @GetMapping("/itemName")
    public ResponseResult<Products> getItemName(@NotBlank(message = SysConstantInfo.JAN_NOT_EMPTY)
                                                String itemId) {
        return ResponseResult.buildOK(masterService.getItemName(itemId));
    }

    @ApiOperation("JANの名前")
    @GetMapping("/itemNameByStoreCallCode")
    public ResponseResult<String> getItemNameByStoreCallCode(@NotBlank(message = SysConstantInfo.STORE_INPUT_NOT_EMPTY) String storeIds,
                                                             @NotBlank(message = SysConstantInfo.CALL_CODE_EMPTY) String callCode) {
        return ResponseResult.buildOK(masterService.getItemNameByStoreCallCode(storeIds, callCode));
    }

    @ApiOperation("JANの名前")
    @GetMapping("/itemNameByCenterCallCode")
    public ResponseResult<String> getItemNameByCenterCallCode(@NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY) Integer centerId,
                                                             @NotNull(message = SysConstantInfo.CALL_CODE_EMPTY) Integer callCode) {
        return ResponseResult.buildOK(masterService.getItemNameByCenterCallCode(centerId, callCode));
    }

    @ApiOperation("JANの名前")
    @GetMapping("/itemNameByStore")
    public ResponseResult<String> getItemNameByStore(@NotBlank(message = SysConstantInfo.STORE_INPUT_NOT_EMPTY) String storeIds,
                                                     @NotBlank(message = SysConstantInfo.CALL_CODE_EMPTY) String itemId) {
        return ResponseResult.buildOK(masterService.getItemNameByStore(storeIds, itemId));
    }

    @ApiOperation("店舗名の取得")
    @GetMapping("/storeName")
    public ResponseResult<List<BranchListVo>> getStoreName(@NotNull(message = SysConstantInfo.STORE_INPUT_NOT_EMPTY) Integer storeId, Integer centerId) {
        return ResponseResult.buildOK(masterService.getStoreName(centerId, storeId));
    }

    @ApiOperation(value = "ホームデータの取得", tags = {"MST"})
    @GetMapping("/homeData")
    public ResponseResult<List<MtHome>> getHomeData() {
        return ResponseResult.buildOK(masterService.getHomeData());
    }

    @ApiOperation("センター管理店舗リスト")
    @GetMapping("/queryMailBasicStoreList")
    @Log(title = "センター管理店舗リスト", businessType = BusinessType.GRANT)
    public ResponseResult<List<BranchListVo>> queryMailBasicStoreList(Integer centerId) {
        return ResponseResult.buildOK(masterService.queryMailBasicStoreList(centerId));
    }

    @ApiOperation("倉庫の取得")
    @GetMapping("/getWarehouse")
    public ResponseResult<List<WarehouseVo>> getWarehouse(Integer centerId) {
        return ResponseResult.buildOK(masterService.getWarehouse(centerId));
    }

    @ApiOperation("菌検査の保存温度")
    @GetMapping("/getTemperature")
    public ResponseResult<List<BacteriacheckListVo>> getTemperature() {
        return ResponseResult.buildOK(masterService.getTemperature());
    }

    @ApiOperation("菌検査の検査目的")
    @GetMapping("/getCheckObj")
    public ResponseResult<List<BacteriacheckListVo>> getCheckObj() {
        return ResponseResult.buildOK(masterService.getCheckObj());
    }

    @ApiOperation("菌検査の検査時刻")
    @GetMapping("/getCheckTime")
    public ResponseResult<List<BacteriacheckListVo>> getCheckTime() {
        return ResponseResult.buildOK(masterService.getCheckTime());
    }

    @ApiOperation("システムパラメータ")
    @GetMapping("/getSystemParam")
    public ResponseResult<MtSysparamVo> getSystemParam(Integer systemId, Integer paramId) {
        return ResponseResult.buildOK(iMtSysparamService.getParam(systemId, paramId));
    }

    @ApiOperation("マネージャーコントローラー")
    @GetMapping(value = "/languages")
    public ResponseResult<List<MtLanguage>> getLanguages() throws IOException {
        return ResponseResult.buildOK(mtLanguageService.selectList());
    }

    @ApiOperation("中心リストを取得する")
    @GetMapping(value = "/centralkitchenes")
    public ResponseResult<List<CenterdlvstoreMasterVo>> getAllCenter() throws IOException {
        return ResponseResult.buildOK(mtCenterdlvstoreService.centerSelect());
    }

    @ApiOperation("マネージャーコントローラー")
    @GetMapping(value = "/managers")
    public ResponseResult<List<MtSectionVo>> getManagers(@RequestParam("central_id") Integer centerId) throws IOException {
        return ResponseResult.buildOK(mtSectionService.managersSelect(centerId));
    }

}