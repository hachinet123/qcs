package com.tre.centralkitchen.common.handler;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tre.centralkitchen.common.domain.BaseEntity;
import com.tre.centralkitchen.common.domain.HistoryBaseEntity;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    private TokenTakeApart tokenTakeApart;

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                baseEntity.setFDel(0);
                DateTime now = DateUtil.date();
                baseEntity.setInsDate(ObjectUtil.isNotNull(baseEntity.getInsDate())
                        ? baseEntity.getInsDate() : now);
                baseEntity.setInsTime(ObjectUtil.isNotNull(baseEntity.getInsTime())
                        ? baseEntity.getInsTime() : now);
                baseEntity.setUpdDate(ObjectUtil.isNotNull(baseEntity.getUpdDate())
                        ? baseEntity.getUpdDate() : now);
                baseEntity.setUpdTime(ObjectUtil.isNotNull(baseEntity.getUpdTime())
                        ? baseEntity.getUpdTime() : now);
                baseEntity.setInsUserId(baseEntity.getInsUserId() != null
                        ? baseEntity.getInsUserId() : getUserId());
                baseEntity.setUpdUserId(baseEntity.getUpdUserId() != null
                        ? baseEntity.getUpdUserId() : getUserId());
                if (baseEntity.getInsFuncId() == null || baseEntity.getInsOpeId() == null) {
                    baseEntity.setInsFuncId(FunType.FOREIGN.getCode());
                    baseEntity.setInsOpeId(OpeType.FOREIGN.getCode());
                }
                if (baseEntity.getUpdFuncId() == null || baseEntity.getUpdOpeId() == null) {
                    baseEntity.setUpdFuncId(FunType.FOREIGN.getCode());
                    baseEntity.setUpdOpeId(OpeType.FOREIGN.getCode());
                }
            }
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof HistoryBaseEntity) {
                HistoryBaseEntity historyBaseEntity = (HistoryBaseEntity) metaObject.getOriginalObject();
                DateTime now = DateUtil.date();
                historyBaseEntity.setBackupDate(ObjectUtil.isNotNull(historyBaseEntity.getBackupDate())
                        ? historyBaseEntity.getBackupDate() : now);
                historyBaseEntity.setInsDate(ObjectUtil.isNotNull(historyBaseEntity.getInsDate())
                        ? historyBaseEntity.getInsDate() : now);
                historyBaseEntity.setInsTime(ObjectUtil.isNotNull(historyBaseEntity.getInsTime())
                        ? historyBaseEntity.getInsTime() : now);
                historyBaseEntity.setUpdDate(ObjectUtil.isNotNull(historyBaseEntity.getUpdDate())
                        ? historyBaseEntity.getUpdDate() : now);
                historyBaseEntity.setUpdTime(ObjectUtil.isNotNull(historyBaseEntity.getUpdTime())
                        ? historyBaseEntity.getUpdTime() : now);
                historyBaseEntity.setInsUserId(historyBaseEntity.getInsUserId() != null
                        ? historyBaseEntity.getInsUserId() : getUserId());
                historyBaseEntity.setUpdUserId(historyBaseEntity.getUpdUserId() != null
                        ? historyBaseEntity.getUpdUserId() : getUserId());
                if (historyBaseEntity.getInsFuncId() == null || historyBaseEntity.getInsOpeId() == null) {
                    historyBaseEntity.setInsFuncId(FunType.FOREIGN.getCode());
                    historyBaseEntity.setInsOpeId(OpeType.FOREIGN.getCode());
                }
                if (historyBaseEntity.getUpdFuncId() == null || historyBaseEntity.getUpdOpeId() == null) {
                    historyBaseEntity.setUpdFuncId(FunType.FOREIGN.getCode());
                    historyBaseEntity.setUpdOpeId(OpeType.FOREIGN.getCode());
                }
            }
        } catch (Exception e) {
            throw new SysBusinessException("自動注入異常 => " + e.getMessage(), HttpStatus.HTTP_OK, HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                // 更新時間の充填（空かどうか）
                DateTime now = DateUtil.date();
                baseEntity.setUpdDate(now);
                baseEntity.setUpdTime(now);
                Long userId = getUserId();
                baseEntity.setUpdUserId(userId);
                if (baseEntity.getUpdFuncId() == null || baseEntity.getUpdOpeId() == null) {
                    baseEntity.setUpdFuncId(FunType.FOREIGN.getCode());
                    baseEntity.setUpdOpeId(OpeType.FOREIGN.getCode());
                }
            }
        } catch (Exception e) {
            throw new SysBusinessException("自動注入異常 => " + e.getMessage(), HttpStatus.HTTP_OK, HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    private Long getUserId() {
        try {
            return Long.parseLong(tokenTakeApart.takeDecryptedUserId());
        } catch (Exception e) {
            return 0L;
        }
    }

}
