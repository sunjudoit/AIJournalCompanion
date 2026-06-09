package com.example.aijournalcompanion.utils

import com.example.aijournalcompanion.JournalEntry

class DoublyLinkedList {

    private class Node(
        val data: JournalEntry,
        var previous: Node? = null,
        var next: Node? = null
    )

    private var head: Node? = null
    private var tail: Node? = null

    fun add(entry: JournalEntry) {
        val newNode = Node(entry)

        if (head == null) {
            head = newNode
            tail = newNode
        } else {
            tail?.next = newNode
            newNode.previous = tail
            tail = newNode
        }
    }

    fun search(emotion: String): List<JournalEntry> {
        val result = mutableListOf<JournalEntry>()
        var current = head

        while (current != null) {
            if (current.data.emotion.contains(emotion, ignoreCase = true)) {
                result.add(current.data)
            }
            current = current.next
        }

        return result
    }
}