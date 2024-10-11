package com.tre.centralkitchen.common.utils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrFormatter;
import com.alibaba.excel.EasyExcelFactory;
import com.tre.centralkitchen.common.constant.FileTypeConstants;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.domain.vo.system.FixAmountResultModifyVo;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ファイル処理ツール
 *
 * @author JDev
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils extends FileUtil {

    /**
     * 記録されたファイル名をダウンロード
     *
     * @param response     応答オブジェクト
     * @param realFileName 実際のファイル名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * パーセント エンコーディング ツール メソッド
     *
     * @param s パーセントでエンコードする必要がある文字列
     * @return パーセントでエンコードされた文字列
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    public static boolean checkFileType(MultipartFile multipartFile) {
        try {
            String tmpFileName = multipartFile.getOriginalFilename();
            if (tmpFileName != null && tmpFileName.contains(FileTypeConstants.XLSX_POINT)) {
                String tmpFileNameSuffix = tmpFileName.substring(tmpFileName.length() - FileTypeConstants.XLSX_POINT.length());
                if (FileTypeConstants.XLSX_POINT.equals(tmpFileNameSuffix)) {
                    String tmpType = FileTypeUtil.getType(multipartFile.getInputStream());
                    return FileTypeConstants.XLSX.equalsIgnoreCase(tmpType) || FileTypeConstants.ZIP.equalsIgnoreCase(tmpType) || FileTypeConstants.JAR.equalsIgnoreCase(tmpType);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            throw new SysBusinessException(e.getMessage());
        }
    }
}
