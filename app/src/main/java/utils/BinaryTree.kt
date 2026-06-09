package com.example.aijournalcompanion.utils

import com.example.aijournalcompanion.JournalEntry

class BinaryTree {

    private class Node(
        val data: JournalEntry,
        var left: Node? = null,
        var right: Node? = null
    )

    private var root: Node? = null

    fun insert(entry: JournalEntry) {
        root = insertNode(root, entry)
    }

    private fun insertNode(node: Node?, entry: JournalEntry): Node {
        if (node == null) return Node(entry)

        if (entry.emotion.uppercase() < node.data.emotion.uppercase()) {
            node.left = insertNode(node.left, entry)
        } else {
            node.right = insertNode(node.right, entry)
        }

        return node
    }

    fun search(emotion: String): List<JournalEntry> {
        val result = mutableListOf<JournalEntry>()
        searchNode(root, emotion.uppercase(), result)
        return result
    }

    private fun searchNode(
        node: Node?,
        emotion: String,
        result: MutableList<JournalEntry>
    ) {
        if (node == null) return

        searchNode(node.left, emotion, result)

        if (node.data.emotion.contains(emotion, ignoreCase = true)) {
            result.add(node.data)
        }

        searchNode(node.right, emotion, result)
    }
}