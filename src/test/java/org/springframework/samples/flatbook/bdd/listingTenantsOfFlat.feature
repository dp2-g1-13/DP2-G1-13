Feature: Listing flat tenants (UH 10) 
   I can see the tenants that lives in a flat and their user pages.

  Scenario: Successful flat tenants access (Positive)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    Then I can visit the tenants user pages	