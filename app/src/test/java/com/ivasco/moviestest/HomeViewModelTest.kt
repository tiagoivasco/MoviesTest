package com.ivasco.moviestest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ivasco.moviestest.data.Resource
import com.ivasco.moviestest.data.model.MoviesResponse
import com.ivasco.moviestest.domain.GetPopularMoviesUseCase
import com.ivasco.moviestest.ui.home.HomeViewModel
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @Rule @JvmField
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getPopularMoviesUseCase: GetPopularMoviesUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = HomeViewModel(getPopularMoviesUseCase)
    }

    @Test
    fun `fetchPopularMovies should update stateFlow with loading state`() {
        `when`(getPopularMoviesUseCase.execute(1))
            .thenReturn(Single.just(MoviesResponse(1, 0, 0, emptyList())))
        viewModel.fetchPopularMovies()
        assertEquals(Resource.Status.LOADING, viewModel.stateFlow.value)
    }

    @Test
    fun `fetchNextPopularMovies should increment currentPage and call fetchPopularMovies`() {
        `when`(getPopularMoviesUseCase.execute(2)).thenReturn(
            Single.just(
                MoviesResponse(
                    1,
                    0,
                    0,
                    emptyList()
                )
            )
        )
        viewModel.currentPage = 1
        viewModel.fetchNextPopularMovies()
        assertEquals(2, viewModel.currentPage)
        verify(getPopularMoviesUseCase).execute(2)
    }

    @Test
    fun `refreshPopularMovies should reset currentPage to 1 and call fetchPopularMovies`() {
        `when`(getPopularMoviesUseCase.execute(1)).thenReturn(
            Single.just(
                MoviesResponse(
                    1,
                    0,
                    0,
                    emptyList()
                )
            )
        )
        viewModel.currentPage = 5
        viewModel.refreshPopularMovies()
        assertEquals(1, viewModel.currentPage)
        verify(getPopularMoviesUseCase).execute(1)
    }

    @Test
    fun `isFirstPage should return true when currentPage is 1`() {
        viewModel.currentPage = 1
        val isFirstPage = viewModel.isFirstPage()
        assertTrue(isFirstPage)
    }

    @Test
    fun `isLastPage should return true when currentPage is equal to lastPage`() {
        viewModel.currentPage = 3
        viewModel.lastPage = 3
        val isLastPage = viewModel.isLastPage()
        assertTrue(isLastPage)
    }
}