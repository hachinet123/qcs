package com.tre.centralkitchen.service.validate;

import com.tre.jdevtemplateboot.common.jwt.IDataBaseRequestLimiterValidationService;
import com.tre.jdevtemplateboot.domain.po.RequestLimiter;
import org.springframework.stereotype.Service;

/**
 * @author JDev
 */
@Service
public class DataBaseLimiterValidateService implements IDataBaseRequestLimiterValidationService {

    @Override
    public RequestLimiter oneRequestLimiter(int limiterType, String limiterKey) {
        return new RequestLimiter();
    }

    @Override
    public void updateRequestLimiter(RequestLimiter requestLimiter) {
        // Do update request times update
    }
}
