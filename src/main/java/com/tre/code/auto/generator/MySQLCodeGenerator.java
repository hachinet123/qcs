package com.tre.code.auto.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import generate.generatecode.GeneratorAutoCode;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: jdev-boot-core
 * @description
 * @author: JDev
 * @create: 2021-08-05 14:12
 **/
public class MySQLCodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MySQLCodeGenerator.class);

    private MySQLCodeGenerator() {

    }

    /**
     * MySQL
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {
            GeneratorAutoCode.generator(
                    "D://AutoGenerator//mysql"
                    , DbType.MYSQL
                    , "com.mysql.jdbc.Driver",
                    "jdbc:mysql://xx.xx.xx.xx:3306/jdevtemplate?characterEncoding=utf8"
                    , "root"
                    , "");
        } catch (EncryptionOperationNotPossibleException ex) {
            logger.error(ex.getMessage());
        }
    }
}
