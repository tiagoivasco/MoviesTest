package com.ivasco.moviestest.data.repository

import com.ivasco.moviestest.data.model.MovieDetail
import com.ivasco.moviestest.data.model.MoviesResponse
import io.reactivex.Single

interface MoviesRemoteRepository {

    fun getPopularMovies(page: Int): Single<MoviesResponse>

    fun getSingleMovie(id: String): Single<MovieDetail>
}