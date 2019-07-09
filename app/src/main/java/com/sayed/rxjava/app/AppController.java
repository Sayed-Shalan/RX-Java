package com.sayed.rxjava.app;

import android.app.Application;
import android.arch.persistence.room.Room;


import com.sayed.rxjava.BuildConfig;
import com.sayed.rxjava.manager.RealmManager;
import com.sayed.rxjava.room_db.AppDatabase;
import com.sayed.rxjava.room_db.DAOMovies;
import com.sayed.rxjava.utils.AppUtils;

import io.realm.Realm;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/** Application class **/
public class AppController extends Application {

    //Dec Fields
    static Retrofit retrofit;
    static int cache_size=10*1024*1024; // 10 mb

    //ROOM Fields
    AppDatabase appDatabase;
    static DAOMovies daoMovies;

    //REALM Fields
    static Realm realm;

    /** On App Created **/
    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofitInstance(); //init retrofit obj
        getAppDatabase(); //init room
        RealmManager.initialize(getApplicationContext());
        setRealm(); //define realm object

//        FacebookSdk.sdkInitialize(getApplicationContext());//init facebook sdk
//        AppEventsLogger.activateApp(this);
    }

    //prepare singleton for retrofit
    private void initRetrofitInstance() {
        retrofit=new Retrofit.Builder().
                baseUrl(BuildConfig.HOST).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(getClient()).
                build();
    }

    //get okhttp client with cache for 4 weeks - and cache size 10 mb
    private OkHttpClient getClient(){
        Cache cache=new Cache(getCacheDir(),cache_size);
        return new OkHttpClient.Builder().cache(cache).addInterceptor(chain -> {
            Request request=chain.request();
            if (!AppUtils.isNetworkAvailable(getApplicationContext())){
                int maxStale=60*60*24*28; //4 weeks
                request=request.newBuilder().addHeader("Cache-Control","public, only-if-cached, max-stale="+maxStale).build();
            }
            return chain.proceed(request);


        }).build();
    }

    //return retrofit
    public static Retrofit getRetrofit(){return retrofit;}

    //return app Database
    public void getAppDatabase(){
        appDatabase= Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();
        daoMovies=appDatabase.getMoviesDAO();
    }

    //get dao movies
    public static DAOMovies getDAOMovies(){
        return daoMovies;
    }

    //set realm
    private void setRealm(){
        realm=Realm.getDefaultInstance();
    }
    //get realm object
    public static Realm getRealm(){
        return realm;
    }

}
