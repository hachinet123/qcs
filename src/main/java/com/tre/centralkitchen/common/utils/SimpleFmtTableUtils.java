package com.tre.centralkitchen.common.utils;

import cn.hutool.core.text.StrFormatter;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.domain.FmtFileInfo;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 10225441
 */
@Component
public class SimpleFmtTableUtils {
    /**
     * character encoding (default utf-8).
     */
    private static final String CHARACTER_ENCODING = "UTF-8";
    /**
     * content disposition key.
     */
    private static final String CONTENT_DISPOSITION = "Content-disposition";
    /**
     * content disposition value prefix.
     */
    private static final String CONTENT_DISPOSITION_PREFIX = "attachment;filename=";
    /**
     * yml parent id
     */
    private static final String YML_PARENT_ID = "fmtMappings";
    private static final String FIELD_FMT_ID = "fmtId";
    private static final String FIELD_FILE_NAME = "fileName";
    private static final String FIELD_HTTP_SERVLET_RESPONSE = "HttpServletResponse";
    private static final String FIELD_CONTENT_TYPE = "ContentType";

    @Value("${ck-system-business.fmt-file-path}")
    private String fmtPath;
    @Value("${ck-system-business.tmp-file-path:\"/tmp\"}")
    private String tmpPath;

    public void downloadFmt(HttpServletResponse response, String fmtId) throws SysBusinessException, IOException {
        if (response == null) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_NULL_MSG_WITH_PLACEHOLDER, FIELD_HTTP_SERVLET_RESPONSE));
        }
        if (fmtId == null) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_NULL_MSG_WITH_PLACEHOLDER, FIELD_FMT_ID));
        }
        if (fmtId.matches(FormatConstants.REGEX_PATTERN_ALL_WORD_IN_ENGLISH)) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_ERROR_MSG_WITH_PLACEHOLDER, FIELD_FMT_ID, fmtId));
        }
        JSONObject fmtMappings = new Yaml().loadAs(new ClassPathResource(SysConstants.FMT_CONFIG_YAML).getInputStream(), JSONObject.class);
        if (fmtMappings.getJSONObject(YML_PARENT_ID).containsKey(fmtId)) {
            FmtFileInfo info = fmtMappings.getJSONObject(YML_PARENT_ID).getObject(fmtId, FmtFileInfo.class);
            response.setContentType(info.getContentType());
            response.setCharacterEncoding(CHARACTER_ENCODING);
            response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_PREFIX + getTempFileName(info.getFileName()));
            try (InputStream in = getFmtFileInputStream(fmtId); OutputStream out = response.getOutputStream()) {
                byte[] bytes = new byte[1024];
                int len;
                while ((len = in.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
            }
        } else {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.FMT_NOT_EXIST_MSG_WITH_PLACEHOLDER, FIELD_FMT_ID, fmtId));
        }
    }

    public void downloadTempFile(HttpServletResponse response, String fileName, String contentType) throws SysBusinessException, IOException {
        if (response == null) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_NULL_MSG_WITH_PLACEHOLDER, FIELD_HTTP_SERVLET_RESPONSE));
        }
        if (contentType == null) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_NULL_MSG_WITH_PLACEHOLDER, FIELD_CONTENT_TYPE));
        }
        if (fileName == null) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_NULL_MSG_WITH_PLACEHOLDER, FIELD_FILE_NAME));
        }
        response.setContentType(contentType);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_PREFIX + getTempFileName(fileName));
        Path path = getTempFilePath(fileName);
        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path); OutputStream out = response.getOutputStream()) {
                byte[] bytes = new byte[1024];
                int len;
                while ((len = in.read(bytes)) > 0) {
                    out.write(bytes, 0, len);
                }
            }
        }
    }
    @SneakyThrows(value = IOException.class)
    public Path getTempFilePath(String fileName) throws SysBusinessException {
        if (fileName == null) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_NULL_MSG_WITH_PLACEHOLDER, FIELD_FILE_NAME));
        }
        String osName = System.getProperty(SysConstants.PROP_OS_NAME).toLowerCase();
        if (Files.notExists(Paths.get(tmpPath))) {
            if (osName.contains(SysConstants.TYPE_OPERATING_SYSTEM_WINDOWS)) {
                Files.createDirectories(Paths.get(SysConstants.PATH_DRIVER_D, tmpPath));
            } else {
                Files.createDirectories(Paths.get(tmpPath));
            }
        }
        return Paths.get(tmpPath, fileName);
    }

    /**
     * <p>Get fmt file's name.</p>
     *
     * @param fileName Fmt file's name
     * @return Encode fmt file's name
     */
    public String getTempFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace(FormatConstants.REGEX_PATTERN_SLASH, StringConstants.SLASH_FORWARD_ENCODE);
    }

    public InputStream getFmtFileInputStream(String fmtId) {
        if (fmtId == null) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_NULL_MSG_WITH_PLACEHOLDER, FIELD_FMT_ID));
        }
        if (fmtId.matches(FormatConstants.REGEX_PATTERN_ALL_WORD_IN_ENGLISH)) {
            throw new SysBusinessException(StrFormatter.format(SysConstantInfo.PARAM_ERROR_MSG_WITH_PLACEHOLDER, FIELD_FMT_ID, fmtId));
        }
        try {
            JSONObject fmtMappings = new Yaml().loadAs(new ClassPathResource(SysConstants.FMT_CONFIG_YAML).getInputStream(), JSONObject.class);
            if (fmtMappings.getJSONObject(YML_PARENT_ID).containsKey(fmtId)) {
                FmtFileInfo info = fmtMappings.getJSONObject(YML_PARENT_ID).getObject(fmtId, FmtFileInfo.class);
                if (fmtPath.isBlank()) {
                    return new ClassPathResource(SysConstants.FMT_PATH_PREFIX + info.getFileName()).getInputStream();
                } else {
                    return Files.newInputStream(Paths.get(fmtPath, info.getFileName()));
                }
            } else {
                throw new SysBusinessException(StrFormatter.format(SysConstantInfo.FMT_NOT_EXIST_MSG_WITH_PLACEHOLDER, FIELD_FMT_ID, fmtId));
            }
        } catch (Exception e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    public List<String> pdfSplitter(String fileName) throws IOException {
        List<String> list = new ArrayList<>();
        try (PdfReader pdfReader = new PdfReader(getTempFilePath(fileName).toFile()); PdfDocument pdf = new PdfDocument(pdfReader)) {
            int pageNum = pdf.getNumberOfPages();
            List<File> files = new ArrayList<>();
            fileName = fileName.replace(".pdf", "");
            list.add(fileName + ".zip");
            for (int i = 1; i <= (int) Math.ceil(pageNum * 1.0 / SysConstants.PDF_NUMBER); i++) {
                String name = fileName + "_" + i + ".pdf";
                list.add(name);
                try (PdfDocument outPdfDocument = new PdfDocument(new PdfWriter(getTempFilePath(name).toFile()))) {
                    pdf.copyPagesTo((i * SysConstants.PDF_NUMBER) - (SysConstants.PDF_NUMBER - 1),
                            i == (int) Math.ceil(pageNum * 1.0 / SysConstants.PDF_NUMBER) ? pageNum : i * SysConstants.PDF_NUMBER, outPdfDocument);
                } catch (Exception ex) {
                    throw ex;
                }
                files.add(getTempFilePath(name).toFile());
            }
            ZipUtils.toZip(files, getTempFilePath(fileName + ".zip").toFile());
        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }
}
