package com.tre.centralkitchen.service;


import com.itextpdf.text.DocumentException;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaResultBo;
import com.tre.centralkitchen.domain.vo.system.BacteriaCheckResultVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;

public interface TrBacteriaCheckResultService {

    BacteriaCheckResultVo search(Integer id);

    void save(TrBacteriaResultBo bo);

    void sendMail(TrBacteriaResultBo bo);

    void bacteriaCheckItemResultPdf(Integer id, HttpServletResponse response) throws   Exception;

    void saveCheck(TrBacteriaResultBo bo);
}
