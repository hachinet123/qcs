package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.bo.system.DocumentLinkSearchBo;
import com.tre.centralkitchen.domain.po.MtDocumentLink;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface MtDocumentLinkService {

    List<MtDocumentLink> getDocumentLinkList();

    void downloadCSV(DocumentLinkSearchBo bo, HttpServletResponse response);
}
