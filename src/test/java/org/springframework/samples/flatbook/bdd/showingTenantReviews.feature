Feature: Showing tenant reviews (UH 15) 
   I can see other users reviews.
  
  Scenario: Successful tenant review show as anonymous (Positive)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    And I go to "kwhatling1p" user page
    Then The reviews of the tenant appears
    
  Scenario: Successful tenant review show as tenant (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I do look for a flat in "Sevilla"
		And I go to the first flat page
    And I go to "kwhatling1p" user page
    Then The reviews of the tenant appears
    
  Scenario: Successful tenant review show as host (Positive)
    Given I am not logged in the system
    When I do login as user "vcasero8"
    And I do look for a flat in "Sevilla"
    And I go to the first flat page
    And I go to "kwhatling1p" user page
    Then The reviews of the tenant appears
    