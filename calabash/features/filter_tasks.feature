Feature: Filter tasks

    Scenario Outline: As a user I want to add some tasks in the list so that set some of them as complete
    Given I press "fabAddTask"
    And I enter "<title>" into input field number 1
    And I enter "<description>" into input field number 2
    Then I press "fabAddTask"

    Examples:
        |  n  | title | description	|
        |  1  | Task1 | Lorem ipsum 1	|
        |  2  | Task2 | Lorem ipsum 2	|

    Scenario: As a user I want to set complete some tasks in the list so that can test the filters
    Given I toggle checkbox number 1
    Then I should see "Task marked complete"

    Scenario: As a user I want to use the filters to see the active tasks
    Given I press "menu_filter"
    And I press "Active"
    Then I should see "Active TO-DOs"
    And I should see "Task2"
    And I should not see "Task1" 
    Given I press "menu_filter"
    And I press "All"
    Then I should see "All TO-DOs"
    And I should see "Task1"
    And I should see "Task2"
    
    Scenario: As a user I want to use the filters to see the completed tasks
    Given I press "menu_filter"
    And I press "Completed"
    Then I should see "Completed TO-DOs"
    And I should see "Task1"
    And I should not see "Task2" 
    Given I press "menu_filter"
    And I press "All"
    Then I should see "All TO-DOs"
    And I should see "Task1"
    And I should see "Task2"
