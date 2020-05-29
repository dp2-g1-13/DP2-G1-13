Feature: Accepting or rejecting requests (UH 18) 
   I can sign up in the system as a host and accept or decline the requests of my flats.
  
  Scenario: Successful request accepting (Positive)
    Given I am not logged in the system
    When I do login as user "negre3"
    And I go to my flats page
    And I go to the requests list of the first one
    Then I accept one request
    
  Scenario: Successful request rejecting (Positive)
    Given I am not logged in the system
    When I do login as user "jdavieb"
    And I go to my flats page
    And I go to the requests list of the first one
    Then I decline one request
    
  Scenario: Fail request accepting or rejecting as tenant (Negative)
    Given I am not logged in the system
    When I do login as user "dlippiett3"
    Then Find my flats button doesnt exists
    
  Scenario: Fail request accepting or rejecting as anonymous (Negative)
    When I am not logged in the system
    Then Find my flats button doesnt exists