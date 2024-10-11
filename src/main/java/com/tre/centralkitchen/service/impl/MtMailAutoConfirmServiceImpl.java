package com.tre.centralkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tre.centralkitchen.domain.po.MtMailAutoConfirm;
import com.tre.centralkitchen.mapper.MtMailAutoConfirmMapper;
import com.tre.centralkitchen.service.IMtMailAutoConfirmService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import static com.tre.centralkitchen.common.constant.LCUConstants.LOCK_LOOP_TIME;
import static com.tre.centralkitchen.common.constant.LCUConstants.LOCK_SLEEP_TIME;

/**
 * <p>
 * システム自動確定 服务实现类
 * </p>
 *
 * @author JDev
 * @since 2023-04-18
 */
@RequiredArgsConstructor
@Service
public class MtMailAutoConfirmServiceImpl implements IMtMailAutoConfirmService {
    private final MtMailAutoConfirmMapper mapper;

    @Override
    public boolean hinemosLock(Integer centerId, Integer mailNo, Boolean flag) throws InterruptedException {
        MtMailAutoConfirm mtMailAutoConfirm = mapper.selectOne(new QueryWrapper<MtMailAutoConfirm>().eq("center_id", centerId));
        if (mtMailAutoConfirm != null) {
            int cnt = 0;
            while (flag && !hinemosLock(centerId, mailNo, false)) {
                Thread.sleep(LOCK_SLEEP_TIME);
                cnt++;
                if (cnt > LOCK_LOOP_TIME) {
                    return false;
                }
            }
            return flag;
        } else {
            try {
                mtMailAutoConfirm = new MtMailAutoConfirm();
                mtMailAutoConfirm.setCenterId(centerId);
                mtMailAutoConfirm.setMailNo(mailNo);
                mapper.insert(mtMailAutoConfirm);
                return true;
            } catch (DuplicateKeyException ex) {
                return hinemosLock(centerId, mailNo, true);
            }
        }
    }

    @Override
    public boolean hinemosUnlock(Integer centerId) {
        return mapper.delete(new QueryWrapper<MtMailAutoConfirm>().eq("center_id", centerId)) > 0;
    }
}
