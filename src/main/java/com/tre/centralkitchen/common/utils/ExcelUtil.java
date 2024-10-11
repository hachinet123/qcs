package com.tre.centralkitchen.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.tre.centralkitchen.common.convert.ExcelBigNumberConvert;
import com.tre.centralkitchen.common.excel.CellMergeStrategy;
import com.tre.centralkitchen.common.excel.DefaultExcelListener;
import com.tre.centralkitchen.common.excel.ExcelListener;
import com.tre.centralkitchen.common.excel.ExcelResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Excel関連処理
 *
 * @author JDev
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExcelUtil {

    public static final String MESSAGE = "Excel 例外のエクスポート";

    /**
     * 同期インポート (少量のデータに適しています)
     *
     * @param is 入力ストリーム
     * @return 変換されたコレクション
     */
    public static <T> List<T> importExcel(InputStream is, Class<T> clazz) {
        return EasyExcel.read(is).head(clazz).autoCloseStream(false).sheet().doReadSync();
    }


    /**
     * 検証リスナーを使用する 非同期インポート 同期リターン
     *
     * @param is         入力ストリーム
     * @param clazz      オブジェクトタイプ
     * @param isValidate Validator インスペクションかどうか、デフォルトは yes です
     * @return 変換されたコレクション
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, boolean isValidate) {
        DefaultExcelListener<T> listener = new DefaultExcelListener<>(isValidate);
        EasyExcel.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * カスタム リスナーを使用する 非同期インポート カスタム リターン
     *
     * @param is       入力ストリーム
     * @param clazz    オブジェクトタイプ
     * @param listener カスタムリスナー
     * @return 変換されたコレクション
     */
    public static <T> ExcelResult<T> importExcel(InputStream is, Class<T> clazz, ExcelListener<T> listener) {
        EasyExcel.read(is, clazz, listener).sheet().doRead();
        return listener.getExcelResult();
    }

    /**
     * エクスポート エクセル
     *
     * @param list      データ セットのエクスポート
     * @param sheetName ワークシート名
     * @param clazz     エンティティ クラス
     * @param response  レスポンスボディ
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, HttpServletResponse response) {
        exportExcel(list, sheetName, clazz, false, response);
    }

    /**
     * エクスポート エクセル
     *
     * @param list      データ セットのエクスポート
     * @param sheetName ワークシート名
     * @param clazz     エンティティ クラス
     * @param merge     セルを結合するかどうか
     * @param response  レスポンスボディ
     */
    public static <T> void exportExcel(List<T> list, String sheetName, Class<T> clazz, boolean merge, HttpServletResponse response) {
        try {
            resetResponse(sheetName, response);
            ServletOutputStream os = response.getOutputStream();
            ExcelWriterSheetBuilder builder = EasyExcel.write(os, clazz)
                    .autoCloseStream(false)
                    //  自動適応
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    // 歪みを防ぐための大きな値の自動変換
                    .registerConverter(new ExcelBigNumberConvert())
                    .sheet(sheetName);
            if (merge) {
                // マージ プロセッサ
                builder.registerWriteHandler(new CellMergeStrategy(list, true));
            }
            builder.doWrite(list);
        } catch (IOException e) {
            throw new RuntimeException(MESSAGE);
        }
    }

    /**
     * 単一テーブルの複数データ テンプレートのエクスポート テンプレートの形式は {.property} です。
     *
     * @param filename     ファイル名
     * @param templatePath テンプレート パス リソース ディレクトリの下のパスには、テンプレート ファイル名が含まれます。
     *                     例: excel/temp.xlsx
     *                     重要: テンプレート ファイルは、スタートアップ クラスに対応するリソース ディレクトリに配置する必要があります。
     * @param data         テンプレートに必要なデータ
     */
    public static void exportTemplate(List<Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ClassPathResource templateResource = new ClassPathResource(templatePath);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                    .withTemplate(templateResource.getStream())
                    .autoCloseStream(false)
                    // 歪みを防ぐための大きな値の自動変換
                    .registerConverter(new ExcelBigNumberConvert())
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            if (CollUtil.isEmpty(data)) {
                throw new IllegalArgumentException("データが空です");
            }
            // 単一テーブルの複数データ エクスポート テンプレートの形式は {.property} です。
            for (Object d : data) {
                excelWriter.fill(d, writeSheet);
            }
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException(MESSAGE);
        }
    }

    /**
     * マルチテーブル マルチデータ テンプレート エクスポート テンプレートの形式は {key.property} です。
     *
     * @param filename     ファイル名
     * @param templatePath テンプレート パス リソース ディレクトリの下のパスには、テンプレート ファイル名が含まれます。
     *                     例: excel/temp.xlsx
     *                     重要: テンプレート ファイルは、スタートアップ クラスに対応するリソース ディレクトリに配置する必要があります。
     * @param data         テンプレートに必要なデータ
     */
    public static void exportTemplateMultiList(Map<String, Object> data, String filename, String templatePath, HttpServletResponse response) {
        try {
            resetResponse(filename, response);
            ClassPathResource templateResource = new ClassPathResource(templatePath);
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                    .withTemplate(templateResource.getStream())
                    .autoCloseStream(false)
                    // 歪みを防ぐための大きな値の自動変換
                    .registerConverter(new ExcelBigNumberConvert())
                    .build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            if (CollUtil.isEmpty(data)) {
                throw new IllegalArgumentException("データが空です");
            }
            for (Map.Entry<String, Object> map : data.entrySet()) {
                // 設定リストの後にデータがあります
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
                if (map.getValue() instanceof Collection) {
                    // 複数テーブルのエクスポートでは、FillWrapper を使用する必要があります
                    excelWriter.fill(new FillWrapper(map.getKey(), (Collection<?>) map.getValue()), fillConfig, writeSheet);
                } else {
                    excelWriter.fill(map.getValue(), writeSheet);
                }
            }
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException(MESSAGE);
        }
    }

    /**
     * レスポンスボディのリセット
     */
    private static void resetResponse(String sheetName, HttpServletResponse response) throws UnsupportedEncodingException {
        String filename = encodingFilename(sheetName);
        FileUtils.setAttachmentResponseHeader(response, filename);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
    }

    /**
     * 派生値の解析 0=男性、1=女性、2=不明
     *
     * @param propertyValue パラメータ値
     * @param converterExp  翻訳メモ
     * @param separator     デリミタ
     * @return 解析値
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtil.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[0].equals(value)) {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[0].equals(propertyValue)) {
                    return itemArray[1];
                }
            }
        }
        return StringUtil.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 逆解析値 男性=0、女性=1、不明=2
     *
     * @param propertyValue パラメータ値
     * @param converterExp  翻訳メモ
     * @param separator     デリミタ
     * @return 解析値
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource) {
            String[] itemArray = item.split("=");
            if (StringUtil.containsAny(propertyValue, separator)) {
                for (String value : propertyValue.split(separator)) {
                    if (itemArray[1].equals(value)) {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            } else {
                if (itemArray[1].equals(propertyValue)) {
                    return itemArray[0];
                }
            }
        }
        return StringUtil.stripEnd(propertyString.toString(), separator);
    }

    /**
     * エンコードされたファイル名
     */
    public static String encodingFilename(String filename) {
        return IdUtil.fastSimpleUUID() + "_" + filename + ".xlsx";
    }

}
