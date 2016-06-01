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
        ADOPTION_AGENCY: {code: 'aa', name: 'ADOPTION AGENCY'},
        FOSTER_FAMILY_AGENCY: {code: 'fa', name: 'FOSTER FAMILY AGENCY'},
        FOSTER_FAMILY_AGENCY_SUB: {code: 'fs', name: 'FOSTER FAMILY AGENCY SUB'}
    })
    .constant('FacilityStatus', {
        LICENSED: {code: 'li', name: 'LICENSED'},
        CLOSED: {code: 'cl', name: 'CLOSED'},
        PENDING: {code: 'pe', name: 'PENDING'},
        UNLICENSED: {code: 'un', name: 'UNLICENSED'}
    });
