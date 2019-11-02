//package com.dark.new_test_job.parser;
//
////import my.package.exceptions.CustomException;
//
//import android.util.Log;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.URLDecoder;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//public class ConnectionUtil {
//    public static String postData(String url, String postData)
//            throws CustomException {
//
//        // Create a new HttpClient and Post Header
//        InputStream is = null;
//        StringBuilder sb = null;
//        String result = "";
//        HttpClient httpclient = new DefaultHttpClient();
//
//        HttpPost httppost = new HttpPost();
//        httppost.setHeader("host", url);
//
//        Log.v("ConnectionUtil", "Opening POST connection to URI = " + httppost.getURI() + " url = " + URLDecoder.decode(url));
//
//        try {
//            httppost.setEntity(new StringEntity(postData));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity entity = response.getEntity();
//            is = entity.getContent();
//
//        } catch (Exception e) {
//            Log.e("log_tag", "Error in http connection " + e.toString());
//            e.printStackTrace();
//            throw new CustomException("Could not establish network connection");
//        }
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    is, "utf-8"), 8);
//            sb = new StringBuilder();
//            sb.append(reader.readLine() + "
//                    ");
//                    String line = "0";
//
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "
//                        ");
//            }
//
//            is.close();
//            result = sb.toString();
//
//        } catch (Exception e) {
//            Log.e("log_tag", "Error converting result " + e.toString());
//            throw new CustomException("Error parsing the response");
//        }
//        Log.v("ConnectionUtil", "Sent: "+postData);
//        Log.v("ConnectionUtil", "Got result "+result);
//        return result;
//
//    }
//    public String urlPost(String strJsonRequest, String strURL) throws Exception
//    {
//        try
//        {
//            URL objURL = new URL(strURL);
//            connection = (HttpURLConnection)objURL.openConnection();
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//            connection.setAllowUserInteraction(false);
//            connection.setUseCaches(false);
//            connection.setConnectTimeout(TIMEOUT_CONNECT_MILLIS);
//            connection.setReadTimeout(TIMEOUT_READ_MILLIS);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Accept-Charset", "utf-8");
//            connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
//            connection.setRequestProperty("Content-Length", ""+strJsonRequest.toString().getBytes("UTF8").length);
//
//            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//
//            byte [] b = strJsonRequest.getBytes("UTF-8");
//
//            outputStream.write(b);
//            outputStream.flush();
//
//            inputstreamObj = (InputStream) connection.getContent();//getInputStream();
//
//            if(inputstreamObj != null)
//                strResponse = convertStreamToString(inputstreamObj);
//
//        }
//        catch(Exception e)
//        {
//            throw e;
//        }
//        return strResponse;
//    }
//}