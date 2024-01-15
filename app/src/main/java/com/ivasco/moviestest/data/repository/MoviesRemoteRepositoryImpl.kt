package com.ivasco.moviestest.data.repository

import com.ivasco.moviestest.data.model.MovieDetail
import com.ivasco.moviestest.data.model.MoviesResponse
import com.ivasco.moviestest.data.remote.api.ApiClient
import com.ivasco.moviestest.data.remote.mapper.MoviesRemoteMapper
import io.reactivex.Single

class MoviesRemoteRepositoryImpl(private val moviesRemoteMapper: MoviesRemoteMapper) : MoviesRemoteRepository  {

    override fun getPopularMovies(page: Int): Single<MoviesResponse> {
        return ApiClient.movieService().getPopularMovies(page).map {
            moviesRemoteMapper.mapFromRemote(it)
        }
    }

    override fun getSingleMovie(id: String): Single<MovieDetail> {
        return ApiClient.movieService().getSingleMovie(id).map {
            moviesRemoteMapper.mapDetailFromRemote(it)
        }
    }

}