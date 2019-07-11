package com.sayed.rxjava.ui.home;

import android.arch.paging.PagedListAdapter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.sayed.rxjava.R;
import com.sayed.rxjava.databinding.SingleItemMovieBinding;
import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.ui.base.DataBindingViewHolder;

import java.util.List;

public class PagedMoviesAdapter extends PagedListAdapter<Movie, PagedMoviesAdapter.PagedMoviesViewHolder> {


    //dec data
    private AdapterMovies.MoviesCallback listener;

    //constructor to init data
    public PagedMoviesAdapter(AdapterMovies.MoviesCallback listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public PagedMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new PagedMoviesViewHolder(LayoutInflater.from(parent.getContext()),parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PagedMoviesViewHolder holder, int position) {
        holder.onBind(getItem(position));
    }


    private static DiffUtil.ItemCallback<Movie> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Movie>() {
                @Override
                public boolean areItemsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
                    return oldItem.equals(newItem);
                }
            };


    /**
     * Create View holder
     */
    public class PagedMoviesViewHolder extends DataBindingViewHolder<SingleItemMovieBinding> {

        //constructor
        public PagedMoviesViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater, parent, R.layout.single_item_movie);
        }

        //On Bind
        void onBind(Movie movie){

            //set image with caching
            Glide.with(getContext())
                    .load("http://image.tmdb.org/t/p/w185".concat(movie.getPoster_path()))
                    .into(binding.posterImage);

            binding.posterFavouriteButton.setColorFilter(movie.isFavourite()? Color.YELLOW:Color.GRAY); //set fav btn color
            binding.posterFavouriteButton.setOnClickListener(v -> {
                binding.posterFavouriteButton.setColorFilter(movie.isFavourite()?Color.GRAY:Color.YELLOW);
                getItem(getAdapterPosition()).setFavourite(!movie.isFavourite());
                listener.onFavouriteClick(getItem(getAdapterPosition()),getAdapterPosition());

            });

        }
    }
}
