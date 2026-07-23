package com.example.kotlintodo.data

import org.junit.Assert.*
import org.junit.Test

/**
 * Pure unit tests for the TodoItem data class.
 */
class TodoItemTest {

    @Test
    fun `default isCompleted is false`() {
        val item = TodoItem(title = "Task")
        assertFalse(item.isCompleted)
    }

    @Test
    fun `copy with toggled isCompleted works correctly`() {
        val item = TodoItem(title = "Task", isCompleted = false)
        val toggled = item.copy(isCompleted = true)
        assertTrue(toggled.isCompleted)
        assertEquals(item.title, toggled.title)
        assertEquals(item.id, toggled.id)
    }

    @Test
    fun `two items with the same id and content are equal`() {
        val a = TodoItem(id = 1, title = "Same", isCompleted = false, createdAt = 1000L)
        val b = TodoItem(id = 1, title = "Same", isCompleted = false, createdAt = 1000L)
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `items with different ids are not equal`() {
        val a = TodoItem(id = 1, title = "Task")
        val b = TodoItem(id = 2, title = "Task")
        assertNotEquals(a, b)
    }

    @Test
    fun `title is stored as-is`() {
        val title = "  Buy groceries  "
        val item = TodoItem(title = title)
        assertEquals(title, item.title)
    }
}
