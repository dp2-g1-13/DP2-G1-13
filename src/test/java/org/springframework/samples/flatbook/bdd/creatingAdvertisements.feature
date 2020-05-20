Feature: Creating advertisements (UH 4) 
   I can sign up in the system as host and create advertisements for my flats.

  Scenario: Successful advertisement creation (Positive)
    Given I am not logged in the system
    When I do login as user "rbordessa0"
    And I go to my flats page
    And I create a new advertisement for my first flat
    Then The advertisement data appears 	

  Scenario: Fail advertisement creation because I dont have an account (Negative)
    Given I am not logged in the system
    Then Find my flats button doesnt exists
    
  Scenario: Fail advertisement creation because I am a tenant (Negative)
    Given I am not logged in the system
    When I do login as user "dlippiett3"
    Then Find my flats button doesnt exists

  Scenario: Fail advertisement creation because I am banned (Negative)
    Given I am not logged in the system
    When I do login as user "mmcgaheye"
    Then The system says that my user is disabled

  Scenario: Fail advertisement creation because already exists one (Negative)
    Given I am not logged in the system
    When I do login as user "rbordessa0"
    And I go to my flats page
    And I try to create an advertisement for the first flat
    Then The "Publish an ad" button doesnt exists