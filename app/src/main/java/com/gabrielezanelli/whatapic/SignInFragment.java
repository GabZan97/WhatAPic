package com.gabrielezanelli.whatapic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.squareup.okhttp.HttpUrl;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

/**
 * Fragment containing welcome message and invite to sign-in
 */

public class SignInFragment extends Fragment {

    @BindView(R.id.button_sign_in) ImageButton buttonSignIn;

    @BindString(R.string.instagram_api_authorization) String authorizationUrl;
    @BindString(R.string.client_id) String clientId;
    @BindString (R.string.redirect_uri) String redirectUri;
    @BindString (R.string.response_type) String responseType;

    public SignInFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this,view);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptInstagramWebAuthorization();
            }
        });

        return view;
    }

    private void attemptInstagramWebAuthorization() {
        String authenticationUrl = buildAuthUrl();


        InstagramDialog instagramDialog = new InstagramDialog(getActivity(), authenticationUrl, redirectUri, new InstagramDialog.InstagramDialogListener() {
            @Override
            public void onSuccess(String token) {
                System.out.print("Success! The token is: "+token+"\n");
                instagramUser.setAccessToken(token);

                new InstagramRequestManager(getActivity()).requestUserInformation();

                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new GalleryFragment()).commit();
                try {
                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                } catch (NullPointerException ex) {
                    System.out.println("Failed to show action bar!\n");
                }
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse(authorizationUrl).newBuilder();
        urlBuilder.addQueryParameter("client_id", clientId);
        urlBuilder.addQueryParameter("redirect_uri", redirectUri);
        urlBuilder.addQueryParameter("response_type", responseType);

        return urlBuilder.build().toString();
    }
}
