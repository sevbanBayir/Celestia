package com.sevban.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sevban.designsystem.theme.ComposeScaffoldProjectTheme
import com.sevban.home.R

@Composable
fun PressureCard(
    pressure: String,
    pressureStatus: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Compress,
                    contentDescription = stringResource(R.string.cd_pressure_icon),
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.pressure_label),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = if (pressure.isNotEmpty()) pressure else "--",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (pressureStatus.isNotEmpty()) {
                Text(
                    text = pressureStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = when (pressureStatus.lowercase()) {
                        "high" -> MaterialTheme.colorScheme.error
                        "low" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PressureCardHighPreview() {
    ComposeScaffoldProjectTheme {
        PressureCard(
            pressure = "1025 hPa",
            pressureStatus = "High"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PressureCardLowPreview() {
    ComposeScaffoldProjectTheme {
        PressureCard(
            pressure = "995 hPa",
            pressureStatus = "Low"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PressureCardNormalPreview() {
    ComposeScaffoldProjectTheme {
        PressureCard(
            pressure = "1013 hPa",
            pressureStatus = "Normal"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PressureCardEmptyPreview() {
    ComposeScaffoldProjectTheme {
        PressureCard(
            pressure = "",
            pressureStatus = ""
        )
    }
} 