package org.qamation.commons.utils;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Pavel on 2017-03-25.
 */
public class FileUtils {
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");

    public static Path copyFileToSameFolder(String sourceFilePath, String newfileName) {
        File f = new File(sourceFilePath);
        if (f.isDirectory()) throw new RuntimeException("Given path: "+sourceFilePath+" should lead to a file.");
        String fileName = getFileName(f.getAbsolutePath());
        String fileExt = getFileNameExtention(fileName);
        String filePath = getPathToFile(f.getAbsolutePath());
        try {
            return  Files.copy(f.toPath(), Paths.get(filePath,newfileName), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public static String getFileName(String absolutePath) {
        Path p = Paths.get(absolutePath);
        return p.getFileName().toString();
    }

    public static String getPathToFile(String absolutePath) {
        Path p = Paths.get(absolutePath);
        return p.getParent().toString();
    }

    public static String getFileNameExtention(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf("."));
        return ext;
    }

    public static String createTempFile(String origFileName) {
        String prefix = generateFileNamePrefix();
        return createTempFile(origFileName,prefix);
    }

    public static String createTempFile(String origFileName, String tempFileNamePrefix) {
        String suffix = FileUtils.getFileNameExtention(origFileName);
        String tempFileName = tempFileNamePrefix+suffix;
        Path p = FileUtils.copyFileToSameFolder(origFileName,tempFileName);
        return p.toString();
    }

    public static String[] listFilesInFolder(String root) {
        File file = new File(root);
        if (file.exists()) {
            if (file.isDirectory()) {
                String rootPath = file.getPath(); // to convert c:/tmp/mq into c:\tmp\mq if needed.
                ArrayList<String> list = new ArrayList<String>();
                processDirectory(rootPath, file, list);
                return list.toArray(new String[]{});
            }
            else return new String[]{file.getPath()};
        }
        else {
            throw new RuntimeException("There is no file or directory at: " + root);
        }
    }



    private static void processDirectory(String startPath, File file, ArrayList<String> list) {
        if (file.isDirectory()) {
            for (String fileName : file.list()) {
                String path = startPath + FILE_SEPARATOR + fileName;
                processDirectory(path, new File(path), list);
            }
        }
        else {
            list.add(file.getPath());
        }
    }


    private static String generateFileNamePrefix() {
        SecureRandom sr = new SecureRandom();
        long l = sr.nextLong();
        return String.valueOf(l);
    }

    public static Properties loadPropertiesFile(String path) {
        try {
            Reader reader = new FileReader(path);
            Properties props = new Properties();
            props.load(reader);
            Set<String> keys = props.stringPropertyNames();
            for(String k:keys) {
                String value = props.getProperty(k);
                System.setProperty(k,value);
            }
            reader.close();
            return props;

        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unable to load properties file: "+path,ex);
        }
    }

    public static PrintStream createFilePrintStream(String path) throws IOException {
        File outFile = new File(path);
        outFile.createNewFile();
        return new PrintStream(outFile);
    }

    public static FileWriter createFileWriter (String fileName, boolean append) throws IOException {
        File outFile = new File(fileName);
        return new FileWriter(outFile,append);
    }



}
