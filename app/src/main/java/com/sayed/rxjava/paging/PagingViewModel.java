package com.sayed.rxjava.paging;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import com.sayed.rxjava.model.Movie;

public class PagingViewModel extends ViewModel {

    //creating livedata for PagedList  and PagedKeyedDataSource
    public LiveData<PagedList<Movie>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Movie>> liveDataSource;

    //constructor
    public PagingViewModel() {
        //getting our data source factory
        MovieDataSourceFactory itemDataSourceFactory = new MovieDataSourceFactory();

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setPageSize(20)
                        .setEnablePlaceholders(false).build();

        //Building the paged list
        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig))
                .build();
    }
}