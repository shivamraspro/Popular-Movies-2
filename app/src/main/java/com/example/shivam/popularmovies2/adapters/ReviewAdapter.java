package com.example.shivam.popularmovies2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shivam.popularmovies2.R;
import com.example.shivam.popularmovies2.data.Review;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

/**
 * Created by shivam on 11/07/16.
 */
public class ReviewAdapter extends BaseAdapter {
    ArrayList<Review> mReviews;

    private Context mContext;

    public ReviewAdapter(Context context, ArrayList<Review> input) {
        mReviews = input;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Object getItem(int position) {
        return mReviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView authorNameView;
        ExpandableTextView contentView;

        ViewHolder(View view) {
            authorNameView = (TextView) view.findViewById(R.id.author_name);
            contentView = (ExpandableTextView) view.findViewById(R.id.expand_text_view);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_reviews_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Review review = mReviews.get(position);
        if(review != null) {
            viewHolder.authorNameView.setText(review.getAuthorName());
            viewHolder.contentView.setText(review.getReviewContent());
        }
        else
            return null;

        return view;
    }
}
