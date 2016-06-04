'use strict';

angular.module('apqdApp')
    .controller('AccessDeniedController',
        ['$scope', '$state', 'Principal',
            function ($scope, $state, Principal) {

                Principal.identity(true).then(function(account) {

                    if (!_.isObject(account)) {
                        $state.go("login");
                    }

                });
                return this;
            }
        ]);
