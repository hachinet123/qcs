package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.domain.po.Lines;
import com.tre.centralkitchen.domain.vo.system.DepartmentsVo;
import com.tre.centralkitchen.service.DepartmentsService;
import com.tre.centralkitchen.service.LinesService;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Validated
@RequestMapping("/departments")
@RequiredArgsConstructor
@Api(value = "部門コントローラー", tags = {"部門コントローラー"})
@Slf4j
public class DepartmentsController {

    private final DepartmentsService service;

    @GetMapping("")
    @ApiOperation("選択リスト部門")
    public ResponseResult<List<DepartmentsVo>> selectList(@RequestParam("line_id") Integer lineId) throws IOException, InterruptedException {
        return ResponseResult.buildOK(service.selectList(lineId));
    }

}
