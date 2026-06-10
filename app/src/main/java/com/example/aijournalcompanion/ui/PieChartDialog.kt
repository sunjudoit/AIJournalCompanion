package com.example.aijournalcompanion.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aijournalcompanion.JournalEntry

@Composable
fun PieChartDialog(
    journalList: List<JournalEntry>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Emotion Chart")
        },
        text = {
            Column {

                EmotionPieChart(
                    journalList = journalList
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                val emotionCounts =
                    journalList.groupingBy { it.emotion }
                        .eachCount()

                emotionCounts.forEach { (emotion, count) ->
                    Text("$emotion : $count")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Close")
            }
        }
    )
}

@Composable
fun EmotionPieChart(
    journalList: List<JournalEntry>
) {

    val emotionCounts =
        journalList.groupingBy { it.emotion }
            .eachCount()

    val total =
        emotionCounts.values.sum()

    val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan
    )

    Canvas(
        modifier = Modifier
            .size(220.dp)
            .padding(8.dp)
    ) {

        var startAngle = -90f
        var colorIndex = 0

        emotionCounts.forEach { (_, count) ->

            val sweepAngle =
                (count.toFloat() / total.toFloat()) * 360f

            drawArc(
                color = colors[colorIndex % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = Size(
                    width = size.width,
                    height = size.height
                )
            )

            startAngle += sweepAngle
            colorIndex++
        }
    }
}