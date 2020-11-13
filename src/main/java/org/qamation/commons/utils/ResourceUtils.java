package org.qamation.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Pavel.Gouchtchine on 12/09/2016.
 */
public class ResourceUtils {
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");
    public static Logger log = LoggerFactory.getLogger(ResourceUtils.class);
    public static String PROPERTIES_FILE_SUFFIX = ".properties";

    public static URL getURLForResouce(String resourceName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return classLoader.getResource(resourceName);
    }
    public static String getSystemProperty(String property, String defValue) {
        String value = System.getProperty(property);
        if (value == null) {
            log.warn(property+" is not set. It is recommended to set it using -D"+property+" for a start up string.");
            return defValue;
        };
        return value;
    }

    public static void loadProperties(String path) {
        loadAllProperties(path);
    }

    public static void loadProperties() {
        loadAllProperties(System.getProperty("user.dir"));
    }


    private static void loadAllProperties(String propertiesPath) {

        String[] paths = listFilesInFolder(propertiesPath);
        for (String p: paths) {
            if (p.endsWith(".properties") || p.endsWith("PROPERTIES") ) loadPropertiesFile(p);
        }
    }

    public static String[] listFilesInFolder(String root) {
        File file = new File(root);
        if (file.exists()) {
            if (file.isDirectory()) {
                String rootPath = file.getPath();
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


}
