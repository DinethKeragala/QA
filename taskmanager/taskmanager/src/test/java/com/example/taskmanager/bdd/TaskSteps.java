package com.example.taskmanager.bdd;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.service.TaskService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskSteps {

    private TaskRepository repo;
    private TaskService service;

    private Task taskUnderTest;
    private Exception caught;

    public TaskSteps() {
        // Create a Mockito mock for the repository and a real service using it
        this.repo = mock(TaskRepository.class);
        this.service = new TaskService(repo);
    }

    @Given("a new task titled {string}")
    public void a_new_task_titled(String title) {
        taskUnderTest = new Task();
        taskUnderTest.setTitle(title);
    }

    @Given("the description {string}")
    public void the_description(String description) {
        taskUnderTest.setDescription(description);
    }

    @When("I add the task")
    public void i_add_the_task() {
        caught = null;
        try {
            // Stub save to return the same task for happy path verification
            when(repo.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
            service.addTask(taskUnderTest);
        } catch (Exception e) {
            caught = e;
        }
    }

    @Then("the task is saved successfully")
    public void the_task_is_saved_successfully() {
        // No exception should have been thrown
        assertNull(caught, () -> "Unexpected exception: " + (caught == null ? "" : caught.getMessage()));
        // Verify the repository save was invoked once with a Task
        verify(repo, times(1)).save(any(Task.class));
    }

    @Then("I should see an error {string}")
    public void i_should_see_an_error(String expectedMessage) {
        assertNotNull(caught, "Expected an exception to be thrown");
        assertTrue(caught.getMessage().contains(expectedMessage),
                () -> "Expected message to contain: " + expectedMessage + " but was: " + caught.getMessage());
        // Ensure no save happened when validation fails
        verify(repo, never()).save(any(Task.class));
    }
}