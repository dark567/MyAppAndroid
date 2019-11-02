package com.dark.new_test_job.other;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by dark on 10.07.2016.
 */
public class test {

        public static void main(String... params_i) throws Exception {

            String login_url = params_i[0];

            try {
            URL url = new URL(login_url);

            Map<String,Object> params = new LinkedHashMap<>();
            params.put("guard", params_i[1]);
            params.put("facility", params_i[2]);
            params.put("status", params_i[3]);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0;)
                System.out.print((char)c);

            Log.d("postData", postData.toString());
        }
         catch (MalformedURLException e) {
        e.printStackTrace();
        Log.d("sayHello", e.toString());
         } catch (IOException e) {
                e.printStackTrace();
                Log.d("sayHello", e.toString());
            }
    }
}
