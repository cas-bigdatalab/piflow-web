package cn.cnic.component.stopsComponent.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;
import java.util.List;

/**
 * 解压缩
 * @author Administrator
 *
 */
public class UnCompressUtils {
    private static final String BASE_DIR = "";

    // 符号"/"用来作为目录标识判断符
    private static final String PATH = File.separator;
    private static final int BUFFER = 1024;
    private static final String EXT = ".tar";
    /**
     * 归档
     *
     * @param srcPath
     * @param destPath
     * @throws Exception
     */
    public static void archive(String srcPath, String destPath)
            throws Exception {

        File srcFile = new File(srcPath);

        archive(srcFile, destPath);

    }

    /**
     * 归档
     *
     * @param srcFile
     *            源路径
     * @param destFile
     *            目标路径
     * @throws Exception
     */
    public static void archive(File srcFile, File destFile) throws Exception {

        TarArchiveOutputStream taos = new TarArchiveOutputStream(
                new FileOutputStream(destFile));

        archive(srcFile, taos, BASE_DIR);

        taos.flush();
        taos.close();
    }

    /**
     * 归档
     *
     * @param srcFile
     * @throws Exception
     */
    public static void archive(File srcFile) throws Exception {
        String name = srcFile.getName();
        String basePath = srcFile.getParent();
        String destPath = basePath+File.separator + name + EXT;
        archive(srcFile, destPath);
    }

    /**
     * 归档文件
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void archive(File srcFile, String destPath) throws Exception {
        archive(srcFile, new File(destPath));
    }

    /**
     * 归档
     *
     * @param srcPath
     * @throws Exception
     */
    public static void archive(String srcPath) throws Exception {
        File srcFile = new File(srcPath);

        archive(srcFile);
    }

    /**
     * 归档
     *
     * @param srcFile
     *            源路径
     * @param taos
     *            TarArchiveOutputStream
     * @param basePath
     *            归档包内相对路径
     * @throws Exception
     */
    private static void archive(File srcFile, TarArchiveOutputStream taos,
                                String basePath) throws Exception {
        if (srcFile.isDirectory()) {
            archiveDir(srcFile, taos, basePath);
        } else {
            archiveFile(srcFile, taos, basePath);
        }
    }

    /**
     * 目录归档
     *
     * @param dir
     * @param taos
     *            TarArchiveOutputStream
     * @param basePath
     * @throws Exception
     */
    private static void archiveDir(File dir, TarArchiveOutputStream taos,
                                   String basePath) throws Exception {

        File[] files = dir.listFiles();

        if (files.length < 1) {
            TarArchiveEntry entry = new TarArchiveEntry(basePath
                    + dir.getName() + PATH);

            taos.putArchiveEntry(entry);
            taos.closeArchiveEntry();
        }

        for (File file : files) {

            // 递归归档
            archive(file, taos, basePath + dir.getName() + PATH);

        }
    }

    /**
     * 归档内文件名定义
     *
     * <pre>
     * 如果有多级目录，那么这里就需要给出包含目录的文件名
     * 如果用WinRAR打开归档包，中文名将显示为乱码
     * </pre>
     */
    private static void archiveFile(File file, TarArchiveOutputStream taos, String dir) throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry(dir + file.getName());
        //如果打包不需要文件夹，就用 new TarArchiveEntry(file.getName())
        entry.setSize(file.length());
        taos.putArchiveEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file));
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = bis.read(data, 0, BUFFER)) != -1) {
            taos.write(data, 0, count);
        }

        bis.close();
        taos.closeArchiveEntry();
    }

    /**
     * 解归档
     *
     * @param srcFile
     * @throws Exception
     */
    public static void dearchive(File srcFile) throws Exception {
        String basePath = srcFile.getParent();
        dearchive(srcFile, basePath);
    }

    /**
     * 解归档
     *
     * @param srcFile
     * @param destFile
     * @throws Exception
     */
    public static void dearchive(File srcFile, File destFile) throws Exception {
        TarArchiveInputStream tais = new TarArchiveInputStream(
                new FileInputStream(srcFile));
        dearchive(destFile, tais);
        tais.close();

    }

    /**
     * 解归档
     *
     * @param srcFile
     * @param destPath
     * @throws Exception
     */
    public static void dearchive(File srcFile, String destPath) throws Exception {
        dearchive(srcFile, new File(destPath));
    }

    /**
     * 文件 解归档
     *
     * @param destFile
     *            目标文件
     * @param tais
     *            ZipInputStream
     * @throws Exception
     */
    private static void dearchive(File destFile, TarArchiveInputStream tais) throws Exception {
        TarArchiveEntry entry = null;
        while ((entry = tais.getNextTarEntry()) != null) {
            // 文件
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);
            // 文件检查
            fileProber(dirFile);
            if (entry.isDirectory()) {
                dirFile.mkdirs();
            } else {
                dearchiveFile(dirFile, tais);
            }
        }
    }

    /**
     * 文件 解归档
     *
     * @param srcPath
     *            源文件路径
     *
     * @throws Exception
     */
    public static void dearchive(String srcPath) throws Exception {
        File srcFile = new File(srcPath);
        dearchive(srcFile);
    }

    /**
     * 文件 解归档
     *
     * @param srcPath
     *            源文件路径
     * @param destPath
     *            目标文件路径
     * @throws Exception
     */
    public static void dearchive(String srcPath, String destPath) throws Exception {
        File srcFile = new File(srcPath);
        dearchive(srcFile, destPath);
    }

    /**
     * 文件解归档
     *
     * @param destFile
     *            目标文件
     * @param tais
     *            TarArchiveInputStream
     * @throws Exception
     */
    private static void dearchiveFile(File destFile, TarArchiveInputStream tais)
            throws Exception {

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile));

        int count;
        byte data[] = new byte[BUFFER];
        while ((count = tais.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }

        bos.close();
    }

    /**
     * 文件探针
     *
     * <pre>
     * 当父目录不存在时，创建目录！
     * </pre>
     *
     * @param dirFile
     */
    private static void fileProber(File dirFile) {

        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists()) {

            // 递归寻找上级目录
            fileProber(parentFile);

            parentFile.mkdir();
        }

    }

    public static String getImageName(String tempFilePath) throws Exception{
        // System.out.println("tempFilePath = " + tempFilePath);
        String dirPath = tempFilePath.substring(0, tempFilePath.lastIndexOf("."));
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        dearchive(tempFilePath, dirPath);
        if (!dirPath.endsWith(File.separator)) {
            dirPath += File.separator;
        }
        //目标文件
        String destFilePath = dirPath + "manifest.json";
        File destFile = new File(destFilePath);
        if (!destFile.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        InputStream io = new FileInputStream(new File(destFilePath));
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = io.read(bytes)) > 0) {
            sb.append(new String(bytes, 0, len));
        }
        String content = sb.toString();
        //只取第一个配置项
        List<JSONObject> jsonList = (List<JSONObject>) JSON.parse(content);
        System.out.println("jsonList = " + jsonList);
        return ((List<String>)jsonList.get(0).get("RepoTags")).get(0);
        /*if (content.startsWith("[")) {
            content = content.substring(1, content.length() - 2);
        }
        System.out.println("content = " + content);
        JSONObject json = JSONObject.parseObject(content.toString());
        List<String> list = (List<String>) json.get("RepoTags");
        System.out.println("list = " + list);
        return list.get(0);*/
    }


}
