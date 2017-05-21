package com.gabrielezanelli.whatapic;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

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
 * Class for managing instagram's api through HTTP requests
 */

public class InstagramRequestManager {

    @BindString(R.string.instagram_api_user_information)
    String userInfoUrl;
    @BindString(R.string.instagram_api_user_media)
    String userMediaUrl;
    @BindString(R.string.instagram_api_authorization)
    String authorizationUrl;
    @BindString(R.string.parameter_client_id)
    String paramClientId;
    @BindString(R.string.parameter_redirect_uri)
    String paramRedirectUri;
    @BindString(R.string.parameter_response_type)
    String paramResponseType;
    @BindString(R.string.parameter_access_token)
    String paramAccessToken;
    @BindString(R.string.client_id)
    String clientId;
    @BindString(R.string.redirect_uri)
    String redirectUri;
    @BindString(R.string.response_type)
    String responseType;

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

    /**
     * Build the authentication url for getting user's access token
     * @return the authentication url
     */
    public String getAuthenticationUrl() {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(authorizationUrl).newBuilder();
        urlBuilder.addQueryParameter(paramClientId, clientId);
        urlBuilder.addQueryParameter(paramRedirectUri, redirectUri);
        urlBuilder.addQueryParameter(paramResponseType, responseType);

        return urlBuilder.build().toString();
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    /**
     * Asks instagram for the user's informations using his token
     * @param token access token of the user
     */
    public void requestUserInformation(String token) {

        OkHttpClient client = getInstance();

        instagramUser.accessToken = token;
        HttpUrl.Builder urlBuilder = HttpUrl.parse(userInfoUrl).newBuilder();
        urlBuilder.addQueryParameter(paramAccessToken, instagramUser.accessToken);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Toast.makeText(context,R.string.instagram_request_fail,Toast.LENGTH_SHORT).show();
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

    /**
     * Parse the JSON response from instagram and copy the result in the static class InstagramUser
     * @param jsonString the JSON response from instagram
     */
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
    }

    /**
     * Asks instagram for the user's recent photos using his token
     * @param galleryAdapter The RecyclerViewAdapter for loading photos
     */
    public void requestUserPhotos(final GalleryAdapter galleryAdapter) {
        OkHttpClient client = getInstance();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(userMediaUrl).newBuilder();
        urlBuilder.addQueryParameter("access_token", instagramUser.accessToken);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Toast.makeText(context,R.string.instagram_request_fail,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String jsonString = response.body().string();
                    System.out.println("Request was Successful");

                    parseAndAddUserPhotosUrls(jsonString, galleryAdapter);
                }
            }
        });
    }

    /**
     * Parse the JSON response from instagram into photo urls and adds them into the adapter's structure
     * @param jsonString the JSON response from instagram
     * @param galleryAdapter The RecyclerViewAdapter for loading photos
     */
    private void parseAndAddUserPhotosUrls(String jsonString, final GalleryAdapter galleryAdapter) {
        try {

            JSONArray jsonArray = new JSONObject(jsonString).getJSONArray("data");
            JSONObject images;
            for (int i = 0; i < jsonArray.length(); i++) {
                images = ((JSONObject) jsonArray.get(i)).getJSONObject("images");
                String thumbnailUrl = images.getJSONObject("thumbnail").getString("url");
                String photoUrl = images.getJSONObject("standard_resolution").getString("url");
                galleryAdapter.addUrls(thumbnailUrl, photoUrl);
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
