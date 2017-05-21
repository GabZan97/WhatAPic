package com.gabrielezanelli.whatapic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindInt;
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
    @BindInt(R.integer.photos_per_row)
    int photosPerRow;

    public GalleryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        } catch (NullPointerException ex) {
            Toast.makeText(getActivity().getApplicationContext(),R.string.action_bar_fail_hide,Toast.LENGTH_SHORT).show();
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);

        final GalleryAdapter galleryAdapter = new GalleryAdapter();
        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), photosPerRow));

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout_item) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        ((MainActivity)getActivity()).logout();

    }
}
