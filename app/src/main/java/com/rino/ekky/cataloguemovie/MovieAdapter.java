package com.rino.ekky.cataloguemovie;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {
    private ArrayList<MovieItems> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<MovieItems> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addItem(final MovieItems item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        data.clear();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getCount() {
        if(data == null)
            return 0;
        return data.size();
    }

    @Override
    public MovieItems getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.movie_items, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_poster);
            holder.tvJudul = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tvRating = (TextView) convertView.findViewById(R.id.tv_rating);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context)
                .load(data.get(position).getImagePath())
                .into(holder.imageView);
        holder.tvJudul.setText(data.get(position).getJudul());
        holder.tvDate.setText(data.get(position).getReleaseDate());
        holder.tvRating.setText(data.get(position).getRating());
        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView tvJudul;
        TextView tvDate;
        TextView tvRating;
    }
}
