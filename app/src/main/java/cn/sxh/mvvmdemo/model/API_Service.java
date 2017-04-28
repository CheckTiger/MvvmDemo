package cn.sxh.mvvmdemo.model;

import java.util.List;

import cn.sxh.mvvmdemo.model.entity.Movie;
import cn.sxh.mvvmdemo.model.entity.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @auther snowTiger
 * @mail SnowTigerSong@gmail.com
 * @time 2017/4/28 21:38
 */

public interface API_Service {
    String BASE_URL = "https://api.douban.com/v2/movie/";
    @GET("top250")
    Observable<Response<List<Movie>>> getMovies(@Query("start") int start, @Query("count") int count);
}
