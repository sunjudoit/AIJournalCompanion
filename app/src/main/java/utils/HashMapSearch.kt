package com.example.aijournalcompanion.utils

import com.example.aijournalcompanion.JournalEntry

class HashMapSearch {

    private val emotionMap =
        HashMap<String, MutableList<JournalEntry>>()

    fun build(list: List<JournalEntry>) {

        emotionMap.clear()

        for (entry in list) {
            val key = entry.emotion.uppercase()

            if (!emotionMap.containsKey(key)) {
                emotionMap[key] = mutableListOf()
            }

            emotionMap[key]?.add(entry)
        }
    }

    fun search(emotion: String): List<JournalEntry> {

        val result = mutableListOf<JournalEntry>()
        val searchText = emotion.uppercase()

        for ((key, entries) in emotionMap) {
            if (key.contains(searchText)) {
                result.addAll(entries)
            }
        }

        return result
    }
}