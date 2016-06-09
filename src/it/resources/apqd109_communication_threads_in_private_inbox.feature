Feature: APQD-109 As a user, I want to see communication threads in my private Inbox

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
    When compose and send new email to 'CHHS Support' with subject 'Out of State travel' and text 'Our family is planning to go to the Grand Canyon for the Memorial Day weekend. Stella and Lucas will need to be placed temporarily while we go out of town.', attach file ' '
    Then verify letter to 'CHHS Support' with subject 'Out of State travel' and text 'Our family is planning to go to the Grand Canyon for the Memorial Day weekend. Stella and Lucas will need to be placed temporarily while we go out of town.' is sent

  Scenario: Login as worker and check email from parent
    When log out
    When login with login 'worker' and password 'worker'
    When open inbox page
    Then verify unread letter from '${parentuser} ${parentuser}' has subject 'Out of State travel' and text 'Our family is planning to go to the Grand Canyon for the Memorial Day weekend. Stella and Lucas will need to be placed temporarily while we go out of town.'
    When reply to '${parentuser} ${parentuser}' with text 'I wish you could have given me more notice. Let me see what can be arranged and I’ll get back to you. When do you hope to leave and when do you plan to be back?'

  Scenario: Log in as parent and check email from caseworker
    When log out
    When login with login '${parentuser}' and password 'password'
    When open inbox page
    Then verify unread letter from 'CHHS Support' has subject 'Out of State travel' and text 'I wish you could have given me more notice. Let me see what can be arranged and I’ll get back to you. When do you hope to leave and when do you plan to be back?'
    Then verify letter in thread from '${parentuser} ${parentuser}' has text 'Our family is planning to go to the Grand Canyon for the Memorial Day weekend. Stella and Lucas will need to be placed temporarily while we go out of town.'
    Then verify letter in thread from 'CHHS Support' has text 'I wish you could have given me more notice. Let me see what can be arranged and I’ll get back to you. When do you hope to leave and when do you plan to be back?'
    When reply to '${parentuser} ${parentuser}' with text 'We are hoping to leave around 4pm on Friday, May 27 and then return late (10pm?) on Monday.'

  Scenario: Login as worker and check email from parent
    When log out
    When login with login 'worker' and password 'worker'
    When open inbox page
    Then verify unread letter from '${parentuser} ${parentuser}' has subject 'Out of State travel' and text 'We are hoping to leave around 4pm on Friday, May 27 and then return late (10pm?) on Monday.'
    Then verify letter in thread from '${parentuser} ${parentuser}' has text 'Our family is planning to go to the Grand Canyon for the Memorial Day weekend. Stella and Lucas will need to be placed temporarily while we go out of town.'
    Then verify letter in thread from 'CHHS Support' has text 'I wish you could have given me more notice. Let me see what can be arranged and I’ll get back to you. When do you hope to leave and when do you plan to be back?'
    Then verify letter in thread from '${parentuser} ${parentuser}' has text 'We are hoping to leave around 4pm on Friday, May 27 and then return late (10pm?) on Monday.'
    When reply to '${parentuser} ${parentuser}' with text 'Lucas will be going to his grandmother’s for a temporary stay while you are away. Lucas’ caseworker, Jim, will pick him up from school on Friday.'

  Scenario: Log in as parent and check email from caseworker
    When log out
    When login with login '${parentuser}' and password 'password'
    When open inbox page
    Then verify unread letter from 'CHHS Support' has subject 'Out of State travel' and text 'Lucas will be going to his grandmother’s for a temporary stay while you are away. Lucas’ caseworker, Jim, will pick him up from school on Friday.'
    Then verify letter in thread from '${parentuser} ${parentuser}' has text 'Our family is planning to go to the Grand Canyon for the Memorial Day weekend. Stella and Lucas will need to be placed temporarily while we go out of town.'
    Then verify letter in thread from 'CHHS Support' has text 'I wish you could have given me more notice. Let me see what can be arranged and I’ll get back to you. When do you hope to leave and when do you plan to be back?'
    Then verify letter in thread from '${parentuser} ${parentuser}' has text 'We are hoping to leave around 4pm on Friday, May 27 and then return late (10pm?) on Monday.'
    Then verify letter in thread from 'CHHS Support' has text 'Lucas will be going to his grandmother’s for a temporary stay while you are away. Lucas’ caseworker, Jim, will pick him up from school on Friday.'
