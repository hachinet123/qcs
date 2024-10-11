package com.tre.centralkitchen.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tre.centralkitchen.service.AutoPrintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoPrintServiceImpl implements AutoPrintService {
    @Value("${print.print-url}")
    private String printUrl;

    @Value("${print.print-file-url}")
    private String printFileUrl;

    @Override
    public boolean startPrintPdf(Integer centerId, List<String> fileNames) {
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("ID", centerId);
        mapParam.put("ProdNum", 1);
        mapParam.put("Systemid", 108);
        mapParam.put("storecd", centerId);
        mapParam.put("DataList", "");
        mapParam.put("Data", getPdfParam(centerId, fileNames));
        mapParam.put("Usercd", "0");
        String body = HttpUtil.createPost(printUrl)
                .contentType("application/x-www-form-urlencoded")
                .form(mapParam)
                .execute().body();
        JSONObject jsonObj = JSONObject.parseObject(body);
        log.info(jsonObj.toString());
        if (!jsonObj.get("Code").equals("000")) {
            return false;
        }
        JSONArray jsonArray = JSON.parseArray(jsonObj.get("Table0").toString());
        if (!JSONObject.parseObject(jsonArray.get(0).toString()).get("printresultid").equals("000")) {
            return false;
        }
        return true;
    }

    public String getPdfParam(Integer centerId, List<String> fileNames) {
        Map<String, Object> mapParent = new LinkedHashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        mapParent.put("Code", "000");
        mapParent.put("Message", "successful");

        for (String fileName : fileNames) {

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("varietycd", 500);
            map.put("branchcd", centerId);
            map.put("branchnm", "");
            map.put("productcd", "");
            map.put("productname", "");
            map.put("specname", "");
            map.put("parameter1", printFileUrl);
            map.put("parameter2", "DefiniteNumberSystem");
            map.put("parameter3", fileName);
            map.put("parameter4", "");
            map.put("parameter5", "CentralKitchen");
            map.put("parameter6", "");
            if (fileName.contains("zip")) {
                map.put("parameter7", "1");
                map.put("parameter8", "zip");
            } else {
                map.put("parameter7", "0");
                map.put("parameter8", "pdf");
            }
            map.put("parameter9", 2);
            map.put("parameter10", "");
            map.put("parameter11", "");
            map.put("parameter12", "");
            map.put("parameter13", "");
            map.put("parameter14", "");
            map.put("parameter15", "");
            map.put("parameter16", "");
            map.put("parameter17", "");
            map.put("parameter18", "");
            map.put("parameter19", "");
            map.put("parameter20", "");
            map.put("parameter21", "");
            map.put("parameter22", "");
            map.put("parameter23", "");
            map.put("parameter24", "");
            map.put("parameter25", "");
            map.put("parameter26", "");
            map.put("parameter27", "");
            map.put("parameter28", "");
            map.put("parameter29", "");
            map.put("parameter30", "");
            map.put("parameter31", "");
            map.put("parameter32", "");
            map.put("parameter33", "");
            map.put("parameter34", "");
            map.put("parameter35", "");
            map.put("parameter36", "");
            map.put("parameter37", "");
            map.put("parameter38", "");
            map.put("parameter39", "");
            map.put("parameter40", "");
            map.put("parameter41", "");
            map.put("parameter42", "");
            map.put("parameter43", "");
            map.put("parameter44", "");
            map.put("parameter45", "");
            list.add(map);
        }
        mapParent.put("Table0", list);
        return JSON.toJSONString(mapParent);
    }
}
