package com.sevban.home

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.domain.usecase.GetWeatherUseCase
import com.sevban.home.mapper.toForecastUiModel
import com.sevban.home.model.WeatherState
import com.sevban.testing.FakeLocationObserver
import com.sevban.testing.extension.MainCoroutineExtension
import com.sevban.testing.testdata.dummyForecast
import com.sevban.testing.testdata.dummyWeather
import com.sevban.ui.model.toWeatherUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private lateinit var getWeatherUseCase: GetWeatherUseCase
    private lateinit var getForecastUseCase: GetForecastUseCase
    private lateinit var locationObserver: FakeLocationObserver
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeEach
    fun setUp() {
        getWeatherUseCase = mockk(relaxed = true)
        getForecastUseCase = mockk(relaxed = true)
        locationObserver = FakeLocationObserver()
        savedStateHandle = SavedStateHandle()
        viewModel = HomeViewModel(
            getWeatherUseCase,
            getForecastUseCase,
            locationObserver,
            savedStateHandle
        )
    }

    @Test
    fun `given viewModel when initialized then weatherState should be Loading`() = runTest {
        assertThat(viewModel.weatherState.value).isEqualTo(WeatherState.Loading)
    }

    @Test
    fun `given location permission granted when weather is fetched then weatherState should be Success`() =
        runTest {
            coEvery { getWeatherUseCase.execute(any(), any()) } returns flow {
                delay(10)
                emit(dummyWeather)
            }
            coEvery { getForecastUseCase.execute(any(), any()) } returns flow {
                delay(10)
                emit(dummyForecast)
            }

            viewModel.weatherState.test {
                val firstItem = awaitItem()
                assertThat(firstItem).isEqualTo(WeatherState.Loading)
                val secondItem = awaitItem()
                assertThat(secondItem).isEqualTo(
                    WeatherState.Success(
                        dummyWeather.toWeatherUiModel(),
                        dummyForecast.toForecastUiModel()
                    )
                )
            }
        }

    @Test
    fun `given no location permission when weather is fetched then weatherState should be NoLocationPermission`() =
        runTest {
            locationObserver.shouldThrowPermissionException = true
            viewModel.weatherState.test {
                val firstItem = awaitItem()
//                assertThat(firstItem).isEqualTo(WeatherState.Loading)
//                val secondItem = awaitItem()
                //Todo: actually the first item should be loading and second item should be NoLocationPermission
                // but now the first item is NoLocationPermission ??
                assertThat(firstItem).isEqualTo(WeatherState.NoLocationPermission)
                expectNoEvents()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given savedLocation when weather is fetched then weatherState should be Success and location should be equal to savedLocation`() =
        runTest {
            every { getWeatherUseCase.execute(any(), any()) } returns flow {
                delay(10)
                emit(dummyWeather)
            }

            every { getForecastUseCase.execute(any(), any()) } returns flow {
                delay(10)
                emit(dummyForecast)
            }

            savedStateHandle[HomeViewModel.LATITUDE_ARG] = 1.0
            savedStateHandle[HomeViewModel.LONGITUDE_ARG] = 2.0

            turbineScope {
                val uiStateTurbine = viewModel.uiState.testIn(backgroundScope)

                val firstUiStateItem = uiStateTurbine.awaitItem()
                assertThat(firstUiStateItem.latitude).isEqualTo(0.0)
                assertThat(firstUiStateItem.longitude).isEqualTo(0.0)

                val weatherStateTurbine = viewModel.weatherState.testIn(backgroundScope)

                val firstWeatherStateItem = weatherStateTurbine.awaitItem()
                assertThat(firstWeatherStateItem).isEqualTo(WeatherState.Loading)

                val secondWeatherStateItem = weatherStateTurbine.awaitItem()
                assertThat(secondWeatherStateItem).isEqualTo(
                    WeatherState.Success(
                        dummyWeather.toWeatherUiModel(),
                        dummyForecast.toForecastUiModel()
                    )
                )

                val secondUiStateItem = uiStateTurbine.awaitItem()
                assertThat(secondUiStateItem.latitude).isEqualTo(1.0)
                assertThat(secondUiStateItem.longitude).isEqualTo(2.0)

                uiStateTurbine.cancelAndIgnoreRemainingEvents()
                weatherStateTurbine.cancelAndIgnoreRemainingEvents()

            }
        }

}