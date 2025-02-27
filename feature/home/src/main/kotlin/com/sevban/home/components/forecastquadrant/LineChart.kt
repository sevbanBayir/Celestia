package com.sevban.home.components.forecastquadrant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed

@Composable
fun LineChart(
    yAxisData: List<Int>,
    modifier: Modifier = Modifier,
    xAxisData: List<String>,
    graphStyle: GraphStyle = GraphStyle(
        lineColor = MaterialTheme.colorScheme.onBackground,
        jointColor = MaterialTheme.colorScheme.primary,
        textColor = MaterialTheme.colorScheme.onBackground,
        backgroundColor = MaterialTheme.colorScheme.background,
        lineStroke = 6f,
        jointStroke = 4f,
        jointRadius = 10f
    )
) {
    val textMeasurer = rememberTextMeasurer()
    val context = LocalContext.current
    val tempRepresentation: (Int) -> String = {
        context.getString(com.sevban.ui.R.string.temperature_celsius, it.toString())
    }
    val xAxisTextResults = xAxisData.map { textMeasurer.measure(it, style = graphStyle.textStyle) }
    val yAxisTextResults =
        yAxisData.map { textMeasurer.measure(tempRepresentation(it), style = graphStyle.textStyle) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val path = Path()
        val yMaxTextWidth = yAxisTextResults.maxOf { it.size.width }
        val xMaxTextHeight = xAxisTextResults.maxOf { it.size.height }
        val graphHeight = size.height - xMaxTextHeight
        val graphWidth = size.width - yMaxTextWidth
        val oneDegree = graphHeight / (yAxisData.max() - yAxisData.min())
        val oneInterval = size.width / (yAxisData.size - 1)

        var xCursor = 0f
        var yCursor: Float

        xAxisTextResults.fastForEach { textResult ->
            drawText(
                textLayoutResult = textResult,
                color = graphStyle.textColor,
                topLeft = Offset(x = xCursor - textResult.firstBaseline, y = graphHeight + 10f)
            )

            xCursor += oneInterval
        }

        xCursor = 0f
        yCursor = graphHeight - ((yAxisData.first() - yAxisData.min()) * oneDegree)

        path.apply {
            // render temperatures
            yAxisData.fastForEachIndexed { index, value ->
                val textResult = yAxisTextResults[index]
                val textOffsetX = -yMaxTextWidth.toFloat() - textResult.firstBaseline / 2

                yCursor = graphHeight - ((value - yAxisData.min()) * oneDegree)
                drawText(
                    textResult,
                    color = graphStyle.textColor,
                    topLeft = Offset(textOffsetX, yCursor - textResult.size.height)
                )

            }
            xCursor = 0f
            yCursor = graphHeight - ((yAxisData.first() - yAxisData.min()) * oneDegree)
        }

        drawWithLayer {

            path.apply {
                moveTo(0f, -xMaxTextHeight + yCursor)
                yAxisData.forEach { value ->

                    yCursor = graphHeight - ((value - yAxisData.min()) * oneDegree)
                    lineTo(xCursor, -xMaxTextHeight + yCursor)
                    drawLine(
                        graphStyle.gridLineColor,
                        start = Offset(xCursor,-xMaxTextHeight.toFloat()),
                        end = Offset(xCursor, graphHeight - xMaxTextHeight)
                    )
                    moveTo(xCursor, yCursor - xMaxTextHeight)
                    xCursor += oneInterval
                }
                xCursor = 0f
                yCursor = graphHeight - ((yAxisData.first() - yAxisData.min()) * oneDegree)
            }

            drawPath(
                path,
                color = graphStyle.lineColor.copy(alpha = 0.5f),
                style = Stroke(width = graphStyle.lineStroke),
            )

            yAxisData.forEach { value ->
                yCursor = graphHeight - ((value - yAxisData.min()) * oneDegree)
                drawCircle(
                    color = graphStyle.jointColor,
                    radius = graphStyle.jointRadius,
                    center = Offset(xCursor, -xMaxTextHeight + yCursor),
                    blendMode = BlendMode.Clear
                )
                xCursor += oneInterval
            }
            xCursor = 0f
            yCursor = graphHeight - ((yAxisData.first() - yAxisData.min()) * oneDegree)
        }

        yAxisData.forEach { value ->
            yCursor = graphHeight - ((value - yAxisData.min()) * oneDegree)
            drawCircle(
                color = graphStyle.jointColor,
                radius = graphStyle.jointRadius,
                center = Offset(xCursor, -xMaxTextHeight + yCursor),
                style = Stroke(graphStyle.jointStroke),
                blendMode = BlendMode.Clear
            )
            xCursor += oneInterval
        }
    }
}