package com.tre.centralkitchen.service.author;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.utils.md5Utils;
import com.tre.jdevtemplateboot.common.redis.IDataBaseAuthorizationService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author JDev
 */
@Service
@Slf4j
public class DataBaseAuthorizationLogicService implements IDataBaseAuthorizationService {

    @Value("${login.key}")
    private String key;
    @Value("${login.url}")
    private String apiUrl;

    @Override
    public List<String> getDataBaseAuthorization(String s) {
        //Extract the user permission list from the DB according to the ID information
        return new ArrayList<>();
    }

    @Override
    public void checkLoginFromDbUserInfo(String s, String s1) {
        JSONObject json = JSONObject.parseObject(loginValidation(s, s1, ""));
        String successed = json.get("successed").toString();
        if (!"0".equals(successed)) {
            throw new SysBusinessException(SysConstantInfo.SERVER_ERROR_MSG, HttpStatus.HTTP_OK, SysConstantInfo.SERVER_ERROR_CODE);
        }
    }

    private String loginValidation(String userid, String password, String ip) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", userid);
        jsonObject.put("password", password);
        jsonObject.put("systemid", key);
        jsonObject.put("clientid", ip.replace(".", ""));
        Map<String, String> heads = new HashMap<>();
        heads.put("Content-Type", "application/json;charset=UTF-8");
        String result = HttpRequest.post(apiUrl)
                .headerMap(heads, false)
                .body(jsonObject.toJSONString())
                .timeout(30 * 1000).execute().body();
        return result;
    }


}
