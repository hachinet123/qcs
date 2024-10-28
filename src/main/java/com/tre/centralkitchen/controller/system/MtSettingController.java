package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.domain.bo.system.MtSettingBo;
import com.tre.centralkitchen.service.MtSettingService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Validated
@RequestMapping("/settings")
@RequiredArgsConstructor
@Api(value = "Mt設定コントローラー", tags = {"MtST"})
@Slf4j
public class MtSettingController {

    private final MtSettingService service;

    @PostMapping("")
    @ApiOperation("MT設定を挿入")
    public ResponseResult<Void> insert(@RequestBody @Valid MtSettingBo bo) throws IOException, InterruptedException {
        service.insertData(bo);
        return ResponseResult.buildOK();
    }

}
