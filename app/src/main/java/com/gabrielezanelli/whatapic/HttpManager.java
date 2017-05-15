package com.gabrielezanelli.whatapic;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Java class responsible for http requests and connection handling
 */


public class HttpManager {
    private static OkHttpClient clientInstance;

    private static OkHttpClient getInstance() {
        if (clientInstance == null)
            clientInstance = new OkHttpClient();
        return clientInstance;
    }

    public static void instagramAuth() {

        String clientID = "782272ecc6664711870a7d33847decd1";
        String redirectURI = "http://localhost:8080/InstagramAPI/callback";

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.instagram.com/oauth/authorize").newBuilder();
        urlBuilder.addQueryParameter("client_id", clientID);
        urlBuilder.addQueryParameter("redirect_uri", redirectURI);
        urlBuilder.addQueryParameter("response_type", "token");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();


    }

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
                    System.out.println("Request was Successful");
                }
            }


        });
    }


}
