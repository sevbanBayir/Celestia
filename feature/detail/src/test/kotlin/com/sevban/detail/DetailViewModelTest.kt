package com.sevban.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.sevban.detail.mapper.DetailForecastUiModelMapper
import com.sevban.domain.usecase.GetForecastUseCase
import com.sevban.testing.extension.MainCoroutineExtension
import com.sevban.testing.testdata.dummyForecast
import com.sevban.testing.testdata.serverError
import com.sevban.ui.model.LocationArgument
import com.sevban.ui.model.locationArgumentNavType
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.typeOf

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class DetailViewModelTest {
    private lateinit var viewModel: DetailViewModel
    private lateinit var getForecastUseCase: GetForecastUseCase
    private lateinit var forecastMapper: DetailForecastUiModelMapper
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeEach
    fun setUp() {
        getForecastUseCase = mockk(relaxed = true)
        forecastMapper = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle()
        
        // Mock navigation
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        
        // Default location for tests
        val defaultLocation = LocationArgument(40.7128, -74.0060)
        every { 
            savedStateHandle.toRoute<Detail>(typeMap = Detail.typeMap)
        } returns Detail(defaultLocation)
        
        viewModel = DetailViewModel(getForecastUseCase, forecastMapper, savedStateHandle)
    }

    @Test
    fun `given viewModel when initialized then forecastState should be Loading`() = runTest {
        assertThat(viewModel.forecastState.value).isEqualTo(ForecastState.Loading)
    }

    @Test
    fun `given location when forecast is fetched then forecastState should be Success`() =
        runTest {
            // Given
            val mockUiModel = mockk<com.sevban.detail.mapper.ForecastUiModel>()

            // When
            every {
                getForecastUseCase.execute("40.7128", "-74.0060")
            } returns flow {
                delay(10) // Ensure time for initial Loading emission
                emit(dummyForecast)
            }
            
            every { forecastMapper.mapToUiModel(dummyForecast) } returns mockUiModel

            // Then
            viewModel.forecastState.test {
                val firstItem = awaitItem()
                assertThat(firstItem).isEqualTo(ForecastState.Loading)
                val secondItem = awaitItem()
                assertThat(secondItem).isEqualTo(ForecastState.Success(mockUiModel))
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `given forecast fetching fails when executed then forecastState should be Error`() =
        runTest {
            every {
                getForecastUseCase.execute("40.7128", "-74.0060")
            } throws serverError
            
            viewModel.forecastState.test {
                val firstItem = awaitItem()
                assertThat(firstItem).isEqualTo(ForecastState.Error(serverError))
            }
        }

    @Test
    fun `given OnTryAgainClick event when triggered then retryTrigger should emit unit`() =
        runTest {
            viewModel.retryTrigger.test {
                viewModel.onEvent(DetailScreenEvent.OnTryAgainClick)
                assertThat(awaitItem()).isEqualTo(Unit)
                expectNoEvents()
            }
        }

    @Test
    fun `given valid location when getForecastUseCase is called then forecastState should be updated`() =
        runTest {
            val mockUiModel = mockk<com.sevban.detail.mapper.ForecastUiModel>()

            every {
                getForecastUseCase.execute("40.7128", "-74.0060")
            } returns flow {
                delay(10)
                emit(dummyForecast)
            }
            
            every { forecastMapper.mapToUiModel(dummyForecast) } returns mockUiModel

            viewModel.forecastState.test {
                val firstItem = awaitItem()
                assertThat(firstItem).isEqualTo(ForecastState.Loading)
                val secondItem = awaitItem()
                assertThat(secondItem).isEqualTo(ForecastState.Success(mockUiModel))
                verify { getForecastUseCase.execute("40.7128", "-74.0060") }
            }
        }

    @Test
    fun `given retryTrigger when triggered then forecastState should reload data`() = runTest {
        val mockUiModel = mockk<com.sevban.detail.mapper.ForecastUiModel>()
        
        every {
            getForecastUseCase.execute("40.7128", "-74.0060")
        } returns flow {
            delay(10)
            emit(dummyForecast)
        }
        
        every { forecastMapper.mapToUiModel(dummyForecast) } returns mockUiModel

        viewModel.forecastState.test {
            val firstItem = awaitItem()
            assertThat(firstItem).isEqualTo(ForecastState.Loading)
            val secondItem = awaitItem()
            assertThat(secondItem).isEqualTo(ForecastState.Success(mockUiModel))
            viewModel.onEvent(DetailScreenEvent.OnTryAgainClick)
            val thirdItem = awaitItem()
            assertThat(thirdItem).isEqualTo(ForecastState.Loading)
            val fourthItem = awaitItem()
            assertThat(fourthItem).isEqualTo(ForecastState.Success(mockUiModel))
            verify(exactly = 2) {
                getForecastUseCase.execute("40.7128", "-74.0060")
            }
        }
    }
}