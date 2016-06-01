'use strict';

angular.module('apqdApp')
    .constant('SecurityRole', {
        ADMIN: 'ROLE_ADMIN',
        CASE_WORKER: 'CASE_WORKER',
        PARENT: 'PARENT'
    })
    .constant('uibCustomDatepickerConfig', {
        formatYear: 'yyyy',
        startingDay: 1,
        showWeeks: false
    })
    .constant('FacilityType', {
        ADOPTION_AGENCY: 'ADOPTION AGENCY',
        FOSTER_FAMILY_AGENCY: 'FOSTER FAMILY AGENCY',
        FOSTER_FAMILY_AGENCY_SUB: 'FOSTER FAMILY AGENCY SUB'
    })
    .constant('FacilityStatus', {
        LICENSED: 'LICENSED',
        CLOSED: 'CLOSED',
        PENDING: 'PENDING'
    });
