package com.gabrielezanelli.whatapic;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Recycler view adapter for gallery item
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{
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
                .inflate(R.layout.item_gallery_recycler_view, parent,false);
        return new ViewHolder(layoutView,parent.getContext());
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        System.out.println("Binding view n° "+position);
        Picasso.with(viewHolder.context).load(thumbnailUrls.get(position)).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return thumbnailUrls.size();
    }

    public void addUrl(String thumbnailUrl, String photoUrl){
        thumbnailUrls.add(thumbnailUrl);
        photoUrls.add(photoUrl);

        System.out.println("Adding "+ thumbnailUrls.size() +"° url: "+thumbnailUrl);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private Context context;

        public ViewHolder(View layoutView, final Context context) {
            super(layoutView);
            this.context = context;
            imageView = (ImageView) layoutView.findViewById(R.id.imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFullScreenImage(getLayoutPosition());
                }
            });
        }

        private void showFullScreenImage(int itemPosition) {
            Dialog imageDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            imageDialog.setCancelable(true);
            imageDialog.setContentView(R.layout.dialog_full_image);
            imageView = (ImageView)imageDialog.findViewById(R.id.imageViewDialog);
            Picasso.with(context).load(photoUrls.get(itemPosition)).placeholder(R.drawable.progress_animation).into(imageView);
            imageDialog.show();
        }
    }

}
