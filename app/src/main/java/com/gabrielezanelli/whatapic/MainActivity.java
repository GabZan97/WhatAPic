package com.gabrielezanelli.whatapic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.squareup.okhttp.HttpUrl;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Starting activity containing elements for obtaining user's access to instagram's account
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.button_sign_in) ImageButton buttonSignIn;
    @BindString (R.string.client_id) String clientId;
    @BindString (R.string.redirect_uri) String redirectUri;
    @BindString (R.string.response_type) String responseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptInstagramAuthorization();
            }
        });
    }

    private void attemptInstagramAuthorization() {
        String authenticationUrl = buildAuthUrl();

        InstagramDialog instagramDialog = new InstagramDialog(this, authenticationUrl, redirectUri, new InstagramDialog.InstagramDialogListener() {
            @Override
            public void onSuccess(String code) {
                System.out.print("Success! The token is: "+code+"\n");
                // TODO: Store Token
            }

            @Override
            public void onCancel() {
                System.out.print("Cancelled\n");
            }

            @Override
            public void onError(String error) {
                System.out.print("Error: "+error+"\n");
            }
        });

        instagramDialog.show();

    }

    private String buildAuthUrl(){
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.instagram.com/oauth/authorize").newBuilder();
        urlBuilder.addQueryParameter("client_id", clientId);
        urlBuilder.addQueryParameter("redirect_uri", redirectUri);
        urlBuilder.addQueryParameter("response_type", responseType);

        return urlBuilder.build().toString();
    }

}
