Feature: Showing flat reviews (UH 14) 
   I can see other users reviews of a flat.
  
  Scenario: Successful flat review show as anonymous (Positive)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The reviews of the flat appears
    
  Scenario: Successful flat review show as tenant (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The reviews of the flat appears
    
  Scenario: Successful flat review show as host (Positive)
    Given I am not logged in the system
    When I do login as user "vcasero8"
    And I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The reviews of the flat appears
    