package com.zou.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author leonard
 */
@Slf4j
public class Convert {

    private static final Gson gson = new Gson();

    private Convert() {
    }

    public static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

    public static <T> T readJsonFile2Object(String filePath, Class<T> classOfT, boolean isSpringRelativePath) throws IOException {
        if (isSpringRelativePath) {
            try (InputStreamReader reader = new InputStreamReader(Convert.class.getResourceAsStream(filePath))) {
                return gson.fromJson(reader, classOfT);
            }
        }
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, classOfT);
        }
    }

    public static void copyFile(String src, String dist, boolean isByte) throws IOException {
        if (isByte) {
            copyFileByteStream(src, dist);
        } else {
            copyFileChar(src, dist);
        }
    }


    public static <T> String toString(T obj, boolean printDetail) {
        if (printDetail) {
            if (obj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) obj;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    log.info(entry.getKey() + ":" + entry.getValue());
                }
            } else if (obj instanceof List) {
                List<?> list = (List) obj;
                Iterator<?> iterator = list.iterator();
                while (iterator.hasNext()) {
                    log.info(iterator.next() + " ");
                }
            } else {
                log.info(gson.toJson(obj));
            }
        }
        return gson.toJson(obj);
    }

    public static <T> T getObject(Class<T> c) throws InstantiationException, IllegalAccessException {
        return c.newInstance();
    }


    private static void copyFileByteStream(String src, String dist) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dist))) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    private static void copyFileChar(String src, String dist) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(src));
             BufferedWriter writer = new BufferedWriter(new FileWriter(dist))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
