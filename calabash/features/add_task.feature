Feature: Add task

    Scenario: As a user I want to add a new task to do
    Given I press "fabAddTask"
    And I enter "Task1" into input field number 1
    And I enter "Description1" into input field number 2
    And I press "fabAddTask"
    Then I should see "TO-DO Saved"

    Scenario: As a user I want to add a new task at least with title or description
    Given I press "fabAddTask"
    And I press "fabAddTask"
    Then I should see "TO DOs cannot be empty"
    And I enter "Task2" into input field number 1
    And I press "fabAddTask"
    Then I should see "TO-DO Saved"
    Given I press "fabAddTask"
    And I enter "Task3" into input field number 2
    And I press "fabAddTask"
    Then I should see "TO-DO Saved"
   
