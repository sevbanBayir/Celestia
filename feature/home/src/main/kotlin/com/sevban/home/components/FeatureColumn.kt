package com.sevban.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme

@Composable
fun FeatureColumn(
    topText: String,
    altText: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = topText,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = altText,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FeatureColumnPrev() {
    ComposeScaffoldProjectTheme {
        FeatureColumn(
            topText = "Wind",
            altText = "14.15 m/h"
        )
    }
}