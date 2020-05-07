@ignore
Feature: User Sign Up (UH 1) 
   I can sign up in the system as a tenant or host with my user name, password and personal information.
  
  Scenario: Successful sign up as Tenant (Positive)
    Given I am not logged in the system
    When I do register as "tenant" with the username "testing"
    And I do login as user "testing"
    Then "testing" appears as the current user
   
  Scenario: Successful sign up as Host (Positive)
    Given I am not logged in the system
    When I do register as "host" with the username "testinghost"
    And I do login as user "testinghost"
    Then "testinghost" appears as the current user
  
  Scenario: Login fail (Negative)
  	Given I am not logged in the system
    When I try to sign up as "tenant" with user "baduser" with wrong information
    Then the sing up form is shown again