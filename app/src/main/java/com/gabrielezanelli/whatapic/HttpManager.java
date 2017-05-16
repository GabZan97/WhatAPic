package com.gabrielezanelli.whatapic;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

public class HttpManager {




    private static void makeRequest(Request request){
        OkHttpClient client = getInstance();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {

                    System.out.println("Request was Successful:\n"+ response.toString());
                }
            }


        });
    }


}
