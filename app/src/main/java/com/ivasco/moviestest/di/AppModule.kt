package com.ivasco.moviestest.di

import android.content.Context
import com.ivasco.moviestest.R
import com.ivasco.moviestest.connectivity.HasInternetConnectionUseCase
import com.ivasco.utils.connectivity.ConnectivityUtils
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single(named("TMDB_KEY")) { provideApiKey(get()) }

    single<ConnectivityUtils> { HasInternetConnectionUseCase(get()) }
}

internal fun provideApiKey(
    context: Context
): String = context.getString(R.string.tmdb_key)
