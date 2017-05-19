package com.gabrielezanelli.whatapic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Recycler view adapter for gallery item
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{
    private ArrayList<String> photoUrls;

    public GalleryAdapter() {
        photoUrls = new ArrayList<>();
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery_recycler_view, parent,false);

        ViewHolder viewHolder = new ViewHolder(layoutView,parent.getContext());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Picasso.with(viewHolder.context).load(photoUrls.get(position)).into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return photoUrls.size();
    }

    public void addUrl(String url){
        photoUrls.add(url);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private Context context;

        public ViewHolder(View layoutView, Context context) {
            super(layoutView);
            this.context = context;
            imageView = (ImageView) layoutView.findViewById(R.id.imageView);
        }
    }

}
