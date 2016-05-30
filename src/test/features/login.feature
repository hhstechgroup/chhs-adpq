Feature: Register new user and log in

  Scenario: Prepare test data

  Scenario: Open home page and register new user
    When open home page
    Given random alphabetic name with 'testuser' and length '4' saved to 'testuser' variable
    Then register new user with email 'testuser53@mailinator.com', login '${testuser}' and password 'password'
    When check confirmation letter for email 'testuser50@mailinator.com'

  Scenario: log in
    When open home page
    When login with login 'testuser50' and password 'password'