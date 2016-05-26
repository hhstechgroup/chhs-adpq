'use strict';

angular.module('apqdApp')
    .constant('SecurityRole', {
        ADMIN: 'ROLE_ADMIN',
        INTAKE_WORKER: 'ROLE_INTAKE_WORKER',
        INVESTIGATOR: 'ROLE_INVESTIGATOR'
    })
    .constant('uibCustomDatepickerConfig', {
        formatYear: 'yyyy',
        startingDay: 1,
        showWeeks: false
    })

;
