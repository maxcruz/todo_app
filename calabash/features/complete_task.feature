Feature: Complete task

    Scenario Outline: As a user I want to add some tasks in the list so that set some of them as complete
    Given I press "fabAddTask"
    And I enter "<title>" into input field number 1
    And I enter "<description>" into input field number 2
    Then I press "fabAddTask"

    Examples:
        | title | description	|
        | Task1 | Lorem ipsum 1	|
        | Task2 | Lorem ipsum 2	|
        | Task3 | Lorem ipsum 3	|

    Scenario: As a user I want to set a task as completed
    Given I toggle checkbox number 1
    Then I should see "Task marked complete"
    And I toggle checkbox number 3
    Then I should see "Task marked complete"
    
    Scenario: As a user I want to set a task active again
    Given I toggle checkbox number 1
    Then I should see "Task marked active"

