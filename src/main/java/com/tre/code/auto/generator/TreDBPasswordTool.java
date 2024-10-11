package com.tre.code.auto.generator;

import com.tre.jdevtemplateboot.common.util.JasyptUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @program: jdev-boot-core
 * @description
 * @author: JDev
 * @create: 2021-08-05 14:06
 **/
public class TreDBPasswordTool {

    private static final Logger logger = LoggerFactory.getLogger(TreDBPasswordTool.class);

    private TreDBPasswordTool() {
    }

    public static void main(String[] args) {
        logger.info("Please enter the characters to encrypt：");
        try(Scanner scanner = new Scanner(System.in, String.valueOf(StandardCharsets.UTF_8));) {
            logger.info("encrypted characters：" + JasyptUtils.encryptPwd(scanner.next()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
