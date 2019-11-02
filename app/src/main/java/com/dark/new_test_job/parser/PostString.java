package com.dark.new_test_job.parser;

/**
 * Created by dark on 10.07.2016.
 *//*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
    import android.util.Log;

    import java.io.IOException;
        import okhttp3.MediaType;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

public final class PostString {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {

       String postBody = "?"
                + "guard="
                + "1"
                + "&facility="
                + "1"
                + "&status="
                + "1";


        Request request = new Request.Builder()
                .url("http://192.168.232.130/test/")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("XXX Unexpected code " + response);

        Log.e("result",request.toString());
        Log.e("result",postBody.toString());

            System.out.println("result "+response.body().string());
            System.out.println(response.body().string());
        }
        catch (Exception e)
        {
            e.printStackTrace();;
        }
    }

    public static void main(String... args) throws Exception {
        new PostString().run();
    }
}