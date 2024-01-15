package com.ivasco.moviestest.domain

import com.ivasco.moviestest.data.model.MoviesResponse
import com.ivasco.moviestest.data.repository.MoviesRemoteRepository
import io.reactivex.Single

class GetPopularMoviesUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(page: Int): Single<MoviesResponse> {
        return moviesRemoteRepository.getPopularMovies(page)
    }

}