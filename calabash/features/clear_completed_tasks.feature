Feature: Clear completed tasks

    Scenario Outline: As a user I want to add some tasks in the list so that set some of them as complete
    Given I press "fabAddTask"
    And I enter "<title>" into input field number 1
    And I enter "<description>" into input field number 2
    Then I press "fabAddTask"

    Examples:
        |  n  | title | description	|
        |  1  | Task1 | Lorem ipsum 1	|
        |  2  | Task2 | Lorem ipsum 2	|

    Scenario: As a user I want to set complete some tasks in the list so that can clear it
    Given I toggle checkbox number 1
    Then I should see "Task marked complete"

    Scenario: As a user I want remove the completed tasks in the list
    Given I press the menu key
    And I press "Clear completed"
    Then I should see "Completed tasks cleared"
    And I should not see "Task1"    

