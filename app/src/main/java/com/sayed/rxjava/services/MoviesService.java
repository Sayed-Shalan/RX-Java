package com.sayed.rxjava.services;


import com.sayed.rxjava.model.MoviesResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MoviesService {

    @GET("3/discover/movie?sort_by=popularity.desc")
    Observable<MoviesResult> getMovies(@Query(value = "api_key") String apiKey, @Query("page") int page);
}
