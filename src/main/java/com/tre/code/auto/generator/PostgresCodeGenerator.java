package com.tre.code.auto.generator;


import generate.generatecode.GeneratorPostgresqlAutoCode;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: jdev-boot-core
 * @description
 * @author: JDev
 * @create: 2021-08-05 14:45
 **/
public class PostgresCodeGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PostgresCodeGenerator.class);
    private PostgresCodeGenerator(){

    }

    /**
     *  Postgres SQL
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        /**
         * Generate the specified table entity in the database
         */
        String[] byTables = {"wk_recievelcu_produced_esc"};
        try {
            GeneratorPostgresqlAutoCode.postgresql(
                    "D://AutoGenerator//PostgreSql"
                    , "jdbc:postgresql://172.21.17.170:25432/centralkitchen"
                    , "escape"
                    , "manager"
                    , "Asl17LQxIbWmJVUlC5fluA=="
                    , byTables
                    , true);
        } catch (EncryptionOperationNotPossibleException ex) {
            logger.error(ex.getMessage());
        }
    }

}
