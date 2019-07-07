package com.sayed.rxjava.ui.home;


import com.sayed.rxjava.model.Movie;

import java.util.List;

public interface MoviesView {
    void addItems(List<Movie> moviesList, int pageNum, int totalPages); //new Items
    void onFail();//on fail to get items
    void showLoading(); //show swipe refresh
}
