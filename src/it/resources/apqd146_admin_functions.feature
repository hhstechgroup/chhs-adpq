Feature: APQD-146 As a system administrator, I want to be able to reach administration functions from menu

  Scenario: Log in as admin and verify admin functionality
    When open home page
    When login with login 'admin' and password 'admin'
    When open and verify Metrics page
    When open and verify Users page
    When open and verify Tracker page
    When open and verify Health page
    When open and verify Configuration page
    When open and verify Audit page
    When open and verify Logs page