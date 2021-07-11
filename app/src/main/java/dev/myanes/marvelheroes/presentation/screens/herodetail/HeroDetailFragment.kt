package dev.myanes.marvelheroes.presentation.screens.herodetail

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dev.myanes.marvelheroes.R
import dev.myanes.marvelheroes.databinding.FragmentHeroDetailBinding
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.presentation.utils.loadURL
import org.koin.androidx.viewmodel.ext.android.viewModel

class HeroDetailFragment : Fragment() {

    // ViewModel
    private val heroDetailViewModel: HeroDetailViewModel by viewModel()

    private var _binding: FragmentHeroDetailBinding? = null
    private val binding get() = _binding!!

    // Members
    private lateinit var heroID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            heroID = it.getString("hero_id")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeroDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = NavHostFragment.findNavController(this);
        NavigationUI.setupWithNavController(binding.detailToolbar, navHostFragment)

        loadObservers()
        loadData()
    }

    private fun loadObservers() {
        heroDetailViewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.pbLoader.visibility = View.VISIBLE
                false -> binding.pbLoader.visibility = View.GONE
            }
        }

        heroDetailViewModel.heroDetail.observe(viewLifecycleOwner) {
            updateHeroUI(it)
        }

        heroDetailViewModel.isEmptyCase.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.tvNoResults.visibility = View.VISIBLE
                false -> binding.tvNoResults.visibility = View.GONE
            }
        }

        heroDetailViewModel.showError.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error.Friendly -> showError(it.msg)
                Result.Error.UnKnown -> showError(getString(R.string.unknown_error))
            }
        }

    }

    private fun loadData() {
        heroDetailViewModel.loadDetail(heroID)
    }

    private fun updateHeroUI(hero: Hero) {
        with(binding) {
            toolbarLayout.title = hero.name
            tvDescription.text = hero.description
            ivImage.loadURL(hero.imageURL)

            // Counters
            counterComics.tvCounterLabel.text = getString(R.string.comics)
            counterComics.tvCounterValue.text = hero.comicsCount.toString()
            counterSeries.tvCounterLabel.text = getString(R.string.series)
            counterSeries.tvCounterValue.text = hero.seriesCount.toString()
            counterStories.tvCounterLabel.text = getString(R.string.stories)
            counterStories.tvCounterValue.text = hero.storiesCount.toString()
            counterEvents.tvCounterLabel.text = getString(R.string.events)
            counterEvents.tvCounterValue.text = hero.eventsCount.toString()
        }
    }

    private fun showError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}