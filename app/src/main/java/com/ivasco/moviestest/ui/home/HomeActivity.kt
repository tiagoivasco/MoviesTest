package com.ivasco.moviestest.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ivasco.moviestest.R
import com.ivasco.moviestest.common.recyclerview.PaginationScrollListener
import com.ivasco.moviestest.common.utils.gone
import com.ivasco.moviestest.common.utils.setAnchorId
import com.ivasco.moviestest.common.utils.visible
import com.ivasco.moviestest.data.Resource
import com.ivasco.moviestest.data.model.Movie
import com.ivasco.moviestest.databinding.ActivityMainBinding
import com.ivasco.moviestest.ui.details.MovieDetailsActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : AppCompatActivity(), MovieListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    val viewModel: HomeViewModel by viewModel ()
    private val movieListAdapter: MovieListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()
        setupSwipeRefresh()
        viewModel.refreshPopularMovies()

        lifecycleScope.launch {
            viewModel.popularMoviesState.collect {
                handleMoviesDataState(it)
            }
        }
    }

    override fun onItemClick(movie: Movie, container: View) {
        val launch = Intent(
            this,
            MovieDetailsActivity::class.java
        )
        launch.putExtra("movieId", movie.id.toString())
        launch.putExtra("moviePosterURL", movie.poster_path.toString())
        startActivity(launch)
    }

    private fun setupRecyclerView() {
        movieListAdapter.setOnMovieClickListener(this)

        binding.rvFragmentMovieList.adapter = movieListAdapter
        binding.rvFragmentMovieList.addOnScrollListener(object :
            PaginationScrollListener(binding.rvFragmentMovieList.linearLayoutManager) {
            override fun isLoading(): Boolean {
                val isLoading = binding.srlFragmentMovieList.isRefreshing

                if (isLoading) {
                    binding.pbFragmentMovieList.visible()
                } else {
                    binding.pbFragmentMovieList.gone()
                }

                return isLoading
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage()
            }

            override fun loadMoreItems() {
                viewModel.fetchNextPopularMovies()
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentMovieList.setOnRefreshListener {
            viewModel.refreshPopularMovies()
        }
    }

    private fun handleMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.srlFragmentMovieList.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding.srlFragmentMovieList.isRefreshing = false
                loadMovies(state.data)
            }

            Resource.Status.ERROR -> {
                binding.srlFragmentMovieList.isRefreshing = false
                binding.pbFragmentMovieList.gone()
                Snackbar.make(
                    binding.srlFragmentMovieList,
                    getString(R.string.error_message_pattern, state.message),
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorId(R.id.pb_fragment_movie_list).show()
            }

            Resource.Status.EMPTY -> {
//                Timber.d("Empty state.")
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let {
            if (viewModel.isFirstPage()) {
                movieListAdapter.clear()
            }

            movieListAdapter.fillList(it)
        }
    }
}