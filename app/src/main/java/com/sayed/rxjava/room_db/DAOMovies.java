package com.sayed.rxjava.room_db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DAOMovies {

    //Queries for Movies Table

    //insert a movie
    @Insert
     void insertMovie(EntityMovies... entityMovies);

    //Update a movie
    @Update
     void updateMovie(EntityMovies... entityMovies);

    //Delete a movie
    @Delete
     void deleteMovie(EntityMovies entityMovies);

    //get All Movies
    @Query("SELECT * FROM movies")
     List<EntityMovies> getItems();

    //get movie by id
    @Query("SELECT * FROM movies WHERE movie_id = :m_id")
     List<EntityMovies> getMovieById(long m_id);

    //delete a movie by id
    @Query("DELETE FROM movies WHERE movie_id= :mID")
     void deleteMovieById(long mID);
}
