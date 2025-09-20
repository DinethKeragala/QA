package com.example.taskmanager.service;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repository;

    private static final String TITLE_REQUIRED_MSG = "Title is mandatory";

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return repository.findById(id);
    }

    /*
    public Task addTask(@Valid Task task) {
        // No validation here -> test expecting an exception will fail (red)
        return repository.save(task);
    }
     */

    /*
    public Task addTask(@Valid Task task) {
        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title is mandatory");
        }
        return repository.save(task);
    }
    */
    public Task addTask(@Valid Task task) {
        ensureTitleValid(task.getTitle());
        return repository.save(task);
    }


    public Task updateTask(Long id, @Valid Task taskDetails) {
        // No validation here -> updating with blank title won't throw either
        return repository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    task.setCompleted(taskDetails.isCompleted());
                    return repository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(Long id) {
        var taskOpt = repository.findById(id);
        if (taskOpt.isPresent()) {
            repository.delete(taskOpt.get());
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    private void ensureTitleValid(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(TITLE_REQUIRED_MSG);
        }
    }
}