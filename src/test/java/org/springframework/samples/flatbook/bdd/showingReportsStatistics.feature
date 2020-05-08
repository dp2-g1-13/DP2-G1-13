Feature: Statistics of the most reported users of the system (UH 29) 
   I can sign up in the system as administrator and see the stats.

  Scenario: Successful resports statistics show (Positive)
    Given I am not logged in the system
    When I do login as user "admin"
    And I go to the statistics page
    Then The "reports" statistics appears 	

#Negative scenarios would be the same as showing requests statistics feature ones.