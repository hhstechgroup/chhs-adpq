'use strict';

angular.module('intakeApp')
    .constant('SecurityRole', {
        ADMIN: 'ROLE_ADMIN',
        INTAKE_WORKER: 'ROLE_INTAKE_WORKER',
        INVESTIGATOR: 'ROLE_INVESTIGATOR'
    })
    .constant('RecordStatus', {
        DRAFT: null,
        COMPLETED: 'CL',
        DELETED: 'DL',
        CLOSED: 'CLOSED'
    })
    .constant('GenderCode', {
        UNKNOWN: 'U',
        MALE: 'M',
        FEMALE: 'F'
    })
    .constant('InvestigationContactStatusCode', {
        // according to LookupServiceStatus
        IN_PROGRESS: 'I',
        SCHEDULED: 'S',
        COMPLETED: 'C',
        ATTEMPTED: 'A'
    })
    .constant('InvestigationContactActionTypeCode', {
        // according to LookupContactActionType
        NOT_CHOSEN: '1',
        VISIT: '2',
        CALL: '3',
        EMAIL: '4',
        FAX: '5'
    })
    .constant('uibCustomDatepickerConfig', {
        formatYear: 'yyyy',
        startingDay: 1,
        showWeeks: false
    })
    .constant('cwsCustomScrollConfig', {
        autoHideScrollbar: false,
        theme: '3d-thick',
        advanced: {
            updateOnContentResize: true
        },
        mouseWheel:{ preventDefault: true },
        scrollButtons:{ enable: true },
        scrollInertia: 0
    })
    .constant('ReferralResponseCode', {
        INVESTIGATE_24HOURS: '0003',
        INVESTIGATE_10DAYS: '0001'
    })

;
