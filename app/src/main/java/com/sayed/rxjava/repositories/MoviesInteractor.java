package com.sayed.rxjava.repositories;


import com.sayed.rxjava.app.AppController;
import com.sayed.rxjava.model.MoviesResult;
import com.sayed.rxjava.services.MoviesService;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;


public class MoviesInteractor {

    //get movies
    public Subscription getMovies(int page, MoviesListener moviesListener){
        MoviesService moviesService= AppController.getRetrofit().create(MoviesService.class);
        return moviesService.getMovies("a3bbc8ba7c5d94e0cd5d7cb994af8c62",page).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                onErrorResumeNext(Observable::error)
                .subscribe(new Subscriber<MoviesResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        moviesListener.onFail();
                    }

                    @Override
                    public void onNext(MoviesResult moviesResult) {
                        moviesListener.onSuccess(moviesResult);
                    }
                });

    }

    /*
     * Movies interactor interface
     */
    public interface MoviesListener{
        void onSuccess(MoviesResult moviesResult);
        void onFail();
    }
}
