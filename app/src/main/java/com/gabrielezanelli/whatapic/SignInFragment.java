package com.gabrielezanelli.whatapic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment containing welcome message and invite to sign-in
 */

public class SignInFragment extends Fragment {

    @BindView(R.id.button_sign_in)
    ImageButton buttonSignIn;

    public SignInFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptInstagramWebAuthorization();
            }
        });

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } catch (NullPointerException ex) {
            Toast.makeText(container.getContext(),R.string.action_bar_fail_show, Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    /**
     * Display a dialog containing the instagram WebView for authorizing the application
     */
    private void attemptInstagramWebAuthorization() {
        final InstagramRequestManager instagramRequestManager = new InstagramRequestManager(getActivity());
        String authUrl = instagramRequestManager.getAuthenticationUrl();
        String redirectUri = instagramRequestManager.getRedirectUri();

        InstagramDialog instagramDialog = new InstagramDialog(getActivity(), authUrl,
                redirectUri, new InstagramDialog.InstagramDialogListener() {
            @Override
            public void onSuccess(String token) {
                System.out.print("Success! The token is: " + token + "\n");

                instagramRequestManager.requestUserInformation(token);

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right,0)
                        .replace(R.id.fragment_container, new GalleryFragment()).commit();
            }

            @Override
            public void onCancel() {
                System.out.print("Cancelled\n");
            }

            @Override
            public void onError(String error) {
                System.out.print("Error: " + error + "\n");
            }
        });

        instagramDialog.show();
    }
}
