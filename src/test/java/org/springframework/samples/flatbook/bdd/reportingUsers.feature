Feature: Reporting users of the system (UH 22) 
   I can report the users who I think dont use the application correctly.

  Scenario: Successful user report as tenant (Positive)
    Given I am not logged in the system
    When I do login as user "rdunleavy0"
    And I go to my flat page
    And I go to my roomate user page
    Then I can report him	
    
Scenario: Fail user report because I dont have an account (Negative)
    Given I am not logged in the system
    When I do look for a flat in "Sevilla"
    And I go to the first flat page
    And I try to report the first tenant
    Then The report button doesnt exist