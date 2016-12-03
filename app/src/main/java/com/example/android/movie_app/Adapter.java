package com.example.android.movie_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amr on 10/22/2016.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<ItemModel> mItemModels;
    private Context mContext;
    public static boolean isTablet;
    public Adapter(Context context, List<ItemModel> itemModels) {
        mItemModels = itemModels;
        mContext = context;
        isTablet = isTablet();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        List<ItemModel> list;
        Context context;

        public ViewHolder(View itemView, Context context, List<ItemModel> list) {
            super(itemView);
            this.list = list;
            this.context = context;
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.movie_image);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            ItemModel itemModel=this.list.get(position);
                   if(isTablet) {
                    ((MainActivity)context).transact(itemModel.getMovieId());
                } else {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id",
                        itemModel.getMovieId());
                context.startActivity(intent);
            }
        }
    }

    public boolean isTablet() {
        return (mContext.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder phv = new ViewHolder(v,mContext,mItemModels);
        return phv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/" + "w500" +
                mItemModels.get(position).getImageViewUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mItemModels.size();
    }
    public void clearData() {
        int size = this.mItemModels.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mItemModels.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }
}
