package com.sevban.home.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.sevban.home.R

@Composable
fun HeaderAndMoreBox(
    onFutureDaysForecastClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.next_24_hours),
            modifier = Modifier.align(Alignment.CenterStart)
        )

        TextButton(
            onClick = onFutureDaysForecastClick,
            modifier = Modifier.align(Alignment.CenterEnd),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = stringResource(R.string.future_days_forecast_button),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}
