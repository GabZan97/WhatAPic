package com.gabrielezanelli.whatapic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
            getFragmentManager().beginTransaction().add(
                    R.id.fragment_container, new GalleryFragment()).commit();
        else
            getFragmentManager().beginTransaction().add(
                    R.id.fragment_container, new SignInFragment()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        instagramPreferences.savePreferences();
    }
}
