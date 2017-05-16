package com.gabrielezanelli.whatapic;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONArray;

import java.net.URL;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

public class InstagramUser {
    private String id;
    private String username;
    private String fullName;
    private String profilePicture;
    private String accessToken;

    public InstagramUser(){

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;

        new GetUserInfoTask(accessToken).execute();
    }

    private static OkHttpClient clientInstance;

    private static OkHttpClient getInstance() {
        if (clientInstance == null)
            clientInstance = new OkHttpClient();
        return clientInstance;
    }


    public class GetUserInfoTask extends AsyncTask<URL, Integer, Long> {
        ProgressDialog progressDlg;
        InstagramUser user;
        String code;

        public GetUserInfoTask(String code) {
            this.code		= code;

            progressDlg 	= new ProgressDialog(mContext);

            progressDlg.setMessage("Getting access token...");
        }

        protected void onCancelled() {
            progressDlg.cancel();
        }

        protected void onPreExecute() {
            progressDlg.show();
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            //

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Long result) {
            progressDlg.dismiss();

            if (user != null) {
                InstagramPreferences.saveSettings(user);

                mListener.onSuccess(user);
            } else {
                mListener.onError("Failed to get access token");
            }
        }
    }


    public static JSONArray getUserInformation() {

        String userInfoUrl = "https://api.instagram.com/v1/users/self";

        HttpUrl.Builder urlBuilder = HttpUrl.parse(userInfoUrl).newBuilder();
        urlBuilder.addQueryParameter("access_token", instagramUser.getAccessToken());

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        makeRequest(request);



    }



}