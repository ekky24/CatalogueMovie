package com.rino.ekky.cataloguemovie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardViewMovieAdapter extends RecyclerView.Adapter<CardViewMovieAdapter.CardViewViewHolder> {
    private ArrayList<MovieItems> listMovie;
    private Context context;

    public CardViewMovieAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<MovieItems> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<MovieItems> listMovie) {
        this.listMovie = listMovie;
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_items_cardview, parent, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewViewHolder holder, int position) {
        final MovieItems movieItems = getListMovie().get(position);

        Picasso.with(context)
                .load(movieItems.getImagePathBig())
                .into(holder.imgPoster);
        holder.tvTitle.setText(movieItems.getJudul());
        holder.tvReleaseDate.setText(movieItems.getReleaseDate());
        holder.btnDetail.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_MOVIE, movieItems);
                        ((Activity) context).startActivityForResult(intent, DetailActivity.REQUEST_FAVORITE);
                    }
                }));
        holder.btnShare.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        Toast.makeText(context, context.getResources().getString(R.string.share) + " " + movieItems.getJudul(),
                                Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    public class CardViewViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvReleaseDate;
        Button btnDetail, btnShare;

        public CardViewViewHolder(View itemView) {
            super(itemView);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_sub2);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title_sub2);
            tvReleaseDate = (TextView) itemView.findViewById(R.id.tv_release_date_sub2);
            btnDetail = (Button) itemView.findViewById(R.id.btn_detail_sub2);
            btnShare = (Button) itemView.findViewById(R.id.btn_share_sub2);
        }
    }
}
