package com.ivasco.moviestest.ui.details

import androidx.lifecycle.ViewModel
import com.ivasco.moviestest.data.Resource
import com.ivasco.moviestest.data.model.MovieDetail
import com.ivasco.moviestest.domain.GetSingleMovieUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MovieDetailsViewModel(private val getSingleMovieUseCase: GetSingleMovieUseCase
) : ViewModel() {

    private val singleMovieStateFlow = MutableStateFlow<Resource<MovieDetail>>(Resource.empty())
    private val favoritesStateFlow = MutableStateFlow<Resource<Boolean>>(Resource.empty())
    var disposable: Disposable? = null

    val singleMovieState: StateFlow<Resource<MovieDetail>>
        get() = singleMovieStateFlow

    val favoritesState: StateFlow<Resource<Boolean>>
        get() = favoritesStateFlow

    fun fetchSingleMovie(id: String) {
        singleMovieStateFlow.value = Resource.loading()

        disposable = getSingleMovieUseCase.execute(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                singleMovieStateFlow.value = Resource.success(res)
            }, { throwable ->
                throwable.localizedMessage?.let {
                    singleMovieStateFlow.value = Resource.error(it)
                }
            })
    }


}