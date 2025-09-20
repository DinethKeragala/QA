package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TaskServiceTests {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testAddTask() {
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Test task");

        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.addTask(task);

        assertNotNull(savedTask);
        assertEquals("New Task", savedTask.getTitle());
    }

    @Test
    void testTaskTitleCannotBeEmpty() {
        Task task = new Task();
        task.setTitle(""); // invalid
        task.setDescription("Test");

        Exception exception = assertThrows(Exception.class, () -> taskService.addTask(task));

        String expectedMessage = "Title is mandatory";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}