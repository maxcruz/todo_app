Feature: Delete task

    Scenario: As a user I want to add a new task so that delete it
    Given I press "fabAddTask"
    And I enter "Task1" into input field number 1
    And I enter "Description1" into input field number 2
    And I press "fabAddTask"
    Then I should see "TO-DO Saved"

    Scenario: As a user I want to edit a task
    Given I press "Task1"
    And I press "menu_delete"
    Then I should not see "Task1"   
