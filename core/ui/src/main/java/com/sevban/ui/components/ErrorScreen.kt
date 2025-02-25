package com.sevban.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.sevban.common.model.Failure
import com.sevban.common.model.toLocalizedMessage
import com.sevban.ui.R

@Composable
fun ErrorScreen(
    whenErrorOccurred: suspend (Throwable, String?) -> Unit,
    failure: Failure,
    onTryAgainClick: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        whenErrorOccurred(
            failure,
            failure.errorType.toLocalizedMessage(context)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .semantics { contentDescription = "Error" }
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = failure.errorType.toLocalizedMessage(context),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onTryAgainClick) {
            Text(text = stringResource(R.string.try_again))
        }
    }
}
