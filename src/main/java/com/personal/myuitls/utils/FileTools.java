package com.personal.myuitls.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 作者：maohongyu on 2016/11/30.
 * 邮箱：mao_hongyu@qq.com
 * 描述：文件工具类
 */

public class FileTools {

    private FileTools() {
        throw new UnsupportedOperationException("u can not do the operation..");
    }

    /**
     * 根据路径得到文件
     * @param path
     * @return
     */
    public static File getFileByPath(String path){
        return StringTools.isSpace(path) ?  null : new File(path);
    }

    /**
     * 判断文件是否存在
     * @param file
     * @return
     */
    public static boolean isFileExists(File file){
        return null != file && file.exists();
    }

    /**
     * 判断文件是否存在
     * @param path 文件路径
     * @return true 存在
     */
    public static boolean isFileExists(String path){
        return isFileExists(getFileByPath(path));
    }
    /**
     * 判断重命名文件
     * @param filePath 文件路径
     * @param name  新名称
     * @return   true 为重复；
     */
    public static boolean rename(String filePath, String name) {
        return rename(getFileByPath(filePath),name);
    }

    /**
     * 判断重命名文件
     * @param file  父文件
     * @param name  新文件名
     * @return  true 为重复；
     */
    public static boolean rename(File file ,String name){
        if (file == null) return false;
        if (!file.exists()) return false;
        if (StringTools.isSpace(name)) return false;
        if (name.equals(file.getName())) return true;
        File newFile = new File(file.getParent() + File.separator + name);
        return !newFile.exists() && file.renameTo(newFile);
    }

    /**
     * 是否为文件夹
     * @param file
     * @return true 为文件夹
     */
    public static boolean isDir(File file){
        if (file == null) {
            return false;
        }
        return isFileExists(file) && file.isDirectory();
    }

    /**
     * 是否为文件夹
     * @param path
     * @return  true 为文件夹
     */
    public static boolean isDir(String path){
        return isDir(new File(path));
    }

    /**
     * 是否为文件
     * @param path
     * @return true为是文件
     */
    public static boolean isFile(String path)
    {
        return isFile(new File(path));
    }

    /**
     * 是否为文件
     * @param file
     * @return true为是文件
     */
    public static boolean isFile(File file) {
        return isFileExists(file) && file.isFile();
    }

    /**
     * 是否存在文件夹，不存在就创建
     * @param dirPath
     * @return  true创建成功，false创建失败
     */
    public static boolean createOrExistsDir(String dirPath){
        return createOrExistsDir(new File(dirPath));
    }

    /**
     * 是否存在文件夹，不存在就创建
     * @param file
     * @return  true创建成功，false创建失败
     */
    private static boolean createOrExistsDir(File file) {
        return null != file && (file.exists() ? file.isDirectory():file.mkdir());
    }

    /**
     * 是否存在，不存在创建文件
     * @param path
     * @return true为创建成功，false创建失败
     */
    public static boolean existsOrCreateFile(String path){
        return existsOrCreateFile(new File(path));
    }

    /**
     * 是否存在，不存在创建文件
     * @param file 希望创建的文件
     * @return true为创建成功，false创建失败
     */
    public static boolean existsOrCreateFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return  file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param filePath 文件路径
     * @return  true: 创建成功 false: 创建失败
     */
    public static boolean createFileByDeleteOldFile(String filePath) {
        return createFileByDeleteOldFile(getFileByPath(filePath));
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param file 文件
     * @return true: 创建成功 false: 创建失败
     */
    public static boolean createFileByDeleteOldFile(File file) {
        if (file == null) return false;
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile() && !file.delete()) return false;
        // 创建目录失败返回false
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制或移动目录
     *
     * @param srcDirPath  源目录路径
     * @param destDirPath 目标目录路径
     * @param isMove      是否移动
     * @return true: 复制或移动成功  false: 复制或移动失败
     */
    private static boolean copyOrMoveDir(String srcDirPath, String destDirPath, boolean isMove) {
        return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove);
    }

    /**
     * 复制或移动目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     * @param isMove  是否移动
     * @return  true: 复制或移动成功 false: 复制或移动失败
     */
    private static boolean copyOrMoveDir(File srcDir, File destDir, boolean isMove) {
        if (srcDir == null || destDir == null) return false;
        // 如果目标目录在源目录中则返回false，看不懂的话好好想想递归怎么结束
        // srcPath : F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
        // destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
        // 为防止以上这种情况出现出现误判，须分别在后面加个路径分隔符
        String srcPath = srcDir.getPath() + File.separator;
        String destPath = destDir.getPath() + File.separator;
        if (destPath.contains(srcPath)) return false;
        // 源文件不存在或者不是目录则返回false
        if (!srcDir.exists() || !srcDir.isDirectory()) return false;
        // 目标目录不存在返回false
        if (!createOrExistsDir(destDir)) return false;
        File[] files = srcDir.listFiles();
        for (File file : files) {
            File oneDestFile = new File(destPath + file.getName());
            if (file.isFile()) {
                // 如果操作失败返回false
                if (!copyOrMoveFile(file, oneDestFile, isMove)) return false;
            } else if (file.isDirectory()) {
                // 如果操作失败返回false
                if (!copyOrMoveDir(file, oneDestFile, isMove)) return false;
            }
        }
        return !isMove || deleteDir(srcDir);
    }

    /**
     * 复制或移动文件
     *
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @param isMove       是否移动
     * @return true: 复制或移动成功 false: 复制或移动失败
     */
    private static boolean copyOrMoveFile(String srcFilePath, String destFilePath, boolean isMove) {
        return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove);
    }

    /**
     * 复制或移动文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @param isMove   是否移动
     * @return true: 复制或移动成功 false: 复制或移动失败
     */
    private static boolean copyOrMoveFile(File srcFile, File destFile, boolean isMove) {
        if (srcFile == null || destFile == null) return false;
        // 源文件不存在或者不是文件则返回false
        if (!srcFile.exists() || !srcFile.isFile()) return false;
        // 目标文件存在且是文件则返回false
        if (destFile.exists() && destFile.isFile()) return false;
        // 目标目录不存在返回false
        if (!createOrExistsDir(destFile.getParentFile())) return false;
        try {
            return writeFileFromIS(destFile, new FileInputStream(srcFile), false)
                    && !(isMove && !deleteFile(srcFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 复制目录
     * @param srcDirPath  源目录路径
     * @param destDirPath 目标目录路径
     * @return true: 复制成功 false: 复制失败
     */
    public static boolean copyDir(String srcDirPath, String destDirPath) {
        return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    /**
     * 复制目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     * @return true: 复制成功 false: 复制失败
     */
    public static boolean copyDir(File srcDir, File destDir) {
        return copyOrMoveDir(srcDir, destDir, false);
    }

    /**
     * 复制文件
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @return true 复制成功 false 复制失败
     */
    public static boolean copyFile(String srcFilePath, String destFilePath) {
        return copyFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    /**
     * 复制文件
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return true: 复制成功  false: 复制失败
     */
    public static boolean copyFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, false);
    }

    /**
     * 移动目录
     * @param srcDirPath  源目录路径
     * @param destDirPath 目标目录路径
     * @return true移动成功 false 移动失败
     */
    public static boolean moveDir(String srcDirPath, String destDirPath) {
        return moveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath));
    }

    /**
     * 移动目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     * @return  true移动成功 false 移动失败
     */
    public static boolean moveDir(File srcDir, File destDir) {
        return copyOrMoveDir(srcDir, destDir, true);
    }

    /**
     * 移动文件
     *
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @return true 移动成功 false 移动失败
     */
    public static boolean moveFile(String srcFilePath, String destFilePath) {
        return moveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath));
    }

    /**
     * 移动文件
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return true移动成功 false 移动失败
     */
    public static boolean moveFile(File srcFile, File destFile) {
        return copyOrMoveFile(srcFile, destFile, true);
    }

    /**
     * 删除目录
     * @param dirPath 目录路径
     * @return true删除成功false 删除失败
     */
    public static boolean deleteDir(String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    /**
     * 删除目录
     * @param dir 目录
     * @return true 删除成功，false 删除失败
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除文件
     * @param srcFilePath 文件路径
     * @return true: 删除成功 false: 删除失败
     */
    public static boolean deleteFile(String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    /**
     * 删除文件
     * @param file 文件
     * @return true: 删除成功  false: 删除失败
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * 删除目录下的所有文件
     * @param dirPath 目录路径
     * @return true: 删除成功 false: 删除失败
     */
    public static boolean deleteFilesInDir(String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    /**
     * 删除目录下的所有文件
     * @param dir 目录
     * @return  true: 删除成功 false: 删除失败
     */
    public static boolean deleteFilesInDir(File dir) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取目录下所有文件
     * @param dirPath     目录路径
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    /**
     * 获取目录下所有文件
     * @param dir         目录
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    public static List<File> listFilesInDir(File dir, boolean isRecursive) {
        if (!isDir(dir)) return null;
        if (isRecursive) return listFilesInDir(dir);
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            Collections.addAll(list, files);
        }
        return list;
    }

    /**
     * 获取目录下所有文件包括子目录
     *
     * @param dirPath 目录路径
     * @return 文件链表
     */
    public static List<File> listFilesInDir(String dirPath) {
        return listFilesInDir(getFileByPath(dirPath));
    }

    /**
     * 获取目录下所有文件包括子目录
     * @param dir 目录
     * @return 文件链表
     */
    public static List<File> listFilesInDir(File dir) {
        if (!isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFilesInDir(file));
                }
            }
        }
        return list;
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath 路径
     * @param is       输入流
     * @param append   是否追加在文件末
     * @return  true: 写入成功 false: 写入失败
     */
    public static boolean writeFileFromIS(String filePath, InputStream is, boolean append) {
        return writeFileFromIS(getFileByPath(filePath), is, append);
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param is     输入流
     * @param append 是否追加在文件末
     * @return true: 写入成功 false: 写入失败
     */
    public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
        if (file == null || is == null) return false;
        if (!existsOrCreateFile(file)) return false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[1024];
            int len;
            while ((len = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 将字符串写入文件
     * @param filePath 文件路径
     * @param content  写入内容
     * @param append   是否追加在文件末
     * @return true: 写入成功 false: 写入失败
     */
    public static boolean writeFileFromString(String filePath, String content, boolean append) {
        return writeFileFromString(getFileByPath(filePath), content, append);
    }

    /**
     * 将字符串写入文件
     *
     * @param file    文件
     * @param content 写入内容
     * @param append  是否追加在文件末
     * @return  true: 写入成功 false: 写入失败
     */
    public static boolean writeFileFromString(File file, String content, boolean append) {
        if (file == null || content == null) return false;
        if (!existsOrCreateFile(file)) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeIO(bw);
        }
    }

    /**
     * 关闭IO
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
