Feature: APQD-137  I want to be able to put city or zip code in address lookup field and hit enter to receive information.
  APQD-107 As a user, I want to be able from Facilities page to ask about some facility using private inbox.

  Scenario: Log in as admin and verify admin functionality
    When open home page
    When login with login 'parent' and password 'parent'
    When open facilities page
    When search 'Palo Alto,' in facility address search
    Then verify facility with address '4043 El Camino Way, Palo Alto, CA 94306' and name 'AFRICAN CRADLE, INC.' presents in the list
    When do Ask About for facility with address '4043 El Camino Way, Palo Alto, CA 94306' and name 'AFRICAN CRADLE, INC.' and send letter
    When open facilities page
    When open inbox page
    When verify letter to 'CHHS Support' with subject 'AFRICAN CRADLE, INC.' and text 'I am interested in more information about' is sent

