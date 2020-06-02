Feature: Listing my flats (UH 7) 
   I can sign up in the system as a host and list my flats.
  
  Scenario: Successful flat listing (Positive)
    Given I am not logged in the system
    When I do login as user "vcasero8"
    And I go to my flats page
    Then I can see my flats listed
    
  Scenario: Fail flat listing as tenant (Negative)
    Given I am not logged in the system
    When I do login as user "dlippiett3"
    Then Find my flats button doesnt exists
    
  Scenario: Fail flat listing as anonymous (Negative)
    When I am not logged in the system
    Then Find my flats button doesnt exists