Feature: Deposit in account

    As a user
    In order to save money
    I want to make a deposit in my account

    Scenario: Make a deposit
      Given user has 1000 in his account
      When user makes a deposit of 1000
      Then user should have 2000 in his account