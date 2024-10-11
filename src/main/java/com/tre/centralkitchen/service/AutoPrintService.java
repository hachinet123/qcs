package com.tre.centralkitchen.service;

import java.util.List;

public interface AutoPrintService {
    boolean startPrintPdf(Integer centerId, List<String> fileNames);

}
