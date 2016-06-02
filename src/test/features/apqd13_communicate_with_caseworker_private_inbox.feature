Feature: Parent (Birth/Foster) communicate with the case worker via a private inbox to inform the worker about changes. I want to see the history of my communication. I will want to attach items to my communication.

  Scenario: Prepare test data

  Scenario: Open home page and register new user
    When open home page
    Given random alphabetic name with 'parentuser' and length '4' saved to 'parentuser' variable
    Then register new user with email '${parentuser}@mailinator.com', login '${parentuser}' and password 'password'
    When check confirmation letter for email '${parentuser}@mailinator.com'

  Scenario: Log in as parent and communicate with caseworker
    When open home page
    When login with login '${parentuser}' and password 'password'
    When open inbox page
    When compose and send new email to 'Worker Worker' with text 'Please send me approximate kids return date. I found new job and stopped to drink.', attach file ' '
    Then verify letter to 'Worker Worker' with text 'Please send me approximate kids return date. I found new job and stopped to drink.' is sent
    When log out

  Scenario: Login as worker and check email from parent
    When login with login 'worker' and password 'worker'
    When open inbox page
    Then verify unread letter from '${parentuser} ${parentuser}' has text 'Please send me approximate kids return date. I found new job and stopped to drink.'
    Then verify letter contains attachment
    When compose and send new email to '${parentuser} ${parentuser}' with text 'Kids will return not earlier than 1 month. Please provide paid electricity bill.', attach file ''
    When log out

  Scenario: Log in as parent and check email from caseworker
    When login with login '${parentuser}' and password 'password'
    When open inbox page
    Then verify unread letter from 'Worker Worker' has text 'Kids will return not earlier than 1 month. Please provide paid electricity bill.'

