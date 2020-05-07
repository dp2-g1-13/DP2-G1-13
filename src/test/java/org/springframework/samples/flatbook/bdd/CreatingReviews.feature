Feature: Creating reviews (UH 12) 
   I can sign up in the system and create a review of a flat or of a tenant.
  
  Scenario: Successful tenant review creation (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I go to my flat page
    And I go to my roomate user page
    And I make a review of him
    Then The review appears in the page showing as creator "rdunleavy0"
    
  Scenario: Successful flat review creation (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I go to my flat page
    And I make a review of the flat
    Then The review appears in the page showing as creator "rdunleavy0"

  Scenario: Fail tenant review creation because I dont have an account (Negative)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    And I try to make a review of the first tenant
    Then The review button doesnt exist
    
Scenario: Fail flat review creation because I dont have an account (Negative)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The flat review button doesnt exist
    
  Scenario: Fail tenant review creation because I dont live with the user (Negative)
    Given I am not logged in the system
    When I do login as user "owinslade2"
    And I do look for a flat in "Sevilla"
    And I go to the first flat page
    And I try to make a review of the first tenant logged
    Then The review button doesnt exist

  Scenario: Fail flat review creation because I dont live in (Negative)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then The flat review button doesnt exist as logged
    
  Scenario: Fail tenant review creation because I already wrote one (Negative)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I go to my flat page
    And I go to my roomate user page
    Then The "tenant" review button doesnt shows
    
Scenario: Fail flat review creation because I already wrote one (Negative)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I go to my flat page
    Then The "flat" review button doesnt shows