package dev.myanes.marvelheroes.presentation.screens.herolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.usecases.GetHeroesUseCase
import dev.myanes.marvelheroes.domain.usecases.SearchHeroesUseCase
import dev.myanes.marvelheroes.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeroListViewModel(
    private val getHeroesUseCase: GetHeroesUseCase,
    private val searchHeroesUseCase: SearchHeroesUseCase

) : ViewModel() {

    private val _isSearchMode = MutableLiveData<Boolean>()
    val isSearchMode: LiveData<Boolean> get() = _isSearchMode

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _heroList = MutableLiveData<List<Hero>>()
    val heroList: LiveData<List<Hero>> get() = _heroList

    private val _isEmptyCase = MutableLiveData<Boolean>()
    val isEmptyCase: LiveData<Boolean> get() = _isEmptyCase

    private val _showError: SingleLiveEvent<Result.Error> = SingleLiveEvent()
    val showError: LiveData<Result.Error> get() = _showError

    private var cachedHeroList: List<Hero> = mutableListOf()


    fun loadHeroes() {
        if (_heroList.value != null) return

        viewModelScope.launch(Dispatchers.Main) {
            if (_heroList.value == null) _loading.value = true

            _isEmptyCase.value = false

            getHeroesUseCase().fold(
                error = {
                    _showError.value = it
                    _isEmptyCase.value = true
                },
                success = {
                    saveHeroesInMemoryCache(it)
                    _heroList.value = it
                    _isEmptyCase.value = false
                }
            )

            _loading.value = false
        }
    }

    fun searchHeroes(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _loading.value = true
            _isEmptyCase.value = false
            _heroList.value = emptyList()

            searchHeroesUseCase(query).fold(
                error = {
                    _showError.value = it
                    _isEmptyCase.value = true
                },
                success = {
                    _heroList.value = it
                    _isEmptyCase.value = it.isEmpty()
                }
            )
            _loading.value = false
        }
    }

    fun enterSearchMode() {
        _isSearchMode.value = true
        _heroList.value = emptyList()
    }

    fun leaveSearchMode() {
        _isSearchMode.value = false
        _heroList.value = cachedHeroList
        _isEmptyCase.value = cachedHeroList.isEmpty()
    }

    private fun saveHeroesInMemoryCache(list: List<Hero>) {
        cachedHeroList = list
    }


}