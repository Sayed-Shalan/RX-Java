package com.sayed.rxjava.presenter;



import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.repositories.FavouritesInteractor;
import com.sayed.rxjava.ui.home.FavouriteView;

import java.util.List;

public class FavouritePresenter implements FavouritesInteractor.FavListener {

    //dec data
    private FavouriteView favouriteView;
    private FavouritesInteractor favouritesInteractor;

    //constructor
    public FavouritePresenter(FavouriteView favouriteView, FavouritesInteractor favouritesInteractor) {
        this.favouriteView = favouriteView;
        this.favouritesInteractor = favouritesInteractor;
    }

    //get list from offline
    public void getFavMoviesList(){
        favouritesInteractor.getFavMovies(this);
    }

    //delete a movie from offline db
    public void deleteMovieFromOfflineDB(int movie_id){
        favouritesInteractor.deleteMovieFromOfflineDB(movie_id);
    }

    @Override
    public void onSuccess(List<Movie> moviesResult) {
        favouriteView.addItems(moviesResult);
    }

    @Override
    public void noData() {
        favouriteView.showNoData();
    }
}
