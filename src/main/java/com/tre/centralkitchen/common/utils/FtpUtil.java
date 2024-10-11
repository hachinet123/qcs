package com.tre.centralkitchen.common.utils;

import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;


public class FtpUtil {


    /**
     * Description: uploadFile
     *
     * @param host     FTP hostname
     * @param username FTP username
     * @param password FTP password
     * @param basePath FTP basePath
     * @param filePath FTP filePath
     * @param filename filename
     * @param input    input
     * @return success:true，failed:false
     */
    public static boolean uploadFile(String host, String username, String password, String basePath, String filePath, String filename, InputStream input) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                String[] dirs = filePath.split(File.separator);
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += File.separator + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            System.out.println("create dir " + tempPath + " failed");
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            if (!ftp.storeFile(filename, input)) {
                return result;
            }
            input.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * Description: downloadFile
     *
     * @param host       FTP hostname
     * @param username   FTP username
     * @param password   FTP password
     * @param remotePath FTP remotePath
     * @param fileName   fileName
     * @param localPath  localPath
     * @return
     */
    public static boolean downloadFile(String host, String username, String password, String remotePath, String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + File.separator + ff.getName());

                    try (OutputStream is = new FileOutputStream(localFile)) {
                        ftp.retrieveFile(ff.getName(), is);
                    }
                }
            }
            ftp.logout();
            result = true;
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    /**
     * Description: rename FTP's File
     *
     * @param host     FTP hostname
     * @param username FTP username
     * @param password FTP password
     * @param fromPath fromPath
     * @param filename filename
     * @param newName  newName
     * @return
     */
    public static boolean renameFile(String host, String username, String password, String fromPath, String filename, String newName) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            if (!ftp.changeWorkingDirectory(fromPath)) {
                String[] dirs = fromPath.split(File.separator);
                String tempPath = fromPath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += File.separator + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            System.out.println("create dir " + tempPath + " failed");
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            if (!ftp.rename(filename, newName)) {
                return result;
            }
            ftp.logout();
            result = true;
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static boolean existFile(String host, String username, String password, String remotePath, String fileName) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    result = true;
                    break;
                }
            }
            ftp.logout();
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static boolean existFiles(String host, String username, String password, String remotePath, String[] fileNames) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();

            loop:
            for (FTPFile ff : fs) {
                for (String fileName : fileNames) {
                    if (ff.getName().equals(fileName)) {
                        result = true;
                        break loop;
                    }
                }
            }
            ftp.logout();
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static boolean deleteFile(String host, String username, String password, String remotePath, String fileName) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            if (!ftp.changeWorkingDirectory(remotePath)) {
                String[] dirs = remotePath.split(File.separator);
                String tempPath = remotePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += File.separator + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            System.out.println("create dir " + tempPath + " failed");
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            if (!ftp.deleteFile(fileName)) {
                return result;
            }
            ftp.logout();
            result = true;
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}