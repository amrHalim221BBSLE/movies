package com.example.android.movie_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Amr on 11/28/2016.
 */
public class AdapterReviewList extends BaseAdapter{
    private final Context context;
    private final ArrayList<ReviewItem> reviews;

    public AdapterReviewList(Context context, ArrayList<ReviewItem> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        try {
            return reviews.size();
        } catch (NullPointerException e) {}
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View row=layoutInflater.inflate(R.layout.review_list_item, parent, false);
        ReviewItem review=reviews.get(position);
        TextView ReviewAuthor=(TextView)row.findViewById(R.id.author);
        ReviewAuthor.setText(review.getAuthor());
        TextView ReviewContent=(TextView)row.findViewById(R.id.content);
        ReviewContent.setText(review.getContent());
        return row;
    }


}
