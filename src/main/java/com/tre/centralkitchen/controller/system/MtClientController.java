package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.domain.po.MtClient;
import com.tre.centralkitchen.mapper.MtClientMapper;
import com.tre.centralkitchen.service.MtClientService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/mtClient")
@RequiredArgsConstructor
@Api(value = "マウントクライアント", tags = {"マウントクライアント"})
@Slf4j
public class MtClientController {

    private final MtClientService service;

    @GetMapping("")
    @ApiOperation("MT設定を挿入")
    public ResponseResult<List<MtClient>> selectList() throws IOException, InterruptedException {
        return ResponseResult.buildOK(service.selectList());
    }

}
