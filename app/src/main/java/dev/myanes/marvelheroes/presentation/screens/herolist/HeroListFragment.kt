package dev.myanes.marvelheroes.presentation.screens.herolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.myanes.marvelheroes.R
import dev.myanes.marvelheroes.databinding.FragmentHeroListBinding
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero
import org.koin.androidx.viewmodel.ext.android.viewModel


class HeroListFragment : Fragment(), HeroListAdapter.HeroListListener {

    //ViewModel
    private val heroListViewModel: HeroListViewModel by viewModel()

    // Views
    private var _binding: FragmentHeroListBinding? = null
    private val binding get() = _binding!!

    // Members
    private lateinit var heroListAdapter: HeroListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeroListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadObservers()
        initComponents()
        loadData()
    }

    private fun loadObservers() {
        heroListViewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.pbLoader.visibility = View.VISIBLE
                false -> binding.pbLoader.visibility = View.GONE
            }
        }

        heroListViewModel.heroList.observe(viewLifecycleOwner) {
            updateData(it)
        }

        heroListViewModel.isEmptyCase.observe(viewLifecycleOwner) {
            when (it) {
                true -> binding.tvNoResults.visibility = View.VISIBLE
                false -> binding.tvNoResults.visibility = View.GONE
            }
        }

        heroListViewModel.showError.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error.Friendly -> showError(it.msg)
                Result.Error.UnKnown -> showError(getString(R.string.unknown_error))
            }
        }
    }

    private fun loadData() {
        heroListViewModel.loadHeroes()
    }

    private fun updateData(list: List<Hero>) {
        heroListAdapter.updateData(list)
    }

    private fun showError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initComponents() {
        heroListAdapter = HeroListAdapter(this)
        binding.recyclerView.adapter = heroListAdapter
    }


    override fun onHeroItemClick(hero: Hero) {
        val bundle = bundleOf("hero_id" to hero.id)
        findNavController().navigate(R.id.show_hero_detail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}