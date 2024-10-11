package com.tre.centralkitchen.common.utils;

import com.tre.centralkitchen.domain.vo.system.UserInfoVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorityUtils {


    public static List<String> checkAuthority(String centerId, Integer lineId, UserInfoVo userInfoVo) {
        List<String> list = new ArrayList<>();
        if (Objects.equals(userInfoVo.getSystemAuthority(), "1")) {
            return list;
        }
        if (!Objects.equals(userInfoVo.getOtherCenterAuthority(), "1") && centerId != null && !Objects.equals(centerId, userInfoVo.getCenterId())) {
            list.add("センターコード");
        }
        if (!Objects.equals(userInfoVo.getOtherLineAuthority(), "1") && lineId != null && !Objects.equals(String.valueOf(lineId), userInfoVo.getLineId())) {
            list.add("ライン");
        }
        return list;
    }
}
