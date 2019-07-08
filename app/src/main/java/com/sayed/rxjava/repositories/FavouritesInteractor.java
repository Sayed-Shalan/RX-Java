package com.sayed.rxjava.repositories;

import android.content.Context;

import com.sayed.rxjava.app.AppController;
import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.room_db.EntityMovies;

import java.util.ArrayList;
import java.util.List;

public class FavouritesInteractor {

    //get fav movies from offline db
    public void getFavMovies(FavListener favListener){
        List<EntityMovies> entityMoviesList= AppController.getDAOMovies().getItems(); // get all movies
        if (entityMoviesList.size()!=0){
            List<Movie> movieList=new ArrayList<>();
            for (EntityMovies entityMovies:entityMoviesList){
                Movie movie=new Movie();
                movie.setFavourite(true);
                movie.setId((int) entityMovies.getMovie_id());
                movie.setTitle(entityMovies.getMovie_title());
                movie.setPoster_path(entityMovies.getPoster_path());
                movieList.add(movie);
            }

            favListener.onSuccess(movieList);
        }else favListener.noData();

    }

    //delete movie from offline db
    public void deleteMovieFromOfflineDB(int movie_id){
        AppController.getDAOMovies().deleteMovieById(movie_id);
    }

    /*
     * fav interactor interface
     */
    public interface FavListener{
        void onSuccess(List<Movie> moviesResult);
        void noData();
    }
}
