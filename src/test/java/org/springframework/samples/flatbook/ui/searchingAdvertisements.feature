Feature: Searching advertisements (UH 11) 
   I can sign up in the system and search by location the advertisements.
  
  Scenario: Successful advertisement search as anonymous (Positive)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    Then Adverts near "Sevilla" appears
    
  Scenario: Successful advertisement search as tenant (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I do look for a flat in "Sevilla"
    Then Adverts near "Sevilla" appears
    
  Scenario: Successful advertisement search as host (Positive)
    Given I am not logged in the system
    When I do login as user "vcasero8"
    And I do look for a flat in "Sevilla"
    Then Adverts near "Sevilla" appears
    