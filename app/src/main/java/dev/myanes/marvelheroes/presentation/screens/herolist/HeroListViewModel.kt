package dev.myanes.marvelheroes.presentation.screens.herolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.usecases.GetHeroesUseCase
import dev.myanes.marvelheroes.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeroListViewModel(
    private val getHeroesUseCase: GetHeroesUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>().apply { false }
    val loading: LiveData<Boolean> get() = _loading

    private val _heroList = MutableLiveData<List<Hero>>()
    val heroList: LiveData<List<Hero>> get() = _heroList

    private val _isEmptyCase = MutableLiveData<Boolean>().apply { false }
    val isEmptyCase: LiveData<Boolean> get() = _isEmptyCase

    private val _showError: SingleLiveEvent<Result.Error> = SingleLiveEvent()
    val showError: LiveData<Result.Error> get() = _showError


    fun loadHeroes() {
        viewModelScope.launch(Dispatchers.Main) {
            if (_heroList.value == null) _loading.value = true
            _isEmptyCase.value = false
            getHeroesUseCase().fold(
                error = {
                    _showError.value = it
                    _isEmptyCase.value = true
                },
                success = {
                    _heroList.value = it
                    _isEmptyCase.value = false
                }
            )
            _loading.value = false
        }
    }


}