package dev.myanes.marvelheroes.presentation.screens.herolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.FakeHeroes
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.usecases.GetHeroesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroListViewModel(
    private val getHeroesUseCase: GetHeroesUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>().apply { false }
    val loading: LiveData<Boolean> get() = _loading

    private val _heroList = MutableLiveData<List<Hero>>()
    val heroList: LiveData<List<Hero>> get() = _heroList


    fun loadHeroes() {
        viewModelScope.launch(Dispatchers.Main) {
            if (_heroList.value == null) _loading.value = true
            getHeroesUseCase().fold(
                error = {
                    when (it){
                        is Result.Error.Friendly -> TODO()
                        Result.Error.UnKnown -> TODO()
                    }
                },
                success = { _heroList.value = it }
            )
            _loading.value = false
        }
    }

}