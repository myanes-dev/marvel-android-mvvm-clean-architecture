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
import org.mockito.Mockito.times
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

    @Mock
    lateinit var observerSearchMode: Observer<Boolean>

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

        // Setup
        val fakeMovieList = FakeHeroes.LIST_ITEMS
        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Right(fakeMovieList)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

        // Recording
        viewModel.heroList.observeForever(listObserver)

        // Action
        viewModel.loadHeroes()

        // Check results
        verify(listObserver).onChanged(fakeMovieList)
    }

    @Test
    fun `loading indicator should show while fetching heroes list from server and hide afterwards`() =
        runBlocking {
            // Setup
            val fakeMovieList = FakeHeroes.LIST_ITEMS
            whenever(getHeroesUseCase.invoke()).thenReturn(
                Either.Right(fakeMovieList)
            )
            val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

            // Recording
            viewModel.heroList.observeForever(listObserver)
            viewModel.loading.observeForever(observerLoading)

            // Action
            viewModel.loadHeroes()

            // Check results
            val inOrder = inOrder(observerLoading, listObserver)
            inOrder.verify(observerLoading).onChanged(true)
            inOrder.verify(listObserver).onChanged(fakeMovieList)
            inOrder.verify(observerLoading).onChanged(false)
        }

    @Test
    fun `no results and error ui should show when loading heroes fail`() = runBlocking {

        // Setup
        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Left(Result.Error.UnKnown)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

        // Recording
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.showError.observeForever(observerError)

        viewModel.loadHeroes()

        // Check results
        val inOrder = inOrder(observerEmpty, observerError)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(observerError).onChanged(Result.Error.UnKnown)
        inOrder.verify(observerEmpty).onChanged(true)
    }

    @Test
    fun `no results ui should hide when loading heroes success`() = runBlocking {

        // Setup
        val fakeMovieList = FakeHeroes.LIST_ITEMS
        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Right(fakeMovieList)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

        // Recording
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.heroList.observeForever(listObserver)

        // Action
        viewModel.loadHeroes()

        // Check results
        val inOrder = inOrder(observerEmpty, listObserver)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(listObserver).onChanged(fakeMovieList)
        inOrder.verify(observerEmpty).onChanged(false)
    }


    @Test
    fun `entering search mode should clear the list`() = runBlocking {

        // Setup
        val fakeMovieList = FakeHeroes.LIST_ITEMS
        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Right(fakeMovieList)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)
        viewModel.loadHeroes()

        // Recording
        viewModel.heroList.observeForever(listObserver)
        viewModel.isSearchMode.observeForever(observerSearchMode)

        // Action
        viewModel.enterSearchMode()

        // Check results
        val inOrder = inOrder(listObserver, observerSearchMode)
        inOrder.verify(observerSearchMode).onChanged(true)
        inOrder.verify(listObserver).onChanged(emptyList())

    }

    @Test
    fun `leaving search mode should load heroes list from memory cache`() = runBlocking {

        // Setup
        val fakeMovieList = FakeHeroes.LIST_ITEMS
        whenever(getHeroesUseCase.invoke()).thenReturn(
            Either.Right(fakeMovieList)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)
        viewModel.loadHeroes()

        // Recording
        viewModel.heroList.observeForever(listObserver)
        viewModel.isSearchMode.observeForever(observerSearchMode)

        // Action
        viewModel.leaveSearchMode()

        // Check results
        val inOrder = inOrder(listObserver, observerSearchMode)
        inOrder.verify(observerSearchMode).onChanged(false)
        inOrder.verify(listObserver).onChanged(fakeMovieList)

    }

    @Test
    fun `Searching heroes should clear the list first and fill it when data results`() = runBlocking {

        // Setup
        val fakeMovieList = FakeHeroes.LIST_ITEMS
        whenever(searchHeroesUseCase.invoke("TEST")).thenReturn(
            Either.Right(fakeMovieList)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

        // Recording
        viewModel.heroList.observeForever(listObserver)

        // Action
        viewModel.searchHeroes("TEST")

        // Check results
        val inOrder = inOrder(listObserver)
        inOrder.verify(listObserver).onChanged(emptyList())
        inOrder.verify(listObserver).onChanged(fakeMovieList)

    }

    @Test
    fun `loading indicator should show while searching heroes list and hide afterwards`() =
        runBlocking {
            // Setup
            val fakeMovieList = FakeHeroes.LIST_ITEMS
            whenever(searchHeroesUseCase.invoke("TEST")).thenReturn(
                Either.Right(fakeMovieList)
            )
            val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

            // Recording
            viewModel.heroList.observeForever(listObserver)
            viewModel.loading.observeForever(observerLoading)

            // Action
            viewModel.searchHeroes("TEST")

            // Check results
            val inOrder = inOrder(observerLoading, listObserver)
            inOrder.verify(observerLoading).onChanged(true)
            inOrder.verify(listObserver).onChanged(fakeMovieList)
            inOrder.verify(observerLoading).onChanged(false)
        }

    @Test
    fun `no results and error ui should show when searching heroes fail`() = runBlocking {

        // Setup
        whenever(searchHeroesUseCase.invoke("TEST")).thenReturn(
            Either.Left(Result.Error.UnKnown)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

        // Recording
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.showError.observeForever(observerError)

        // Action
        viewModel.searchHeroes("TEST")

        // Check results
        val inOrder = inOrder(observerEmpty, observerError)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(observerError).onChanged(Result.Error.UnKnown)
        inOrder.verify(observerEmpty).onChanged(true)
    }

    @Test
    fun `no results ui should hide when searching heroes success with results`() = runBlocking {

        // Setup
        val fakeMovieList = FakeHeroes.LIST_ITEMS
        whenever(searchHeroesUseCase.invoke("TEST")).thenReturn(
            Either.Right(fakeMovieList)
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

        // Recording
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.heroList.observeForever(listObserver)

        // Action
        viewModel.searchHeroes("TEST")

        // Check results
        val inOrder = inOrder(observerEmpty, listObserver)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(listObserver).onChanged(fakeMovieList)
        inOrder.verify(observerEmpty).onChanged(false)
    }

    @Test
    fun `no results ui should show when searching heroes returns nothing`() = runBlocking {

        // Setup
        whenever(searchHeroesUseCase.invoke("TEST")).thenReturn(
            Either.Right(emptyList())
        )
        val viewModel = HeroListViewModel(getHeroesUseCase, searchHeroesUseCase)

        // Recording
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.heroList.observeForever(listObserver)

        // Action
        viewModel.searchHeroes("TEST")

        // Check results
        val inOrder = inOrder(observerEmpty, listObserver)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(listObserver, times(2)).onChanged(emptyList())
        inOrder.verify(observerEmpty, ).onChanged(true)
    }

}