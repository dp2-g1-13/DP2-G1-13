Feature: Traditional Login 
   I can do login in the system with my user name and password.
  
  Scenario: Successful login as admin (Positive)
    Given I am not logged in the system
    When I do login as user "admin"
    Then "admin" appears as the current user
    
  Scenario: Login fail (Negative)
  	Given I am not logged in the system
    When I try to do login as user "admin" with an invalid password
    Then the login form is shown again