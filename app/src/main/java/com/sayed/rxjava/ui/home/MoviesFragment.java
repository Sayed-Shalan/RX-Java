package com.sayed.rxjava.ui.home;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sayed.rxjava.R;
import com.sayed.rxjava.databinding.FragmentMoviesBinding;
import com.sayed.rxjava.manager.BroadcastManager;
import com.sayed.rxjava.model.Movie;
import com.sayed.rxjava.paging.PagingViewModel;
import com.sayed.rxjava.presenter.MoviesPresenter;
import com.sayed.rxjava.repositories.MoviesInteractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MoviesFragment extends Fragment implements MoviesView{

    //dec data
    FragmentMoviesBinding binding;
    AdapterMovies adapterMovies;
    List<Movie> items;
    int page=0;
    int total_pages=1;
    MoviesPresenter moviesPresenter;
    PagingViewModel pagingViewModel;
    PagedMoviesAdapter pagedMoviesAdapter;


    //Swipe refresh Listener
    SwipeRefreshLayout.OnRefreshListener onRefreshListener= () -> {
        adapterMovies.clearItems();
       // moviesPresenter.getMovies(1);
    };

    //on fav click
    AdapterMovies.MoviesCallback moviesCallback= (movie, position) -> {
        if (movie.isFavourite()) moviesPresenter.addMovieToOfflineDB(movie);
        else moviesPresenter.deleteMovieFromOfflineDB(movie.getId());

        sendMovieBroadCast(movie); //to notify fav frag,ent with updates
    };


    //to handle if recycler view reached its bottom - handle pagination
    RecyclerView.OnScrollListener onScrollListener=new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //If scrolled at last then
            if (isLastItemDisplaying(recyclerView)) {
                //Calling the method get data again
                getNextPage();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //Log.v("RECYCLER :"," ON SCROLLED ");
        }
    };

    //register receiver movie removed from fav fragment
    private BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Movie movie=intent.getParcelableExtra(BroadcastManager.KEY_MOVIE_DELETED);
            if (movie!=null){
                removeFav(movie);
            }
        }
    };

    /**
     * ON CREATE VIEW
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_movies, container, false);
        return binding.getRoot();
    }

    /**
     * ON VIEW CREATED
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();//init Data
    }

    /**
     * ON PAUSE
     */
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).unregisterReceiver(mMessageReceiver);

    }

    /**
     * On STOP
     */
    @Override
    public void onStop() {
        super.onStop();
        moviesPresenter.onStop();
    }


    //init Data
    private void initData() {
        items=new ArrayList<>();
        binding.swipeRefresh.setOnRefreshListener(onRefreshListener);
        setupAdapter();
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).registerReceiver(mMessageReceiver,
                new IntentFilter(BroadcastManager.ACTION_MOVIE_REMOVED));
//        moviesPresenter=new MoviesPresenter(this,new MoviesInteractor());
//        moviesPresenter.getMovies(1);
    }

    //setup adapter
    private void setupAdapter() {
        items.clear();
        adapterMovies=new AdapterMovies(moviesCallback,items);
        pagedMoviesAdapter=new PagedMoviesAdapter(moviesCallback);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.recyclerView.setHasFixedSize(true);

        //getting our ItemViewModel
        pagingViewModel = ViewModelProviders.of(this).get(PagingViewModel.class);

        //creating the Adapter

        //observing the itemPagedList from view model
        pagingViewModel.itemPagedList.observe(this, items -> {

            //in case of any changes
            //submitting the items to adapter
            pagedMoviesAdapter.submitList(items);
        });

        //setting the adapter
        binding.recyclerView.setAdapter(pagedMoviesAdapter);
    }

    //This method would check that the recycler view scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((GridLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    //request the next page
    private void getNextPage(){
        if (page<total_pages){
            page++;
            moviesPresenter.getMovies(page);
        }
    }

    //Send Movie Broadcast
    private void sendMovieBroadCast(Movie movie) {
        Intent intent = new Intent(BroadcastManager.ACTION_MOVIE_FAV_CLICK);
        intent.putExtra(BroadcastManager.KEY_MOVIE_OBJECT, movie);
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getActivity())).sendBroadcast(intent);
    }

    //remove Fav
    private void removeFav(Movie movie) {
        for (Movie object:items){
            if (object.getId()==movie.getId()){
                object.setFavourite(false);
                break;
            }
        }
        adapterMovies.refresh(items);
    }

    /**
     *  VIEW CALLBACK METHODS
     */
    @Override
    public void addItems(List<Movie> moviesList, int pageNum, int totalPages) {
        total_pages=totalPages;
        page=pageNum;
        binding.swipeRefresh.setRefreshing(false);
        adapterMovies.add(moviesList);
    }

    @Override
    public void onFail() {
        binding.swipeRefresh.setRefreshing(false);
//        showError(getResources().getString(R.string.error_occurred));
        Toast.makeText(getActivity(),getResources().getString(R.string.error_occurred),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showLoading() {
        binding.swipeRefresh.setRefreshing(true);
    }
}
