package com.example.shivam.popularmovies2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shivam.popularmovies2.R;
import com.example.shivam.popularmovies2.data.Trailer;

import java.util.ArrayList;

/**
 * Created by shivam on 11/07/16.
 */
public class TrailerAdapter extends BaseAdapter {

    ArrayList<Trailer> mTrailers;

    private Context mContext;

    public TrailerAdapter(Context context, ArrayList<Trailer> input) {
        mTrailers = input;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTrailers.size();
    }

    @Override
    public Object getItem(int position) {
        return mTrailers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        //ImageView icon;
        TextView trailerNameView;
        TextView trailerTypeView;

        ViewHolder(View view) {
            trailerNameView = (TextView) view.findViewById(R.id.trailer_name);
            trailerTypeView = (TextView) view.findViewById(R.id.trailer_type);
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listview_trailers_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Trailer trailer = mTrailers.get(position);
        if(trailer != null) {
            viewHolder.trailerNameView.setText(trailer.getTrailerName());
            viewHolder.trailerTypeView.setText(trailer.getTrailerType());
        }
        else
            return null;

        return view;
    }
}
