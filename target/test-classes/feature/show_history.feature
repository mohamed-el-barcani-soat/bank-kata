Feature:
  As a user
  In order to see the history of my account operations
  I want to see a list of my transactions

  Scenario Outline: User can see a list of transactions
    Given I have 1000.00 on my account
    And I have made a deposit of 1000.00 on 10,01,2012
    And I have made a deposit of 2000.00 on 13,01,2012
    And I have made a withdrawal of 500.00 on 14,01,2012
    When I show history
    Then I should have 3 transactions in my history
    And for every <transactionId> I would see a transaction containing exactly a <date>, a type of operation <credit> or <debit> and its amount, And the <balance>
    Examples:
      | transactionId | date       | credit | debit  | balance |
      | 10-01-2012    | 10/01/2012 | 1000.0 | 0      | 2000.00 |
      | 13-01-2012    | 13/01/2012 | 2000.0 | 0      | 4000.00 |
      | 14-01-2012    | 14/01/2012 | 0      | 500.00 | 3500.00 |