package com.ivasco.moviestest.data.remote.service

import com.ivasco.moviestest.data.remote.model.RemoteMovieDetail
import com.ivasco.moviestest.data.remote.model.RemoteMoviesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<RemoteMoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<RemoteMoviesResponse>

    @GET("movie/{id}")
    fun getSingleMovie(@Path("id") id: String): Single<RemoteMovieDetail>

}