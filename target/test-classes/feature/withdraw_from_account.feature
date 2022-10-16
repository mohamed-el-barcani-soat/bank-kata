Feature: Withdraw from Account

  As a user
  In order to withdraw money from my account
  I want to withdraw money from my account

  Scenario: User have enough money in account
    Given I have 1000.00 in my account
    When I withdraw 500.00
    Then I should have 500.00 in my account
    #And I should see a message "You have withdrawn 500.00 from your account"

  Scenario: User have not enough money in account
    Given I have 1000.00 in my account
    When I withdraw 1500.00
    Then I should have 1000.00 in my account
    And I should see an error message "Insufficient funds in your account"
