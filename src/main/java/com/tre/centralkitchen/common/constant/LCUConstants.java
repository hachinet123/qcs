package com.tre.centralkitchen.common.constant;

public interface LCUConstants {
    String GET_ACTUAL_CMD = "CS,  ,1";
    String GET_ACTUAL_CMD_FRONT = "CS,";
    String GET_ACTUAL_CMD_END = ",1\r\n";
    String HST_SUFFIX = ".HST";
    String TXT_SUFFIX = ".TXT";
    String CSV_SUFFIX = ".CSV";
    String CSV_HEADER = "column01,column02,column03,column04,column05,column06,column07,column08,column09,column10,column11,column12,column13,column14,column15,column16,column17,column18,column19,column20,column21,column22,column23,column24,column25,column26,column27,column28,column29,column30,column31,column32,column33,column34,column35,column36,column37,column38,column39,column40,column41,column42,column43,column44,column45,column46,column47\r\n";

    Integer FILE_LOOP_TIME = 180;
    Integer THREAD_SLEEP_TIME = 1000;

    Integer LOCK_LOOP_TIME = 1200;

    Integer LOCK_SLEEP_TIME = 3000;
}
