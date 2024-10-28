package com.tre.centralkitchen.controller.system;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.tre.centralkitchen.common.annotation.Log;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.enums.BusinessType;
import com.tre.centralkitchen.common.enums.UserType;
import com.tre.centralkitchen.domain.bo.system.LoginBo;
import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import com.tre.centralkitchen.domain.vo.system.MtSettingVo;
import com.tre.centralkitchen.domain.vo.system.MtSysparamVo;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.service.IMtSysparamService;
import com.tre.centralkitchen.service.IUserInfoService;
import com.tre.centralkitchen.service.MtSettingService;
import com.tre.centralkitchen.service.UserMasterService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.common.dto.PropertiesConfig;
import com.tre.jdevtemplateboot.common.dto.ResponseResult;
import com.tre.jdevtemplateboot.common.enums.AuthenticateTypeEnums;
import com.tre.jdevtemplateboot.common.jwt.IJwtTokenManagerService;
import com.tre.jdevtemplateboot.common.redis.IDataBaseAuthorizationService;
import com.tre.jdevtemplateboot.common.util.BasedDesUtils;
import com.tre.jdevtemplateboot.exception.business.SysInvalidUserException;
import com.tre.jdevtemplateboot.exception.business.SysRequestTokenCategoryException;
import com.tre.jdevtemplateboot.exception.business.SysUnAuthenticatedException;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Api(value = "ログインコントローラ", tags = {"ユーザー"})
@RestController
@RequestMapping()
public class LoginCKController {
    @Autowired
    private IJwtTokenManagerService jwtTokenManagerService;
    @Autowired
    private PropertiesConfig propertiesConfig;
    @Autowired
    private IUserInfoService iUserInfoService;
    @Autowired
    private IMtSysparamService iMtSysparamService;
    @Autowired
    private IDataBaseAuthorizationService dataBaseAuthorizationService;
    @Autowired
    private TokenTakeApart tokenTakeApart;
    @Autowired
    private UserMasterService userMasterService;
    @Autowired
    private MtSettingService mtSettingService;

//    @ApiOperation("ログイン")
//    @PostMapping({"/login"})
//    @Log(title = "ログイン", businessType = BusinessType.INSERT)
//    public ResponseResult<Object> loginJwt(@RequestParam("userId") String userId, @RequestParam("password") String passWord, @RequestParam("type") String type, HttpServletResponse response) {
//        UserInfoVo userInfoVo = iUserInfoService.getUserInfo(userId);
//        if (Objects.isNull(userInfoVo) || UserType.GUEST.getType().equals(type)) {
//            throw new SysBusinessException(SysConstantInfo.USER_TYPE_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.USER_TYPE_ERROR_CODE);
//        }
//        if (!AuthenticateTypeEnums.JWT.toString().equalsIgnoreCase(this.propertiesConfig.getTokenCategory())) {
//            throw new SysRequestTokenCategoryException();
//        } else if (StringUtils.hasLength(userId) && StringUtils.hasLength(passWord)) {
//            MtSysparamVo mtSysparamVo = iMtSysparamService.getParam(1, 99);
//            if (!Objects.isNull(mtSysparamVo) && !passWord.equals(mtSysparamVo.getParamVal1())) {
//                this.dataBaseAuthorizationService.checkLoginFromDbUserInfo(userId, passWord);
//            }
//            MtSysparamVo pageVo = iMtSysparamService.getParam(1, 98);
//            MtSysparamVo MaxLengthVo = iMtSysparamService.getParam(1, 97);
//            String encoderUserId = BasedDesUtils.encryptBasedDes(userId);
//            String resultToken = this.jwtTokenManagerService.generateToken(encoderUserId);
//            response.setHeader(this.propertiesConfig.getPreUserIssuer() + "-access-token", resultToken);
//            JSONObject object = new JSONObject();
//            object.put("page", pageVo);
//            object.put("maxLength", MaxLengthVo);
//            object.put("token", resultToken);
//            return ResponseResult.buildOK(object);
//        } else {
//            throw new SysInvalidUserException();
//        }
//    }

    @ApiOperation("ログインv2")
    @PostMapping({"/login"})
    public ResponseResult<MtEmployeesVo> login(@RequestBody LoginBo loginBo) {
        MtEmployeesVo employeesVo = userMasterService.getUserName(loginBo.getCode());
        if (employeesVo == null) {
            throw new SysUnAuthenticatedException("社員が存在しません");
        }
        MtSettingVo mtSettingVo = mtSettingService.findOne(employeesVo.getEmployeeCode());
        employeesVo.setIsSetting(0);
        if(mtSettingVo != null) {
            employeesVo.setIsSetting(1);
        }
        System.out.println(mtSettingVo);
        return ResponseResult.buildOK(employeesVo);
    }

    @ApiOperation("ユーザー情報の取得")
    @GetMapping({"/userInfo"})
    @Log(title = "ユーザー情報の取得", businessType = BusinessType.GRANT)
    public ResponseResult<UserInfoVo> userInfo() {
        String userId = tokenTakeApart.takeDecryptedUserId();
        UserInfoVo userInfoVo = iUserInfoService.getUserInfo(userId);
        return ResponseResult.buildOK(userInfoVo);
    }
}
