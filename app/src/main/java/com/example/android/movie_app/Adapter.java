package com.example.android.movie_app;

import android.content.Context;
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
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private List<ItemModel> mItemModels;
    private Context mContext;
    private PosterListener mListener;
    public Adapter(Context context, List<ItemModel> itemModels,PosterListener posterListener) {
        mItemModels = itemModels;
        mContext = context;
        mListener=posterListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        List<ItemModel> list;
        Context context;
        private PosterListener mListener;

        public ViewHolder(View itemView, Context context, List<ItemModel> list, PosterListener mListener) {
            super(itemView);
            this.list = list;
            this.context = context;
            this.mListener=mListener;
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
            mListener.setSelectedMovie(itemModel.getMovieId());
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder phv = new ViewHolder(v,mContext,mItemModels,mListener);
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
