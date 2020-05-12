Feature: Managing tasks (UH 20) 
   I can sign up in the system as tenant manage my tasks.

  Scenario: Successful task creation (Positive)
    Given I am not logged in the system
    When I do login as user "anund5"
    And I go to my tasks page
    Then I create a new task 	
    
  Scenario: Successful task edition (Positive)
    Given I am not logged in the system
    When I do login as user "anund5"
    And I go to my tasks page
    Then I edit a task 
    
  Scenario: Successful task deletion (Positive)
    Given I am not logged in the system
    When I do login as user "anund5"
    And I go to my tasks page
    Then I delete a task 
    
 Scenario: Fail task management as host (Negative)
    Given I am not logged in the system
    When I do login as user "rbordessa0"
    And I go to my user page
    Then The "My tasks" button doesnt exists 	