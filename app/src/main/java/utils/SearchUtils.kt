package com.example.aijournalcompanion.utils

import com.example.aijournalcompanion.JournalEntry

object SearchUtils {

    // Binary Tree Search
    fun searchWithBinaryTree(
        list: List<JournalEntry>,
        emotion: String
    ): List<JournalEntry> {

        val tree = BinaryTree()

        list.forEach { entry ->
            tree.insert(entry)
        }

        return tree.search(emotion)
    }

    // HashMap Search
    fun searchWithHashMap(
        list: List<JournalEntry>,
        emotion: String
    ): List<JournalEntry> {

        val hashMapSearch = HashMapSearch()

        hashMapSearch.build(list)

        return hashMapSearch.search(emotion)
    }

    // Doubly Linked List Search
    fun searchWithDoublyLinkedList(
        list: List<JournalEntry>,
        emotion: String
    ): List<JournalEntry> {

        val linkedList = DoublyLinkedList()

        list.forEach { entry ->
            linkedList.add(entry)
        }

        return linkedList.search(emotion)
    }
}