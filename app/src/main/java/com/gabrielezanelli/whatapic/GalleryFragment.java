package com.gabrielezanelli.whatapic;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

/**
 * Fragment containing user's instagram photos and info
 */

public class GalleryFragment extends Fragment {

    @BindView(R.id.introductionText)
    TextView introView;
    @BindView(R.id.galleryRecyclerView)
    RecyclerView galleryRecyclerView;
    @BindString(R.string.welcome_text)
    String welcomeText;
    @BindString(R.string.help_text)
    String helpText;

    public GalleryFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        } catch (NullPointerException ex) {
            System.out.println("Failed to show action bar!\n");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);

        final GalleryAdapter galleryAdapter = new GalleryAdapter();
        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));

        new InstagramRequestManager(getActivity()).requestUserPhotos(galleryAdapter);

        return view;
    }

    @Override
    public void onResume() {
        String name = "";
        if(instagramUser.fullName != null)
            name = instagramUser.fullName;
        String introductionText = new StringBuilder().append(welcomeText).append(" ")
                .append(name).append("\n").append(helpText).toString();
        introView.setText(introductionText);

        super.onResume();
    }
}
