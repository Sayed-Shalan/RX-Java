package com.sayed.rxjava.paging;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.model.MoviesResult;
import com.sayed.rxjava.repositories.MoviesInteractor;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    //we will start from the first page which is 1
    private static final int FIRST_PAGE = 1;

    //repo
    private MoviesInteractor moviesInteractor;

    //total pages
    private int TOTAL_PAGES=0;

    public MovieDataSource(MoviesInteractor moviesInteractor) {
        this.moviesInteractor = moviesInteractor;
    }

    /**
     * Data Source Methods
     */
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        moviesInteractor.getMovies(FIRST_PAGE, new MoviesInteractor.MoviesListener() {
            @Override
            public void onSuccess(MoviesResult moviesResult) {
                if (moviesResult==null) return;
                TOTAL_PAGES=moviesResult.getTotal_pages();
                callback.onResult(moviesResult.getResults(),null,FIRST_PAGE+1);
            }

            @Override
            public void onFail() {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        moviesInteractor.getMovies(params.key, new MoviesInteractor.MoviesListener() {
            @Override
            public void onSuccess(MoviesResult moviesResult) {
                if (moviesResult==null) return;
                TOTAL_PAGES=moviesResult.getTotal_pages();
                Integer key = params.key<TOTAL_PAGES ? params.key + 1 : null;
                callback.onResult(moviesResult.getResults(),key);

            }

            @Override
            public void onFail() {

            }
        });
    }


}
