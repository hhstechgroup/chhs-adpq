Feature: APQD-96 Check twitter widget on landing page

  Scenario: Open home page and check tweets in the widget
    When open home page
    Then tweet with text ' ' from 'CW Digital Services' should be presented