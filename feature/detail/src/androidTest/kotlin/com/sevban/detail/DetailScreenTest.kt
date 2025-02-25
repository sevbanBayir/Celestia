package com.sevban.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import com.sevban.common.model.ErrorType
import com.sevban.common.model.Failure
import org.junit.Rule
import org.junit.Test

class DetailScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoadingState() {
        composeTestRule.setContent {
            DetailScreen(
                onEvent = {},
                forecastState = ForecastState.Loading,
                whenErrorOccurred = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithContentDescription("Loading").assertIsDisplayed()
    }

    @Test
    fun testErrorState() {
        val failure = Failure(ErrorType.UNKNOWN)
        composeTestRule.setContent {
            DetailScreen(
                onEvent = {},
                forecastState = ForecastState.Error(failure),
                whenErrorOccurred = { _, _ -> }
            )
        }
        composeTestRule.onNodeWithContentDescription("Error").assertIsDisplayed()
    }
}