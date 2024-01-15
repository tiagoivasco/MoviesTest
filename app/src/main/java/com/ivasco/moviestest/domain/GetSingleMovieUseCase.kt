package com.ivasco.moviestest.domain

import com.ivasco.moviestest.data.model.MovieDetail
import com.ivasco.moviestest.data.repository.MoviesRemoteRepository
import io.reactivex.Single

class GetSingleMovieUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(id: String): Single<MovieDetail> {
        return moviesRemoteRepository.getSingleMovie(id)
    }

}