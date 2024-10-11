package com.tre.centralkitchen.domain.vo.common;

import lombok.Data;

@Data
public class FileBackErrorVo {
    private boolean otherFlag;
    private boolean priceFlag;
    private boolean dateFlag;
    private String fileError;
}
