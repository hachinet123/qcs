package com.tre.centralkitchen.controller.system;


import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.domain.vo.system.MtMenuVo;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.service.IMtMenuService;
import com.tre.centralkitchen.service.IUserInfoService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "メニューコントローラ", tags = {"メニュー"})
@RestController
@RequestMapping("/mtMenu")
public class MtMenuController {

    @Autowired
    private IMtMenuService iMtMenuService;
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private TokenTakeApart tokenTakeApart;

    @ApiOperation("メニューの取得")
    @GetMapping()
    @Log(title = "メニューの取得", businessType = BusinessType.GRANT)
    public ResponseResult<List<MtMenuVo>> getMenuList() {
        String userId = tokenTakeApart.takeDecryptedUserId();
        UserInfoVo userInfoVo = iUserInfoService.getUserInfo(userId);
        return ResponseResult.buildOK(iMtMenuService.getMenuList(userInfoVo));
    }
}
