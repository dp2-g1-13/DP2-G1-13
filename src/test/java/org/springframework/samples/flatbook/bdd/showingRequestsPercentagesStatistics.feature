Feature: Statistics of the percentage of accepted and rejected requests (UH 27) 
   I can sign up in the system as administrator and see the stats.

  Scenario: Successful request percentages statistics show (Positive)
    Given I am not logged in the system
    When I do login as user "admin"
    And I go to the statistics page
    Then The "percentage of requests" statistics appears 	

#Negative scenarios would be the same as showing requests statistics feature ones.