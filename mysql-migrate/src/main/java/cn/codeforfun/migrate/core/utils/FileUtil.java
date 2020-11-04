package cn.codeforfun.migrate.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 文件工具类
 *
 * @author org.springframework.util.ClassUtils
 * @author wangbin
 */
public class FileUtil {

    public static final String UTF_8 = "utf-8";
    public static final String GB_2312 = "gb2312";

    public static String getStringByClasspath(String filePath) {
        ClassLoader classLoader = getDefaultClassLoader();
        InputStream is = classLoader.getResourceAsStream(filePath);
        return toString(is);
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = FileUtil.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public static String toString(InputStream is) {
        if (is == null) {
            try {
                throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream boa = null;
        try {
            boa = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                boa.write(buffer, 0, len);
            }
            is.close();
            boa.close();
            byte[] result = boa.toByteArray();
            String temp = new String(result);
            if (temp.contains(UTF_8)) {
                return new String(result, StandardCharsets.UTF_8);
            } else if (temp.contains(GB_2312)) {
                return new String(result, "gb2312");
            } else {
                return new String(result, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (boa != null) {
                try {
                    boa.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
