Feature: Listing requests (UH 17) 
   I can sign up in the system as a host and list the requests of my flats.
  
  Scenario: Successful request listing (Positive)
    Given I am not logged in the system
    When I do login as user "glikly4"
    And I go to my flats page
    Then I list the requests of the first flat
    
  Scenario: Fail request listing as tenant (Negative)
    Given I am not logged in the system
    When I do login as user "dlippiett3"
    Then Find my flats button doesnt exists
    
  Scenario: Fail request listing as anonymous (Negative)
    When I am not logged in the system
    Then Find my flats button doesnt exists