package com.gabrielezanelli.whatapic;

import android.app.Activity;
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

import butterknife.BindString;
import butterknife.ButterKnife;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

/**
 * Class for http request through instagram's api
 */

public class InstagramRequestManager {

    @BindString(R.string.instagram_api_user_information)
    String userInfoUrl;
    @BindString(R.string.instagram_api_user_media)
    String userMediaUrl;

    private static OkHttpClient clientInstance;
    private Context context;

    public InstagramRequestManager(Activity activity) {
        context = activity.getApplicationContext();
        ButterKnife.bind(this, activity);
    }

    private static OkHttpClient getInstance() {
        if (clientInstance == null)
            clientInstance = new OkHttpClient();
        return clientInstance;
    }

    public void requestUserInformation() {
        OkHttpClient client = getInstance();

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
                    System.out.println("Request was Successful");

                    parseAndSetUserInformation(jsonString);
                }
            }


        });
    }

    private void parseAndSetUserInformation(String jsonString) {
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

        instagramUser.setUserInformation(id, username, fullName, profilePictureUrl);
        // TODO: Save settings in shared preferences
    }

    public void requestUserPhotos(final GalleryAdapter galleryAdapter) {
        OkHttpClient client = getInstance();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(userMediaUrl).newBuilder();
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
                    System.out.println("Request was Successful");

                    parseAndAddUserPhotos(jsonString, galleryAdapter);
                }
            }
        });
    }

    private void parseAndAddUserPhotos(String jsonString, final GalleryAdapter galleryAdapter) {
        try {

            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");
            JSONObject images;
            for (int i = 0; i < jsonArray.length(); i++) {
                images = ((JSONObject) jsonArray.get(i)).getJSONObject("images");
                String thumbnailUrl = images.getJSONObject("thumbnail").getString("url");
                String photoUrl = images.getJSONObject("standard_resolution").getString("url");
                galleryAdapter.addUrl(thumbnailUrl, photoUrl);
            }

            new Handler(context.getMainLooper()).post(new Runnable() {
                public void run() {
                    galleryAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
