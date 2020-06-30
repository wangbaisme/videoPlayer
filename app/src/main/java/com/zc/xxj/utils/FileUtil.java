package com.zc.xxj.utils;

import com.zc.player.util.LoggerUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class FileUtil {

    public static byte[] readStream(String path) throws Exception {
        FileInputStream fs = new FileInputStream(path);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            LoggerUtil.showDebugLog("info1",buffer+"");
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }
}
