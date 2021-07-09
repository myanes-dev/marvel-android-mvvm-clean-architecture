package dev.myanes.marvelheroes.presentation.screens.herolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myanes.marvelheroes.domain.models.FakeHeroes
import dev.myanes.marvelheroes.domain.models.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeroListViewModel() : ViewModel() {

    private val _loading = MutableLiveData<Boolean>().apply { false }
    val loading: LiveData<Boolean> get() = _loading

    private val _heroList = MutableLiveData<List<Hero>>()
    val heroList: LiveData<List<Hero>> get() = _heroList


    fun loadHeroes() {
        viewModelScope.launch(Dispatchers.Main) {
            if (_heroList.value == null) _loading.value = true
            withContext(Dispatchers.IO) {
                delay(2000)
                _heroList.postValue(FakeHeroes.LIST_ITEMS)
            }
            _loading.value = false
        }
    }

}