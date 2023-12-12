package com.gatning.ip_scan.utils;


import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpClientUtils {


    public static String postBody(String urlPath, String json) {
        String response = null;

        try {
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestProperty("content-type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            urlConnection.setRequestProperty("contentType", "UTF-8");
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
            out.write(json);
            out.flush();
            out.close();
            InputStream inputStream = urlConnection.getInputStream();
            String encoding = urlConnection.getContentEncoding();
            String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            if (urlConnection.getResponseCode() == 200) {
                response = body;
            }
        } catch (IOException var9) {

        }

        return response;
    }
}
