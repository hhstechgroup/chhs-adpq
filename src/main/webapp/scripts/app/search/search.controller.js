angular.module('apqdApp')
    .controller('SearchController', function ($scope, $log, CWSSearchService, searchString) {
        $scope.toShow = '';

        $scope.show = function (toShow) {
            $scope.toShow = toShow;
        };

        $scope.recordCount = 0;

        $scope.updateRecordCount = function () {
            $scope.recordCount = $scope.referrals.length + $scope.reporters.length + $scope.clients.length;
        };

        $scope.doSearch = function () {
            $scope.recordCount = 0;
            $scope.referrals = $scope.reporters = $scope.clients = [];

            CWSSearchService.search($scope.searchString, ['Referral', 'Reporter', 'Client']).then(function(results) {
                if (results.Referral == null) {
                    // there was nothing to search for
                    $scope.show('');

                } else {
                    $scope.referrals = results.Referral;
                    $scope.reporters = results.Reporter;
                    $scope.clients = results.Client;

                    //phonetic search is blocked by CWS-439
                    var searchCriteria = [];
                    searchCriteria.push({
                        field: 'commonFirstName.phonetic',
                        searchString: $scope.searchString,
                        matchExactWord: true
                    });
                    searchCriteria.push({
                        field: 'commonMiddleName.phonetic',
                        searchString: $scope.searchString,
                        matchExactWord: true,
                        combine: 'OR'
                    });
                    searchCriteria.push({
                        field: 'commonLastName.phonetic',
                        searchString: $scope.searchString,
                        matchExactWord: true
                    });

                    CWSSearchService.search(searchCriteria, 'Client').then(function(phoneticResults) {
                        mergeByProperty($scope.clients, phoneticResults.Client, "id");

                        $scope.updateRecordCount();
                        $scope.show('all');
                    });
                }

            });
        };

        function mergeByProperty(arr1, arr2, prop) {
            _.each(arr2, function(arr2obj) {
                var arr1obj = _.find(arr1, function (arr1obj) {
                    return arr1obj[prop] === arr2obj[prop];
                });

                if (_.isNil(arr1obj)) {
                    arr1.push(arr2obj);
                }
            });
        }

        /**
         *
         * @param entityTag can be: 'referral' or 'reporter' or 'client'
         * @param entity
         */
        $scope.showDetails = function (entityTag, entity) {
            $scope[entityTag] = entity;
            $scope.show(entityTag + 'Details');
        };

        // init
        //
        $scope.initPage = function () {
            if (searchString) {
                $scope.searchString = searchString;
                $scope.doSearch();
            }
        };
        $scope.initPage();
    });
