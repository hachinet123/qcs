package com.tre.centralkitchen.common.aspectj;

import com.tre.centralkitchen.common.annotation.FunAndOpe;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
public class FunAndOpeAspect {
    @Around(value = "@annotation(controllerFunAndOpe)")
    public Object around(ProceedingJoinPoint joinPoint, FunAndOpe controllerFunAndOpe) throws Throwable {
        Object[] arguments = joinPoint.getArgs();
        for (Object arg : arguments) {
            if (arg instanceof BaseEntityBo) {
//                if (controllerFunAndOpe.businessType().equals(BusinessType.INSERT)) {
                ((BaseEntityBo) arg).setInsFuncId(controllerFunAndOpe.funType().getCode());
                ((BaseEntityBo) arg).setInsOpeId(controllerFunAndOpe.opeType().getCode());
//                }
                ((BaseEntityBo) arg).setUpdFuncId(controllerFunAndOpe.funType().getCode());
                ((BaseEntityBo) arg).setUpdOpeId(controllerFunAndOpe.opeType().getCode());
                break;
            }
            if (arg instanceof List) {
                for (Object obj : (List) arg) {
                    if (obj instanceof BaseEntityBo) {
                        ((BaseEntityBo) obj).setInsFuncId(controllerFunAndOpe.funType().getCode());
                        ((BaseEntityBo) obj).setInsOpeId(controllerFunAndOpe.opeType().getCode());
                        ((BaseEntityBo) obj).setUpdFuncId(controllerFunAndOpe.funType().getCode());
                        ((BaseEntityBo) obj).setUpdOpeId(controllerFunAndOpe.opeType().getCode());
                    }
                }
                break;
            }
        }
        return joinPoint.proceed(arguments);
    }
}
