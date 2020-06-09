package com.wujiuye.jcg.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * class输出到文件工具类
 *
 * @author wujiuye
 * @version 1.0 on 2019/11/24
 */
public class ClassFileUtils {

    private static String CLASS_SAVA_PATH;

    static {
        CLASS_SAVA_PATH = System.getProperty("jcg.classSavaPath", null);
        if (!CLASS_SAVA_PATH.endsWith("/")) {
            CLASS_SAVA_PATH = CLASS_SAVA_PATH + "/";
        }
    }

    /**
     * 将字节码转为class文件输出到类路径下
     *
     * @throws IOException
     */
    public static void savaToClasspath(String className, byte[] byteCode) {
        if (CLASS_SAVA_PATH == null) {
            return;
        }
        if (className.contains(".")) {
            className = className.replace(".", "/");
        }
        File packageFile = new File(CLASS_SAVA_PATH + className.substring(0, className.lastIndexOf("/")));
        if (!packageFile.exists() && !packageFile.mkdirs()) {
            return;
        }
        File file = new File(CLASS_SAVA_PATH + className + ".class");
        try {
            if ((!file.exists() || file.delete()) && file.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(byteCode);
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
    }

}
