package com.ivasco.moviestest.ui.details

import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigator
import com.google.android.material.chip.Chip
import com.ivasco.moviestest.R
import com.ivasco.moviestest.common.glide.load
import com.ivasco.moviestest.common.utils.ColorUtils.darken
import com.ivasco.moviestest.common.utils.TimeUtils
import com.ivasco.moviestest.common.utils.dp
import com.ivasco.moviestest.common.utils.gone
import com.ivasco.moviestest.common.utils.orNa
import com.ivasco.moviestest.common.utils.visible
import com.ivasco.moviestest.data.Resource
import com.ivasco.moviestest.data.remote.api.ApiClient
import com.ivasco.moviestest.data.model.Genres
import com.ivasco.moviestest.data.model.MovieDetail
import com.ivasco.moviestest.data.remote.model.RemoteMovieDetail
import com.ivasco.moviestest.databinding.ActivityMovieDetailsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class MovieDetailsActivity : AppCompatActivity() {
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()
    private lateinit var binding: ActivityMovieDetailsBinding
    private var movieDetail: MovieDetail? = null

    private val movieId by lazy {
        intent.extras?.getString("movieId")
    }

    private val moviePosterURL by lazy {
        intent.extras?.getString("moviePosterURL")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupPosterImage()
        movieDetailsViewModel.fetchSingleMovie(movieId.toString())
        lifecycleScope.launch {
            movieDetailsViewModel.singleMovieState.collect {
                handleSingleMovieDataState(it)
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleSingleMovieDataState(state: Resource<MovieDetail>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
            }

            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                loadMovieData(state.data)
            }

            Resource.Status.ERROR -> {
                binding.progressBar.gone()
                Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                finish()
            }

            else -> {}
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadMovieData(data: MovieDetail?) {
        data?.let {
            movieDetail = it
            binding.collapsingToolbar.title = data.title
            binding.detailDescription.text = data.overview
            binding.companyName.text = data.production_companies.firstOrNull()?.name.orNa()

            binding.runtime.text = if (data.runtime > 0)
                TimeUtils.formatMinutes(this, data.runtime) else getString(R.string.no_data_na)

            binding.year.text = if (data.release_date.isNotEmpty())
                LocalDate.parse(data.release_date).format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                        .withLocale(Locale.getDefault())
                ) else getString(R.string.no_release_date)

            binding.website.text = HtmlCompat.fromHtml(
                getString(
                    R.string.visit_website_url_pattern,
                    data.homepage,
                    getString(R.string.visit_website)
                ), HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            binding.website.movementMethod = LinkMovementMethod.getInstance()
            fillGenres(data.genres)

            binding.detailExtraInfo.detailRating.text =
                if (data.vote_average > 0) data.vote_average.toString() else getString(R.string.no_ratings)
            binding.detailExtraInfo.detailVotes.text =
                if (data.vote_count > 0) data.vote_count.toString() else getString(R.string.no_data_na)
            binding.detailExtraInfo.detailRevenue.text = getString(
                R.string.revenue_pattern,
                DecimalFormat("##.##").format(data.revenue / 1000000.0)
            )
        }
    }

    private fun fillGenres(genres: List<Genres>) {
        for (g in genres) {
            val chip = Chip(this)
            chip.text = g.name
            binding.genresChipGroup.addView(chip)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupPosterImage() {
        postponeEnterTransition()
        binding.ivActivityMovieDetails.transitionName = movieId.toString()
        binding.ivActivityMovieDetails.load(
            url = ApiClient.POSTER_BASE_URL + moviePosterURL,
            width = 160.dp,
            height = 160.dp
        ) { color ->
            window?.statusBarColor = color.darken
            binding.collapsingToolbar.setBackgroundColor(color)
            binding.collapsingToolbar.setContentScrimColor(color)
            startPostponedEnterTransition()
        }
    }
}