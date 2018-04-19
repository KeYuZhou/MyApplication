package com.example.chirag.slidingtabsusingviewpager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by effy on 2018/3/31.
 */

public class HttpUtils {

    private final static String URL_PATH = "https://img3.doubanio.com/lpic/s28509222.jpg";

    public HttpUtils() {
    }

    public static InputStream getImageViewInputStream() throws IOException {
        InputStream inputStream = null;
        URL url = new URL(URL_PATH);
        if (url != null) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
//            httpURLConnection.connect();
            int response_code = httpURLConnection.getResponseCode();
            if (response_code == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        }
        return inputStream;
    }
}
