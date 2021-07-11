package dev.myanes.marvelheroes.presentation.screens.herodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myanes.marvelheroes.domain.models.FakeHeroes.VALID_ITEM
import dev.myanes.marvelheroes.domain.models.Hero
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeroDetailViewModel(

): ViewModel() {

    private val _loading = MutableLiveData<Boolean>().apply { false }
    val loading: LiveData<Boolean> get() = _loading

    private val _heroDetail = MutableLiveData<Hero>()
    val heroDetail: LiveData<Hero> get() = _heroDetail


    fun loadDetail(id: String){
        viewModelScope.launch(Dispatchers.Main) {
            if (_heroDetail.value == null) _loading.value = true
            val result: Hero = VALID_ITEM
            _heroDetail.value = result
            _loading.value = false
        }
    }

}