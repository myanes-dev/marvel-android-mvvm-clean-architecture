package dev.myanes.marvelheroes.presentation.screens.herodetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import dev.myanes.marvelheroes.domain.Either
import dev.myanes.marvelheroes.domain.Result
import dev.myanes.marvelheroes.domain.models.FakeHeroes
import dev.myanes.marvelheroes.domain.models.Hero
import dev.myanes.marvelheroes.domain.usecases.GetHeroDetailUseCase
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
class HeroDetailViewModelTest {
    @Mock
    lateinit var getHeroDetailUseCase: GetHeroDetailUseCase

    @Mock
    lateinit var detailObserver: Observer<Hero>

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
    fun `hero details should be updated from server`() = runBlocking {

        val fakeHero = FakeHeroes.VALID_ITEM

        whenever(getHeroDetailUseCase.invoke("1")).thenReturn(
            Either.Right(fakeHero)
        )

        val viewModel = HeroDetailViewModel(getHeroDetailUseCase)
        viewModel.heroDetail.observeForever(detailObserver)

        viewModel.loadDetail("1")

        verify(detailObserver).onChanged(fakeHero)

    }

    @Test
    fun `loading indicator should show while fetching hero details from server and hide afterwards`() = runBlocking {
        val fakeHero = FakeHeroes.VALID_ITEM

        whenever(getHeroDetailUseCase.invoke("1")).thenReturn(
            Either.Right(fakeHero)
        )

        val viewModel = HeroDetailViewModel(getHeroDetailUseCase)
        viewModel.heroDetail.observeForever(detailObserver)
        viewModel.loading.observeForever(observerLoading)

        viewModel.loadDetail("1")

        val inOrder = inOrder(observerLoading, detailObserver)
        inOrder.verify(observerLoading).onChanged(true)
        inOrder.verify(detailObserver).onChanged(fakeHero)
        inOrder.verify(observerLoading).onChanged(false)
    }

    @Test
    fun `no results and error ui should show when loading hero details fail`() = runBlocking {

        whenever(getHeroDetailUseCase.invoke("FAIL")).thenReturn(
            Either.Left(Result.Error.UnKnown)
        )

        val viewModel = HeroDetailViewModel(getHeroDetailUseCase)
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.showError.observeForever(observerError)

        viewModel.loadDetail("FAIL")

        val inOrder = inOrder(observerEmpty, observerError)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(observerError).onChanged(Result.Error.UnKnown)
        inOrder.verify(observerEmpty).onChanged(true)
    }

    @Test
    fun `no results ui should hide when loading hero details success`() = runBlocking {

        val fakeHero = FakeHeroes.VALID_ITEM

        whenever(getHeroDetailUseCase.invoke("1")).thenReturn(
            Either.Right(fakeHero)
        )

        val viewModel = HeroDetailViewModel(getHeroDetailUseCase)
        viewModel.isEmptyCase.observeForever(observerEmpty)
        viewModel.heroDetail.observeForever(detailObserver)

        viewModel.loadDetail("1")

        val inOrder = inOrder(observerEmpty, detailObserver)
        inOrder.verify(observerEmpty).onChanged(false)
        inOrder.verify(detailObserver).onChanged(fakeHero)
        inOrder.verify(observerEmpty).onChanged(false)
    }


}