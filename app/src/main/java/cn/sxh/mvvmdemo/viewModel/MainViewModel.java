package cn.sxh.mvvmdemo.viewModel;

import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import cn.sxh.mvvmdemo.adapter.MovieAdapter;
import cn.sxh.mvvmdemo.model.Movie;
import cn.sxh.mvvmdemo.network.RetrofitHelper;
import cn.sxh.mvvmdemo.view.CompletedListener;
import rx.Subscriber;

/**
 * @auther snowTiger
 * @mail SnowTigerSong@gmail.com
 * @time 2017/4/29 21:11
 */

public class MainViewModel {
    public ObservableField<Integer> contentViewVisibility;
    public ObservableField<Integer> progressBarVisibility;
    public ObservableField<Integer> errorInfoLayoutVisibility;
    public ObservableField<String> exception;
    private Subscriber<Movie> subscriber;
    private MovieAdapter movieAdapter;
    private CompletedListener completedListener;

    public MainViewModel(MovieAdapter movieAdapter,CompletedListener completedListener) {
        this.movieAdapter = movieAdapter;
        this.completedListener = completedListener;
        initData();
    }
    private void initData() {
        contentViewVisibility = new ObservableField<>();
        progressBarVisibility = new ObservableField<>();
        errorInfoLayoutVisibility = new ObservableField<>();
        exception = new ObservableField<>();
        contentViewVisibility.set(View.GONE);
        errorInfoLayoutVisibility.set(View.GONE);
        progressBarVisibility.set(View.VISIBLE);
        getMovies();
    }

    private void getMovies() {
        subscriber = new Subscriber<Movie>() {
            @Override
            public void onCompleted() {
                Log.e("[MainViewModel]", "onCompleted");
                hideAll();
                contentViewVisibility.set(View.VISIBLE);
                completedListener.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                hideAll();
                errorInfoLayoutVisibility.set(View.VISIBLE);
                exception.set(e.getMessage());
            }

            @Override
            public void onNext(Movie movie) {
                movieAdapter.addItem(movie);
            }
        };
        RetrofitHelper.getInstance().getMovies(subscriber, 0, 20);
    }

    public void refreshData() {
        getMovies();
    }

    private void hideAll(){
        contentViewVisibility.set(View.GONE);
        errorInfoLayoutVisibility.set(View.GONE);
        progressBarVisibility.set(View.GONE);
    }
}
