package com.sayed.rxjava.presenter;

import com.sayed.rxjava.model.MoviesResult;
import com.sayed.rxjava.repositories.MoviesInteractor;
import com.sayed.rxjava.ui.home.MoviesView;

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
        moviesView.addItems(moviesResult.getResults(),moviesResult.getPage(),moviesResult.getTotal_pages());
    }

    @Override
    public void onFail() {
        moviesView.onFail();
    }
}
