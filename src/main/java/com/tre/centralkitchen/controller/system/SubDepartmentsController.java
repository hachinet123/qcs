package com.tre.centralkitchen.controller.system;

import com.tre.centralkitchen.domain.vo.system.DepartmentsVo;
import com.tre.centralkitchen.domain.vo.system.SubDepartmentsVo;
import com.tre.centralkitchen.service.DepartmentsService;
import com.tre.centralkitchen.service.SubDepartmentsService;
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
@RequestMapping("/subDepartments")
@RequiredArgsConstructor
@Api(value = "サブ部門コントローラー", tags = {"サブ部門コントローラー"})
@Slf4j
public class SubDepartmentsController {

    private final SubDepartmentsService service;

    @GetMapping("")
    @ApiOperation("サブ部門のリストを取得する")
    public ResponseResult<List<SubDepartmentsVo>> selectList(@RequestParam("department_id") Integer departmentId) throws IOException, InterruptedException {
        return ResponseResult.buildOK(service.selectList(departmentId));
    }

}
