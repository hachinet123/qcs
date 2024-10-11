package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tre.centralkitchen.domain.po.MtMailcontrol;
import com.tre.centralkitchen.domain.po.MtMailcontrolHistory;
import com.tre.centralkitchen.mapper.MtMailcontrolHistoryMapper;
import com.tre.centralkitchen.mapper.MtMailcontrolMapper;
import com.tre.centralkitchen.service.IMtMailcontrolHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 便コントロール履歴 服务实现类
 * </p>
 *
 * @author JDev
 * @since 2023-02-28
 */
@RequiredArgsConstructor
@Service
public class MtMailcontrolHistoryServiceImpl extends ServiceImpl<MtMailcontrolHistoryMapper, MtMailcontrolHistory> implements IMtMailcontrolHistoryService {
    private final MtMailcontrolMapper mtMailcontrolMapper;
    private final MtMailcontrolHistoryMapper mtMailcontrolHistoryMapper;

    @Override
    public void backup(Integer centerId, Integer mailNo) {
        QueryWrapper<MtMailcontrol> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("center_id", centerId);
        queryWrapper.eq("mail_no", mailNo);
        MtMailcontrol mtMailcontrol = mtMailcontrolMapper.selectOne(queryWrapper);
        MtMailcontrolHistory history = new MtMailcontrolHistory();
        BeanUtil.copyProperties(mtMailcontrol, history);
        history.setUpdateTypeId(3);
        mtMailcontrolHistoryMapper.insert(history);
    }
}
