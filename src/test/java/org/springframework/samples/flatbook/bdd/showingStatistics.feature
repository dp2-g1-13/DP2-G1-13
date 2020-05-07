Feature: Statistics (UH 26-29) 
   I can sign up in the system as administrator and see the stats.

  Scenario: Successful statistics show (Positive)
    Given I am not logged in the system
    When I do login as user "admin"
    And I go to the statistics page
    Then The statistics appears 	

  Scenario: Fail statistics show as host (Negative)
    Given I am not logged in the system
    When I do login as user "rbordessa0"
    Then The statistics button doesnt exists 	
    
  Scenario: Fail statistics show as tenant (Negative)
    Given I am not logged in the system
    When I do login as user "anund5"
    Then The statistics button doesnt exists 	
    
  Scenario: Fail statistics show as anonimous (Negative)
    Given I am not logged in the system
    Then The statistics button doesnt exists 	