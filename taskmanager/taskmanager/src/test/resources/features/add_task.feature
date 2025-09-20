Feature: Add a new task
  As a user
  I want to add a new task
  So that I can track my work

  Scenario: Successfully add a task with a valid title
    Given a new task titled "Write BDD"
    And the description "Create Gherkin and steps"
    When I add the task
    Then the task is saved successfully

  Scenario: Reject adding a task with an empty title
    Given a new task titled ""
    And the description "Missing title should fail"
    When I add the task
    Then I should see an error "Title is mandatory"