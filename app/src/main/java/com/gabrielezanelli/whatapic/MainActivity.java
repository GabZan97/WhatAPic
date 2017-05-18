package com.gabrielezanelli.whatapic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static InstagramUser instagramUser = new InstagramUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        InstagramPreferences instagramPreferences = new InstagramPreferences(this);

        getSupportActionBar().hide();

        if (savedInstanceState == null)
            getFragmentManager().beginTransaction().add(
                    R.id.fragment_container,new SignInFragment()).commit();

    }

}
