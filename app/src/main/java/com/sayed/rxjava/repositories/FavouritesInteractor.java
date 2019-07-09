package com.sayed.rxjava.repositories;

import com.sayed.rxjava.app.AppController;
import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.realm.RealmMovie;
import com.sayed.rxjava.room_db.EntityMovies;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class FavouritesInteractor {

    //get fav movies from offline db
    public void getFavMovies(FavListener favListener){
        RealmResults<RealmMovie> realmList=AppController.getRealm().where(RealmMovie.class).findAll();
        if (realmList.size()!=0){
            List<Movie> movieList=new ArrayList<>();
            for (RealmMovie entityMovies:realmList){
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
        RealmMovie realmObj= AppController.getRealm().where(RealmMovie.class).equalTo("movie_id",movie_id).findFirst();
        if (realmObj!=null){
            AppController.getRealm().beginTransaction();
            realmObj.deleteFromRealm();
            AppController.getRealm().commitTransaction();
        }
    }

    /*
     * fav interactor interface
     */
    public interface FavListener{
        void onSuccess(List<Movie> moviesResult);
        void noData();
    }
}
