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
 * @create: 2021-08-05 14:10
 **/
public class MsSQLCodeGenerator {
    private static final Logger logger = LoggerFactory.getLogger(MsSQLCodeGenerator.class);

    private MsSQLCodeGenerator() {

    }

    /**
     * SQLSERVER
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try {
            GeneratorAutoCode.generator(
                    "D://AutoGenerator//sqlserver"
                    , DbType.SQL_SERVER
                    , "com.microsoft.sqlserver.jdbc.SQLServerDriver"
                    , "jdbc:sqlserver://xx.xx.xx.xx:1433;DatabaseName=test"
                    , ""
                    , "");

        } catch (EncryptionOperationNotPossibleException ex) {
            logger.error(ex.getMessage());
        }
    }
}
