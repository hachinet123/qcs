package com.tre.centralkitchen.common.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import com.tre.centralkitchen.common.config.CkWebBackendConfig;
import com.tre.centralkitchen.common.constant.SysConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtils {

    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        if (StringUtil.isBlank(ip)) {
            return address;
        }
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip);
        if (NetUtil.isInnerIP(ip)) {
            return "Intranet IP";
        }
        if (CkWebBackendConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtil.createGet(IP_URL)
                        .body("ip=" + ip + "&json=true", SysConstants.GBK)
                        .execute()
                        .body();
                if (StringUtil.isEmpty(rspStr)) {
                    log.error("Get geographic location exception {}", ip);
                    return UNKNOWN;
                }
                Dict obj = JsonUtils.parseMap(rspStr);
                String region = obj.getStr("pro");
                String city = obj.getStr("city");
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.error("Get geographic location exception {}", ip);
            }
        }
        return UNKNOWN;
    }

}
