package com.ivasco.moviestest.di

import android.content.Context
import com.ivasco.moviestest.R
import com.ivasco.moviestest.common.utils.ConnectivityUtils
import com.ivasco.moviestest.common.utils.ResourceProvider
import com.ivasco.moviestest.connectivity.HasInternetConnectionUseCase
import com.ivasco.moviestest.data.remote.mapper.MoviesRemoteMapper
import com.ivasco.moviestest.data.repository.MoviesRemoteRepository
import com.ivasco.moviestest.data.repository.MoviesRemoteRepositoryImpl
import com.ivasco.moviestest.domain.GetPopularMoviesUseCase
import com.ivasco.moviestest.domain.GetSingleMovieUseCase
import com.ivasco.moviestest.ui.details.MovieDetailsViewModel
import com.ivasco.moviestest.ui.home.MovieListAdapter
import com.ivasco.moviestest.ui.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named("TMDB_KEY")) { provideApiKey(get()) }
    single { MoviesRemoteMapper() }
    single<ConnectivityUtils> { HasInternetConnectionUseCase(get()) }
    factory<MoviesRemoteRepository> { MoviesRemoteRepositoryImpl(get()) }
    factory { MovieListAdapter(androidContext()) }
    single { ResourceProvider(context = androidContext()) }
}

val homeViewModel = module {
    factory { GetPopularMoviesUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}

val detailsViewModel = module {
    factory { GetSingleMovieUseCase(get()) }
    viewModel { MovieDetailsViewModel(get()) }
}

internal fun provideApiKey(
    context: Context
): String = context.getString(R.string.tmdb_key)
