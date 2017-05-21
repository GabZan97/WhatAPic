package com.gabrielezanelli.whatapic;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

/**
 * Main activity containing SignIn and Gallery fragments
 */

public class MainActivity extends AppCompatActivity {

    public static InstagramUser instagramUser = new InstagramUser();
    private InstagramPreferences instagramPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instagramPreferences = new InstagramPreferences(this);

        if(instagramPreferences.loadPreferences())
            getSupportFragmentManager().beginTransaction().add(
                    R.id.fragment_container, new GalleryFragment()).
                    setCustomAnimations(R.anim.fade_in,0).commit();
        else
            getSupportFragmentManager().beginTransaction().add(
                    R.id.fragment_container, new SignInFragment()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        instagramPreferences.savePreferences();
    }

    public void logout(){
        instagramPreferences.deletePreferences();
        instagramUser.deleteInformations();

        deleteCache(this);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                .replace(R.id.fragment_container, new SignInFragment())
                .commit();
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDirectory(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDirectory(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDirectory(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
