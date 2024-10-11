package com.tre.centralkitchen.service.author;

import com.tre.jdevtemplateboot.common.redis.IDataBaseFileAuthorizationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JDev
 */
@Service
public class DataBaseFileAuthoriztionLogicService implements IDataBaseFileAuthorizationService {

    @Override
    public List<String> getDataBaseFileAuthorization(String userId) {
        //Extract the user file permission list from the DB according to the ID information
        return new ArrayList<>();
    }

}
