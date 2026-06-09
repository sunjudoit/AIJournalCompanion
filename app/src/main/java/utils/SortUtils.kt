package com.example.aijournalcompanion.utils

import com.example.aijournalcompanion.JournalEntry

object SortUtils {

    fun bubbleSort(list: List<JournalEntry>): List<JournalEntry> {
        val sorted = list.toMutableList()
        for (i in 0 until sorted.size - 1) {
            for (j in 0 until sorted.size - i - 1) {
                if (sorted[j].emotion > sorted[j + 1].emotion) {
                    val temp = sorted[j]
                    sorted[j] = sorted[j + 1]
                    sorted[j + 1] = temp
                }
            }
        }
        return sorted
    }

    fun insertionSort(list: List<JournalEntry>): List<JournalEntry> {
        val sorted = list.toMutableList()
        for (i in 1 until sorted.size) {
            val key = sorted[i]
            var j = i - 1
            while (j >= 0 && sorted[j].emotion > key.emotion) {
                sorted[j + 1] = sorted[j]
                j--
            }
            sorted[j + 1] = key
        }
        return sorted
    }

    fun selectionSort(list: List<JournalEntry>): List<JournalEntry> {
        val sorted = list.toMutableList()
        for (i in sorted.indices) {
            var minIndex = i
            for (j in i + 1 until sorted.size) {
                if (sorted[j].emotion < sorted[minIndex].emotion) {
                    minIndex = j
                }
            }
            val temp = sorted[i]
            sorted[i] = sorted[minIndex]
            sorted[minIndex] = temp
        }
        return sorted
    }
}