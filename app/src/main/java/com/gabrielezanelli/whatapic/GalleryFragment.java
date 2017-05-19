package com.gabrielezanelli.whatapic;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.gabrielezanelli.whatapic.MainActivity.instagramUser;

/**
 * Fragment containing user's instagram photos and info
 */

public class GalleryFragment extends Fragment {

    @BindView(R.id.uno) TextView textView;
    @BindView(R.id.galleryRecyclerView) RecyclerView galleryRecyclerView;

    public GalleryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this,view);

        final GalleryAdapter galleryAdapter = new GalleryAdapter();
        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));

        textView.setText("click me");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(instagramUser.fullName);
                new InstagramRequestManager(getActivity()).requestUserPhotos(galleryAdapter);
            }
        });

        return view;
    }



}