package dev.myanes.marvelheroes.presentation.screens.herolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.FakeHeroes
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.usecases.GetHeroesUseCase
import dev.myanes.marvelheroes.domain.usecases.SearchHeroesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HeroListViewModelTest {

    @Mock
    lateinit var getHeroesUseCase: GetHeroesUseCase

    @Mock
    lateinit var searchHeroesUseCase: SearchHeroesUseCase

    @Mock
    lateinit var listObserver: Observer<List<Hero>>

    @Mock
    lateinit var observerLoading: Observer<Boolean>

    @Mock
    lateinit var observerError: Observer<Result.Error>

    @Mock
    lateinit var observerEmpty: Observer<Boolean>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `heroes list should be updated from server`() = runBlocking {

        val fakeMovieList = FakeHeroes.LIST_ITEMS

        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Right(fakeMovieList)
        )

        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)
        viewModel.heroList.observeForever(listObserver)

        viewModel.loadHeroes()

        verify(listObserver).onChanged(fakeMovieList)

    }

    @Test
    fun `loading indicator should show while fetching heroes list from server and hide afterwards`() = runBlocking {
        val fakeMovieList = FakeHeroes.LIST_ITEMS

        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Right(fakeMovieList)
        )

        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)
        viewModel.heroList.observeForever(listObserver)
        viewModel.loading.observeForever(observerLoading)

        viewModel.loadHeroes()

        val inOrder = inOrder(observerLoading, listObserver)
        inOrder.verify(observerLoading).onChanged(true)
        inOrder.verify(listObserver).onChanged(fakeMovieList)
        inOrder.verify(observerLoading).onChanged(false)
    }

    @Test
    fun `no results and error ui should show when loading heroes fail`() = runBlocking {

        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Left(Result.Error.UnKnown)
        )

        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.showError.observeForever(observerError)

        viewModel.loadHeroes()

        val inOrder = inOrder(observerEmpty, observerError)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(observerError).onChanged(Result.Error.UnKnown)
        inOrder.verify(observerEmpty).onChanged(true)
    }

    @Test
    fun `no results ui should hide when loading heroes success`() = runBlocking {

        val fakeMovieList = FakeHeroes.LIST_ITEMS

        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Right(fakeMovieList)
        )

        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.heroList.observeForever(listObserver)
        viewModel.loadHeroes()

        val inOrder = inOrder(observerEmpty, listObserver)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(listObserver).onChanged(fakeMovieList)
        inOrder.verify(observerEmpty).onChanged(false)
    }




}