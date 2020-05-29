Feature: Statistics of the best rated tenants and hosts of the system (UH 28) 
   I can sign up in the system as administrator and see the stats.

  Scenario: Successful best reviewed statistics show (Positive)
    Given I am not logged in the system
    When I do login as user "admin"
    And I go to the statistics page
    Then The "best reviewed" statistics appears 	

#Negative scenarios would be the same as showing requests statistics feature ones.