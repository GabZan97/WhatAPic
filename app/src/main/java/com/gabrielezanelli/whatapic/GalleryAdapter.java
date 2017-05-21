package com.gabrielezanelli.whatapic;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Recycler view adapter for gallery item
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private ArrayList<String> thumbnailUrls;
    private ArrayList<String> photoUrls;

    public GalleryAdapter() {
        thumbnailUrls = new ArrayList<>();
        photoUrls = new ArrayList<>();
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_recycler_view, parent, false);
        return new ViewHolder(layoutView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Picasso.with(viewHolder.context).load(thumbnailUrls.get(position)).into(viewHolder.imageItem);
    }

    @Override
    public int getItemCount() {
        return thumbnailUrls.size();
    }

    public void addUrls(String thumbnailUrl, String photoUrl) {
        thumbnailUrls.add(thumbnailUrl);
        photoUrls.add(photoUrl);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageItem;
        private Context context;

        public ViewHolder(View layoutView, final Context context) {
            super(layoutView);
            this.context = context;

            imageItem = (ImageView) layoutView.findViewById(R.id.imageItem);

            imageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullScreenImage(getLayoutPosition());
                }
            });
        }

        /**
         * Display a dialog with dark transparent background and the fullscreen image in the centre
         * @param itemPosition the position of the image to display
         */
        private void showFullScreenImage(int itemPosition) {

            final Dialog imageDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            imageDialog.setCancelable(true);
            imageDialog.setContentView(R.layout.full_screen_image);

            ImageView fullScreenImage = (ImageView) imageDialog.findViewById(R.id.imageDialog);
            final ProgressBar loadingBar = (ProgressBar) imageDialog.findViewById(R.id.loadingBar);

            fullScreenImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageDialog.dismiss();
                }
            });
            imageDialog.show();

            loadingBar.setVisibility(View.VISIBLE);

            Picasso.with(context).load(photoUrls.get(itemPosition)).into(fullScreenImage,
                    new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            loadingBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError() {
                            loadingBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }
}
