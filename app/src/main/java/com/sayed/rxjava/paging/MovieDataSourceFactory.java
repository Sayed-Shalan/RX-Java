package com.sayed.rxjava.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.repositories.MoviesInteractor;

public class MovieDataSourceFactory extends DataSource.Factory {

    //creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<Integer, Movie>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource create() {
        //getting our data source object
        MovieDataSource itemDataSource = new MovieDataSource(new MoviesInteractor());

        //posting the data source to get the values
        itemLiveDataSource.postValue(itemDataSource);

        //returning the data source
        return itemDataSource;
    }
    //getter for itemlivedatasource
    public MutableLiveData<PageKeyedDataSource<Integer, Movie>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
