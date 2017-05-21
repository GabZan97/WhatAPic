package com.gabrielezanelli.whatapic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindString;
import butterknife.ButterKnife;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

/**
 * Class for managing user's information in android shared preferences
 */

public class InstagramPreferences {
    private static SharedPreferences instagramPreferences;

    @BindString(R.string.instagram_preferences)
    public String preferences;
    @BindString(R.string.preferences_user_id)
    public String userId;
    @BindString(R.string.preferences_username)
    public String username;
    @BindString(R.string.preferences_fullname)
    public String fullName;
    @BindString(R.string.preferences_profile_pic)
    public String profilePicture;
    @BindString(R.string.preferences_access_token)
    public String accessToken;

    public InstagramPreferences(AppCompatActivity activity) {
        instagramPreferences = activity.getApplicationContext().getSharedPreferences(preferences, Context.MODE_PRIVATE);
        ButterKnife.bind(this,activity);
    }

    public void savePreferences() {
        if (instagramUser != null) {
            Editor editor = instagramPreferences.edit();

            editor.putString(accessToken, instagramUser.accessToken);
            editor.putString(userId, instagramUser.id);
            editor.putString(username, instagramUser.username);
            editor.putString(fullName, instagramUser.fullName);
            editor.putString(profilePicture, instagramUser.profilePictureUrl);

            editor.apply();
        }
    }

    public void deletePreferences() {
        Editor editor = instagramPreferences.edit();

        editor.putString(accessToken, "");
        editor.putString(userId, "");
        editor.putString(username, "");
        editor.putString(fullName, "");
        editor.putString(profilePicture, "");

        editor.apply();
    }

    public boolean loadPreferences() {
        if (isNew()) {
            return false;
        }
        instagramUser.id = instagramPreferences.getString(userId, "");
        instagramUser.username = instagramPreferences.getString(username, "");
        instagramUser.fullName = instagramPreferences.getString(fullName, "");
        instagramUser.profilePictureUrl = instagramPreferences.getString(profilePicture, "");
        instagramUser.accessToken = instagramPreferences.getString(accessToken, "");

        return true;
    }

    public boolean isNew() {
        return instagramPreferences.getString(accessToken, "").equals("");
    }
}