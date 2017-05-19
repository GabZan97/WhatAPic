package com.gabrielezanelli.whatapic;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

import butterknife.BindString;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

/**
 * Class for managing user's information in android shared preferences
 */

public class InstagramPreferences {
    private static SharedPreferences instagramPreferences;

    @BindString (R.string.instagram_preferences) String preferences;
    @BindString (R.string.preferences_user_id) String userId;
    @BindString (R.string.preferences_username) String username;
    @BindString (R.string.preferences_fullname)  String fullName;
    @BindString (R.string.preferences_profile_pic) String profilePicture;
    @BindString (R.string.preferences_access_token) String accessToken;

    public InstagramPreferences(Context context) {
        instagramPreferences = context.getSharedPreferences(preferences, Context.MODE_PRIVATE);
    }

    public void savePreferences() {
        InstagramUser user = instagramUser;
        if (user != null) {
            Editor editor = instagramPreferences.edit();

            editor.putString(accessToken, user.accessToken);
            editor.putString(userId, user.id);
            editor.putString(username, user.username);
            editor.putString(fullName, user.fullName);
            editor.putString(profilePicture, user.profilePictureUrl);

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

    public InstagramUser getUserFromPreferences() {
        if (instagramPreferences.getString(accessToken, "").equals("")) {
            return null;
        }

        InstagramUser user 	= new InstagramUser();

        user.id	= instagramPreferences.getString(userId, "");
        user.username = instagramPreferences.getString(username, "");
        user.fullName = instagramPreferences.getString(fullName, "");
        user.profilePictureUrl = instagramPreferences.getString(profilePicture, "");
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