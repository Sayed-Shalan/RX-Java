 package com.sayed.rxjava.presenter;

import com.sayed.rxjava.app.AppController;
import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.model.MoviesResult;
import com.sayed.rxjava.repositories.MoviesInteractor;
import com.sayed.rxjava.room_db.EntityMovies;
import com.sayed.rxjava.ui.home.MoviesView;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MoviesPresenter implements MoviesInteractor.MoviesListener {

    //Dec Data
    private MoviesView moviesView;
    private MoviesInteractor moviesInteractor;
    private CompositeSubscription subscriptions;

    //Constructor
    public MoviesPresenter(MoviesView moviesView, MoviesInteractor moviesInteractor) {
        this.moviesView = moviesView;
        this.moviesInteractor = moviesInteractor;
        this.subscriptions = new CompositeSubscription();
    }

    //get movies
    public void getMovies(int page){
        moviesView.showLoading();
        Subscription subscription=moviesInteractor.getMovies(page,this);
        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
    /**
     * Response Callback
     */
    @Override
    public void onSuccess(MoviesResult moviesResult) {

        if (moviesResult==null) return;
        checkFavList(moviesResult.getResults(),moviesResult.getPage(),moviesResult.getTotal_pages());

    }

    //check if movie in favourite
    private void checkFavList(List<Movie> results, int page, int total_pages){

        for (Movie movie:results){
            if (AppController.getDAOMovies().getMovieById(movie.getId()).size()!=0){
                EntityMovies entityMovies=AppController.getDAOMovies().getMovieById(movie.getId()).get(0);
                if (entityMovies!=null&&movie.getId()==entityMovies.getMovie_id()) movie.setFavourite(true);
                else movie.setFavourite(false);
            }else {
                movie.setFavourite(false);
            }

        }
        moviesView.addItems(results,page,total_pages);
    }

    @Override
    public void onFail() {
        moviesView.onFail();
    }

    //Insert a movie into room db
    public void addMovieToOfflineDB(Movie movie) {
        EntityMovies entityMovies=new EntityMovies();
        entityMovies.setMovie_id(movie.getId());
        entityMovies.setMovie_title(movie.getTitle());
        entityMovies.setPoster_path(movie.getPoster_path());
        AppController.getDAOMovies().insertMovie(entityMovies);
    }

    //Delete a movie from db
    public void deleteMovieFromOfflineDB(int id) {
        AppController.getDAOMovies().deleteMovieById(id);
    }
}
