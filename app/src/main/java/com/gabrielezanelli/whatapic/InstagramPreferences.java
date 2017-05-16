package com.gabrielezanelli.whatapic;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

import butterknife.BindString;

public class InstagramPreferences {
    private SharedPreferences instagramPreferences;

    @BindString (R.string.instagram_preferences) String preferences;
    @BindString (R.string.preferences_user_id) String userId;
    @BindString (R.string.preferences_username) String username;
    @BindString (R.string.preferences_fullname) String fullName;
    @BindString (R.string.preferences_profile_pic) String profilePicture;
    @BindString (R.string.preferences_access_token) String accessToken;

    public InstagramPreferences(Context context) {
        instagramPreferences = context.getSharedPreferences(preferences, Context.MODE_PRIVATE);
    }

    public void saveSettings(InstagramUser user) {
        Editor editor = instagramPreferences.edit();

        editor.putString(accessToken, user.accessToken);
        editor.putString(userId, user.id);
        editor.putString(username, user.username);
        editor.putString(fullName, user.fullName);
        editor.putString(profilePicture, user.profilePicture);

        editor.apply();
    }

    public void deleteSettings() {
        Editor editor = instagramPreferences.edit();

        editor.putString(accessToken, "");
        editor.putString(userId, "");
        editor.putString(username, "");
        editor.putString(fullName, "");
        editor.putString(profilePicture, "");

        editor.apply();
    }

    public InstagramUser getUser() {
        if (instagramPreferences.getString(accessToken, "").equals("")) {
            return null;
        }

        InstagramUser user 	= new InstagramUser();

        user.id	= instagramPreferences.getString(userId, "");
        user.username = instagramPreferences.getString(username, "");
        user.fullName = instagramPreferences.getString(fullName, "");
        user.profilePicture	= instagramPreferences.getString(profilePicture, "");
        user.accessToken = instagramPreferences.getString(accessToken, "");

        return user;
    }

    public String getAccessToken() {
        return instagramPreferences.getString(accessToken, "");
    }


    public boolean isActive() {
        return !instagramPreferences.getString(accessToken, "").equals("");
    }
}