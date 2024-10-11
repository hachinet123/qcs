package com.tre.centralkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.domain.po.MtCenterstatus;
import com.tre.centralkitchen.domain.po.MtMenu;
import com.tre.centralkitchen.domain.vo.system.MtMenuVo;
import com.tre.centralkitchen.domain.vo.system.UserInfoVo;
import com.tre.centralkitchen.mapper.MtCenterstatusMapper;
import com.tre.centralkitchen.mapper.MtMenuMapper;
import com.tre.centralkitchen.service.IMtMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MtMenuServiceImpl implements IMtMenuService {
    @Resource
    private MtMenuMapper mtMenuMapper;

    @Value("${menu.masterAuthority}")
    private String masterAuthority;

    @Value("${menu.userAuthority}")
    private String userAuthority;

    private final MtCenterstatusMapper mtCenterstatusMapper;

    private static final List<String> MENU_VALUE_LIST = Arrays.asList("/achievePage", "/confirmedPage", "/fixedDeterminationPage");

    @Override
    public List<MtMenuVo> getMenuList(UserInfoVo userInfoVo) {
        QueryWrapper<MtMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("f_del", 0);
        queryWrapper.orderByAsc("pid");
        queryWrapper.orderByAsc("seq");
        List<MtMenuVo> mtMenuVos = mtMenuMapper.selectVoList(queryWrapper);

        QueryWrapper<MtCenterstatus> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("center_id", Integer.valueOf(userInfoVo.getCenterId()));
        MtCenterstatus mtCenterstatusPo = mtCenterstatusMapper.selectOne(queryWrapper2);

        List<MtMenuVo> mtMenuVosNew = new ArrayList<>();
        mtMenuVos.forEach(a -> {
            if (a.getPid() == Integer.parseInt(masterAuthority) || a.getId() == Integer.parseInt(masterAuthority)) {
                if (userInfoVo.getMasterAuthority().equals("1") || userInfoVo.getSystemAuthority().equals("1")) {
                    menuControllerByCenter(mtCenterstatusPo, mtMenuVosNew, a);
                }
            } else if (a.getPid() == Integer.parseInt(userAuthority) || a.getId() == Integer.parseInt(userAuthority)) {
                if (userInfoVo.getUserAuthority().equals("1") || userInfoVo.getSystemAuthority().equals("1")) {
                    menuControllerByCenter(mtCenterstatusPo, mtMenuVosNew, a);
                }
            } else {
                menuControllerByCenter(mtCenterstatusPo, mtMenuVosNew, a);
            }
        });
        return mtMenuVosNew;
    }

    private static void menuControllerByCenter(MtCenterstatus mtCenterstatusPo, List<MtMenuVo> mtMenuVosNew, MtMenuVo a) {
        if (MENU_VALUE_LIST.contains(a.getRouterPath())) {
            if (mtCenterstatusPo.getFAutoconfirm() == 0) {
                mtMenuVosNew.add(a);
            }
        } else {
            mtMenuVosNew.add(a);
        }
    }
}
