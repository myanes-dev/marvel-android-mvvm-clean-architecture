package dev.myanes.marvelheroes.presentation.screens.herolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.myanes.marvelheroes.R
import dev.myanes.marvelheroes.databinding.FragmentHeroListBinding
import dev.myanes.marvelheroes.domain.models.FakeHeros
import dev.myanes.marvelheroes.domain.models.Hero


class HeroListFragment : Fragment(), HeroListAdapter.HeroListListener {

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

        initComponents()
        loadData()

    }

    private fun loadData() {
        heroListAdapter.updateData(FakeHeros.LIST_ITEMS)
    }

    private fun initComponents() {
        heroListAdapter = HeroListAdapter(this)
        binding.recyclerView.adapter = heroListAdapter
    }


    override fun onHeroItemClick(hero: Hero) {
        // TODO send hero ID to detail
        val bundle = Bundle()
        findNavController().navigate(R.id.show_hero_detail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}