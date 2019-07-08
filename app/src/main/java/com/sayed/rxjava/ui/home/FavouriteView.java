package com.sayed.rxjava.ui.home;


import com.sayed.rxjava.model.Movie;

import java.util.List;

public interface FavouriteView {
    void addItems(List<Movie> moviesList); //new Items
    void showNoData();//on zero items-list empty
}
