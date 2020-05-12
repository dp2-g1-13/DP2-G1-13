Feature: Allow only one advertisement per flat (UH 40) 
   I can sign up in the system as host and create only one advertisement per flat.

#Positive scenario is the same as the creatingAvertisements.feature one

  Scenario: Fail advertisement creation because already exists one (Negative)
    Given I am not logged in the system
    When I do login as user "rbordessa0"
    And I go to my flats page
    And I try to create an advertisement for the first flat
    Then The "Publish an ad" button doesnt exists
