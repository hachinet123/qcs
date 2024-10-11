package com.tre.centralkitchen.service.commom;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    void downloadFmt(String fmtId, HttpServletResponse response);

    void downloadFile(String fileName, HttpServletResponse response);
}
