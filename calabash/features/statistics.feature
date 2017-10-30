Feature: Statistics

    Scenario Outline: As a user I want to add some tasks in the list so that view the statistics
    Given I press "fabAddTask"
    And I enter "<title>" into input field number 1
    And I enter "<description>" into input field number 2
    Then I press "fabAddTask"

    Examples:
        |  n  | title | description	|
        |  1  | Task1 | Lorem ipsum 1	|
        |  2  | Task2 | Lorem ipsum 2	|
        |  3  | Task3 | Lorem ipsum 3	|
        |  4  | Task4 | Lorem ipsum 4	|
        |  5  | Task5 | Lorem ipsum 5	|
        |  6  | Task6 | Lorem ipsum 6	|

    Scenario: As a user I want to set complete some tasks in the list so that can test the statistics
    Given I toggle checkbox number 1
    Then I should see "Task marked complete"
    Given I toggle checkbox number 2
    Then I should see "Task marked complete"
    Given I toggle checkbox number 3
    Then I should see "Task marked complete"

    Scenario: As a user I want to view the statistics of the completed and active tasks
    When I swipe left
    And I wait for 2 seconds
    And I press "Statistics"
    Then I should see "Active tasks: 3"
    And I should see "Completed tasks: 3"
