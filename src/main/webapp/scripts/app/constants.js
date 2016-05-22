'use strict';

angular.module('apqdApp')
    .constant('SecurityRole', {
        ADMIN: 'ROLE_ADMIN',
        INTAKE_WORKER: 'ROLE_INTAKE_WORKER',
        INVESTIGATOR: 'ROLE_INVESTIGATOR'
    })
;
