package com.sayed.rxjava.room_db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {EntityMovies.class} , version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //get DAO
    public abstract DAOMovies getMoviesDAO();
}
