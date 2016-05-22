'use strict';

angular.module('apqdApp')
    .service('ElasticSearchIndexService', ['$http', '$log', function ($http, $log) {
        /**
         * This performs authorized version of HTTP POST like the following:
         *      > curl -X POST --header "Content-Type: application/json" --header "Accept: text/plain" --header "X-CSRF-TOKEN: 00000000-0000-0000-0000-000000000000" "http://localhost:8080/api/elasticsearch/index"
         *
         * This functionality depends on ElasticsearchIndexService.java which, in turn, should always be synchronized
         * after each model change by the following command:
         *      > yo jhipster-elasticsearch-reindexer
         *
         * @param successResponseCallback
         * @param errorResponseCallback
         */
        this.reindexAll = function (successResponseCallback, errorResponseCallback) {
            $http.post('api/elasticsearch/index', '', {
                'X-CSRF-TOKEN': '00000000-0000-0000-0000-000000000000'
            }).then(
                angular.isDefined(successResponseCallback) ? successResponseCallback
                    : function (response) {
                    $log.info('reindexAll POST status: '+ response.data);
                },
                angular.isDefined(errorResponseCallback) ? errorResponseCallback
                    : function (response) {
                    $log.error('reindexAll POST failed: '+ response.data);
                }
            );
        };
    }]);
