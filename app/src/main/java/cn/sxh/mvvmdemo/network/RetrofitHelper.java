package cn.sxh.mvvmdemo.network;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.sxh.mvvmdemo.model.API_Service;
import cn.sxh.mvvmdemo.model.entity.Movie;
import cn.sxh.mvvmdemo.model.entity.Response;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @auther snowTiger
 * @mail SnowTigerSong@gmail.com
 * @time 2017/4/28 21:44
 */

public class RetrofitHelper {
    private static final int DEFAULT_TIMEOUT = 10;
    private Retrofit mRetrofit;
    private API_Service mApiService;
    private OkHttpClient.Builder builder;

    private static class Singleton{
        private static final RetrofitHelper instance = new RetrofitHelper();
    }
    public static RetrofitHelper getInstance(){
        return Singleton.instance;
    }

    private Retrofit getRetrofit(){
        builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(API_Service.BASE_URL)
                .build();
        return mRetrofit;
    }

    private API_Service getApiService(){
        mApiService = getRetrofit().create(API_Service.class);
        return mApiService;
    }

    public void getMovies(Subscriber<Movie> subscriber,int start,int count){
        getApiService().getMovies(start,count)
                .map(new Func1<Response<List<Movie>>, List<Movie>>() {
                    @Override
                    public List<Movie> call(Response<List<Movie>> listResponse) {
                        return listResponse.getSubjects();
                    }
                })
                .flatMap(new Func1<List<Movie>, Observable<Movie>>() {
                    @Override
                    public Observable<Movie> call(List<Movie> movies) {
                        return Observable.from(movies);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
