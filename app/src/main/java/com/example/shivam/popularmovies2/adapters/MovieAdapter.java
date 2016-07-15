package com.example.shivam.popularmovies2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.shivam.popularmovies2.R;
import com.example.shivam.popularmovies2.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shivam on 25/02/16.
 */

public class MovieAdapter extends BaseAdapter {

    private String LOG_TAG = MovieAdapter.class.getSimpleName();

    ArrayList<Movie> mMovies;

    private Context mContext;

    public MovieAdapter(Context context, ArrayList<Movie> input) {
        mMovies = input;
        mContext = context;
    }


    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        ImageView imageView;

        ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.griditem_poster_imageview);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.griditem_poster, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            Picasso.with(mContext)
                    .load(mMovies.get(position).getPosterURL())
                    .error(R.drawable.ic_error)
                    .into(viewHolder.imageView);
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return viewHolder.imageView;

    }

}
