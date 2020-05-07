Feature: Listing tasks (UH 21) 
   I can sign up in the system as tenant and list my tasks.

  Scenario: Successful task list (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I go to my tasks page
    Then The tasks of my flat appears 	

  Scenario: Failt task list as host (Negative)
    Given I am not logged in the system
    When I do login as user "rbordessa0"
    And I go to my user page
    Then My tasks button doesnt appears 	