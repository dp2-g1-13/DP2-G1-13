Feature: Showing advertisement and its flat details (UH 9) 
   I can sign up in the system and see an advertisement with its flat details.
  
  Scenario: Successful advert and flat show as anonymous (Positive)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The advert and flat info appears
    
  Scenario: Successful advert and flat show as tenant (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The advert and flat info appears
    
  Scenario: Successful advert and flat show as host (Positive)
    Given I am not logged in the system
    When I do login as user "vcasero8"
    And I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The advert and flat info appears
    
  Scenario: Fail advert and flat show because I am banned (Negative)
    Given I am not logged in the system
    When I do login as user "mmcgaheye"
    Then The system says that my user is disabled
    