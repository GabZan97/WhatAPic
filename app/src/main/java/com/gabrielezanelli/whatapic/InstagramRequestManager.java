package com.gabrielezanelli.whatapic;

import android.content.Context;
import android.os.Handler;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

public class InstagramRequestManager {

    private static OkHttpClient clientInstance;

    private static OkHttpClient getInstance() {
        if (clientInstance == null)
            clientInstance = new OkHttpClient();
        return clientInstance;
    }

    public void requestUserInformation() {
        OkHttpClient client = getInstance();

        String userInfoUrl = "https://api.instagram.com/v1/users/self";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(userInfoUrl).newBuilder();
        urlBuilder.addQueryParameter("access_token", instagramUser.getAccessToken());

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

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
                    String jsonString = response.body().string();
                    System.out.println("Request was Successful:\n"+ jsonString);
                    String id = "", username = "", fullName = "", profilePictureUrl = "";
                    try {
                        JSONObject json = new JSONObject(jsonString);
                        JSONObject dataJson = json.getJSONObject("data");
                        id = dataJson.getString("id");
                        username = dataJson.getString("username");
                        fullName = dataJson.getString("full_name");
                        profilePictureUrl = dataJson.getString("profile_picture");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    instagramUser.setUserInformation(id,username,fullName,profilePictureUrl);
                }
            }


        });
    }

    public void requestUserPhotos(final GalleryAdapter galleryAdapter,final Context context) {
        OkHttpClient client = getInstance();

        String userInfoUrl = "https://api.instagram.com/v1/users/self/media/recent";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(userInfoUrl).newBuilder();
        urlBuilder.addQueryParameter("access_token", instagramUser.getAccessToken());

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

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
                    String jsonString = response.body().string();
                    System.out.println("Request was Successful:\n"+ jsonString);

                    try {

                        JSONArray jsonArray= new JSONObject(jsonString).getJSONArray("data");
                        JSONObject data;
                        for(int i=0; i<jsonArray.length();i++) {
                            data = (JSONObject) jsonArray.get(i);
                            String thumbnailUrl = data.getJSONObject("images")
                                    .getJSONObject("thumbnail").getString("url");
                            galleryAdapter.addUrl(thumbnailUrl);
                        }

                        new Handler(context.getMainLooper()).post(new Runnable(){
                            public void run(){
                                galleryAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }


        });
    }





}
