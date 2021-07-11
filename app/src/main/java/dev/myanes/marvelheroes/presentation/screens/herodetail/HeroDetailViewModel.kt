package dev.myanes.marvelheroes.presentation.screens.herodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.usecases.GetHeroDetailUseCase
import dev.myanes.marvelheroes.presentation.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeroDetailViewModel(
    private val getHeroDetailUseCase: GetHeroDetailUseCase
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>().apply { false }
    val loading: LiveData<Boolean> get() = _loading

    private val _heroDetail = MutableLiveData<Hero>()
    val heroDetail: LiveData<Hero> get() = _heroDetail

    private val _isEmptyCase = MutableLiveData<Boolean>().apply { false }
    val isEmptyCase: LiveData<Boolean> get() = _isEmptyCase

    private val _showError: SingleLiveEvent<Result.Error> = SingleLiveEvent()
    val showError: LiveData<Result.Error> get() = _showError


    fun loadDetail(id: String) {
        viewModelScope.launch(Dispatchers.Main) {
            if (_heroDetail.value == null) _loading.value = true
            _isEmptyCase.value = false
            getHeroDetailUseCase(id).fold(
                error = {
                    _showError.value = it
                    _isEmptyCase.value = true
                },
                success = {
                    _heroDetail.value = it
                    _isEmptyCase.value = false
                }
            )
            _loading.value = false
        }
    }

}