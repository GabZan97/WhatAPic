package com.gabrielezanelli.whatapic;

import android.os.AsyncTask;
import java.net.URL;

public class InstagramUser {
    public String id;
    public String username;
    public String fullName;
    public String profilePictureUrl;
    public String accessToken;


    public InstagramUser(){

    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
        new GetUserInfoTask().execute();
    }

    public void setUserInformation(String id, String username, String fullName, String profilePictureUrl){
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.profilePictureUrl = profilePictureUrl;
    }


    public class GetUserInfoTask extends AsyncTask<URL, Integer, Long> {

        public GetUserInfoTask() {
        }

        protected Long doInBackground(URL... urls) {
            long result = 0;

            new InstagramRequestManager().requestUserInformation();
            return result;
        }

        protected void onPostExecute(Long result) {
            //savePreferences();
        }
    }

}