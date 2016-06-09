Feature: Parent (Birth/Foster) communicate with the case worker via a private inbox to inform the worker about changes. I want to see the history of my communication. I will want to attach items to my communication.

  Scenario: Prepare test data

  Scenario: Open home page and register new user
    When open home page
    Given random alphabetic name with 'parentuser' and length '4' saved to 'parentuser' variable
    Then register new user with email '${parentuser}', login '${parentuser}' and password 'password'
    When check confirmation letter for email '${parentuser}'

  Scenario: Log in as parent and communicate with caseworker
    When open home page
    When login with login '${parentuser}' and password 'password'
    When open inbox page
    When compose and send new email to 'CHHS Support' with subject 'Status Update' and text 'Please send me approximate kids return date. I found new job and stopped to drink.', attach file ' '
    Then verify letter to 'CHHS Support' with subject 'Status Update' and text 'Please send me approximate kids return date. I found new job and stopped to drink.' is sent

  Scenario: Login as worker and check email from parent
    When log out
    When login with login 'worker' and password 'worker'
    When open inbox page
    When compose and send new email to '${parentuser} ${parentuser}' with subject 'Response from Worker' and text 'Kids will return not earlier than 1 month. Please provide paid electricity bill.', attach file ''
    Then verify unread letter from '${parentuser} ${parentuser}' has subject 'Status Update' and text 'Please send me approximate kids return date. I found new job and stopped to drink.'
    Then verify letter contains attachment

  Scenario: Log in as parent and check email from caseworker
    When log out
    When login with login '${parentuser}' and password 'password'
    When open inbox page
    Then verify unread letter from 'CHHS Support' has subject 'Response from Worker' and text 'Kids will return not earlier than 1 month. Please provide paid electricity bill.'

