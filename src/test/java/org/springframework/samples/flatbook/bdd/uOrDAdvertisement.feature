Feature: Updating or deleting advertisements (UH 6) 
   I can sign up in the system as host and delete or modify my advertisements.

  Scenario: Successful advertisement edition (Positive)
    Given I am not logged in the system
    When I do login as user "dframmingham2"
    And I go to my flats page
    And I edit the first flat advertisement description to "Test description"
    Then The edited description text "Test description" appears 	

  Scenario: Successful advertisement deletion (Positive)
    Given I am not logged in the system
    When I do login as user "fricart1"
    And I go to my flats page
    And I delete the first flat advertisement
    Then The see advertisement button doesnt exists	

  Scenario: Fail advertisement edition or deletion because the advertisement is not mine (Negative)
    Given I am not logged in the system
    When I do login as user "dframmingham2"
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The "Edit advertisement" button doesnt exists
    Then The "Delete advertisement" button doesnt exists