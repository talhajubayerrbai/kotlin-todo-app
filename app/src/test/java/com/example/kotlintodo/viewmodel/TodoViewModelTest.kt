package com.example.kotlintodo.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.kotlintodo.data.TodoItem
import com.example.kotlintodo.data.TodoRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModelTest {

    // Swap out the main dispatcher for a test dispatcher
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var repository: TodoRepository

    private lateinit var viewModel: TodoViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        Dispatchers.setMain(testDispatcher)

        // Provide a backing LiveData for allTodos
        every { repository.allTodos } returns MutableLiveData(emptyList())
        viewModel = TodoViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // ------------------------------------------------------------------ addTodo

    @Test
    fun `addTodo with valid title calls repository insert`() = runTest {
        coEvery { repository.insert(any()) } returns 1L

        viewModel.addTodo("Buy milk")
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.insert(match { it.title == "Buy milk" && !it.isCompleted })
        }
    }

    @Test
    fun `addTodo with blank title does NOT call repository insert`() = runTest {
        viewModel.addTodo("   ")
        advanceUntilIdle()

        coVerify(exactly = 0) { repository.insert(any()) }
    }

    @Test
    fun `addTodo posts insertResult on success`() = runTest {
        coEvery { repository.insert(any()) } returns 42L

        val results = mutableListOf<Long>()
        val observer = Observer<Long> { results.add(it) }
        viewModel.insertResult.observeForever(observer)

        viewModel.addTodo("Walk the dog")
        advanceUntilIdle()

        viewModel.insertResult.removeObserver(observer)
        assertEquals(listOf(42L), results)
    }

    // --------------------------------------------------------------- toggleCompleted

    @Test
    fun `toggleCompleted flips isCompleted and calls update`() = runTest {
        val todo = TodoItem(id = 1, title = "Read book", isCompleted = false)

        viewModel.toggleCompleted(todo)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.update(match { it.id == 1L && it.isCompleted })
        }
    }

    @Test
    fun `toggleCompleted on completed item marks it incomplete`() = runTest {
        val todo = TodoItem(id = 2, title = "Exercise", isCompleted = true)

        viewModel.toggleCompleted(todo)
        advanceUntilIdle()

        coVerify(exactly = 1) {
            repository.update(match { it.id == 2L && !it.isCompleted })
        }
    }

    // --------------------------------------------------------------- deleteTodo

    @Test
    fun `deleteTodo calls repository delete with the correct item`() = runTest {
        val todo = TodoItem(id = 5, title = "Clean house")

        viewModel.deleteTodo(todo)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.delete(todo) }
    }

    // --------------------------------------------------------------- deleteCompleted

    @Test
    fun `deleteCompleted calls repository deleteCompleted`() = runTest {
        viewModel.deleteCompleted()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.deleteCompleted() }
    }

    // --------------------------------------------------------------- todos LiveData

    @Test
    fun `todos LiveData reflects allTodos from repository`() {
        val items = listOf(
            TodoItem(id = 1, title = "First"),
            TodoItem(id = 2, title = "Second")
        )
        val liveData = MutableLiveData(items)
        every { repository.allTodos } returns liveData

        // Re-create the ViewModel to pick up the new LiveData
        val vm = TodoViewModel(repository)

        var observed: List<TodoItem>? = null
        val observer = Observer<List<TodoItem>> { observed = it }
        vm.todos.observeForever(observer)

        assertEquals(items, observed)
        vm.todos.removeObserver(observer)
    }
}
