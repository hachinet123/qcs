package com.tre.centralkitchen.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void toZip(List<File> srcFiles, File zipFile) throws IOException {
        if (zipFile == null) {
            return;
        }
        if (!zipFile.getName().endsWith(".zip")) {
            return;
        }
        try (FileOutputStream out = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(out)) {
            for (File srcFile : srcFiles) {
                byte[] buf = new byte[1024];
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int len;
                try (FileInputStream in = new FileInputStream(srcFile)) {
                    while ((len = in.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                        zos.flush();
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}