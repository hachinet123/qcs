package com.tre.centralkitchen.service;

import com.alibaba.fastjson.JSONObject;
import com.tre.centralkitchen.domain.bo.system.MailBasicSettingBo;
import com.tre.centralkitchen.domain.vo.common.BranchListVo;

import java.util.List;

public interface IMailBasicSettingService {

    List<JSONObject> getBasicSettingList(MailBasicSettingBo bo);

    void deleteStore(MailBasicSettingBo bo);

    void insertStore(MailBasicSettingBo bo);

    void updateSqlserverOrderList(MailBasicSettingBo bo);

    void updateSqlserverOrderQty(MailBasicSettingBo bo);

    List<BranchListVo> queryCenterName();


}