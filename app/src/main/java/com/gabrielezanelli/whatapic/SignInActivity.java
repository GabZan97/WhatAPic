package com.gabrielezanelli.whatapic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Starting activity containing elements for obtaining user's access to instagram's account
 */

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button buttonSignIn = (Button) findViewById(R.id.button_sign_in);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptInstagramAuthorization();
            }
        });
    }

    private void attemptInstagramAuthorization() {
        HttpManager.instagramAuth();
    }


}
