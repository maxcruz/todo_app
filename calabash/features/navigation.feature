Feature: Navigation 

    Scenario: As a user I want to get in the main screen
    Given I wait for 1 second
    Then I should see "No tasks are available"
    And I should see "TO-DO"

    Scenario: As a user I want to add a new task so that navigate to their detail
    Given I press "fabAddTask"
    And I enter "Task1" into input field number 1
    And I enter "Description1" into input field number 2
    And I press "fabAddTask"
    Then I should see "TO-DO saved"
    
    Scenario: As a user I want to add a new task so that navigate to their detail
    Given I press "Task1"
    Then I wait for the view with id "fabEditTask" to appear
    Given I go back
    Then I wait for the view with id "fabAddTask" to appear

    Scenario: As a user I want to open the statistics screen
    When I swipe left
    And I wait for 2 seconds
    And I press "Statistics"
    Then I should see "Active tasks: 1"

    Scenario: As a user I want to open the todo list screen
    When I swipe left
    And I wait for 2 seconds
    And I press "TO-DO List"
    Then I should see "Task1"
