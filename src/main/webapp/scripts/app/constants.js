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

;
