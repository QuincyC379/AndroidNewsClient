package com.gc.newssplider.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author gc
 */
public class HtmlUtil {

    /**
     * 返回该链接地址的html数据
     */
    public static String doGet(String urlStr) throws IOException {
        // StringBuffer 字符串变量，可以扩充和修改
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlStr);
            // 创建HttpURLConnection实例
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            if (conn.getResponseCode() == 200) {
                // 这里设置utf-8,防止中文乱码
                InputStreamReader in = new InputStreamReader(conn.getInputStream(),"utf-8");
                BufferedReader bufferedReader = new BufferedReader(in);
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                in.close();
            } else {
                throw new IOException("访问网络失败！");
            }
        } catch (IOException e) {
            throw new IOException("访问网络失败！");
        }
        return sb.toString();
    }

}