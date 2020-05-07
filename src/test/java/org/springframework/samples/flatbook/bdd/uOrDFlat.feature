Feature: Updating or deleting flats (UH 5) 
   I can sign up in the system as host and delete or modify my flats.

  Scenario: Successful flat edition (Positive)
    Given I am not logged in the system
    When I do login as user "efarnalld"
    And I go to my flats page
    And I edit the first flat square meters to "400"
    Then The edited square meters field shows "400" 	

  Scenario: Successful flat deletion (Positive)
    Given I am not logged in the system
    When I do login as user "mmcgaheye"
    And I go to my flats page
    And I delete the first flat
    Then The flat doesnt exists 	

  Scenario: Fail flat edition or deletion because I am a tenant (Negative)
    Given I am not logged in the system
    When I do login as user "dlippiett3"
    Then Find my flats button doesnt exists
    
  Scenario: Fail flat edition or deletion because I am anonymous (Negative)
    When I am not logged in the system
    Then Find my flats button doesnt exists