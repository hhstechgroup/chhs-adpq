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
        ADOPTION_AGENCY: {id: '01ft', name: 'ADOPTION AGENCY', label: 'adoption'},
        FOSTER_FAMILY_AGENCY: {id: '02ft', name: 'FOSTER FAMILY AGENCY', label: 'foster'},
        FOSTER_FAMILY_AGENCY_SUB: {id: '03ft', name: 'FOSTER FAMILY AGENCY SUB', label: 'foster_sub'}
    })
    .constant('chCustomScrollConfig', {
        autoHideScrollbar: false,
        theme: 'inset-2-dark',
        advanced: {
            updateOnContentResize: true
        },
        scrollInertia: 0
    })
    .constant('FacilityStatus', {
        LICENSED: {id: '01fs', name: 'LICENSED', color: 'green'},
        CLOSED: {id: '02fs', name: 'CLOSED', color: 'red'},
        PENDING: {id: '03fs', name: 'PENDING', color: 'yellow'},
        UNLICENSED: {id: '04fs', name: 'UNLICENSED', color: 'yellow'}
    });
