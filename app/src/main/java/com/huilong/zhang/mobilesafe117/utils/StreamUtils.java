package com.huilong.zhang.mobilesafe117.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Mario on 1/17/16.
 * 将输入流读取成为String后返回
 */
public class StreamUtils {
    public static String readFromString(InputStream inputStream) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        try {
            while((len = inputStream.read(buffer)) != -1){
                out.write(buffer,0,len);
            }
            String result = out.toString();
            inputStream.close();
            out.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  "";
    }
}
