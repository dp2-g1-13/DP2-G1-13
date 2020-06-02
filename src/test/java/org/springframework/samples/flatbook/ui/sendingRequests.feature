Feature: Sending requests (UH 16) 
   I can sign up in the system as a tenant and send a lease request to a flat.
  
  Scenario: Successful request sending (Positive)
    Given I am not logged in the system
    When I do login as user "creuble0"
    And I do look for a flat in "Sevilla"
    And I make a request for live in the first flat
    Then The request appears in my requests list

  Scenario: Failed request sending because I already have one request to the flat (Negative)
    Given I am not logged in the system
    When I do login as user "creuble0"
    And I do look for a flat in "Sevilla"
    And I try to make another request for live in the first flat
    Then I cannot click the request button

  Scenario: Failed request sending because I already live in a flat (Negative)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I do look for a flat in "Sevilla"
    And I try to make a request for live in the first flat
    Then The "Make a request!" button doesnt exists

  Scenario: Failed request sending because I am a host (Negative)
    Given I am not logged in the system
    When I do login as user "fricart1"
    And I do look for a flat in "Sevilla"
    And I try to make a request for live in the first flat
    Then The "Make a request!" button doesnt exists