'use strict';

/*
Usage:
var queryBuilder = ElasticSearchStringQueryBuilder.newInstance().minTokenLength(5).matchExactWord();
if (searchByFirstName) {
    queryBuilder.searchByField('firstName');
} else {
    queryBuilder.searchAnyField();
}
var stringQuery = queryBuilder.buildStringQuery(searchString);
 */
angular.module('apqdApp')
    .factory('ElasticSearchStringQueryBuilder', [function () {
        function ElasticSearchStringQueryBuilder() {
            this.searchOptions = {
                // Minimum length that is required to qualify input search data as a subject to search for.
                minTokenLength: 3,
                matchAnyPrefix: false, // mutually exclusive with fuzzy
                matchAnySuffix: true, // mutually exclusive with fuzzy
                byField: '',
                combineOperator: 'OR',
                fuzziness: 0, // mutually exclusive with wildcards
                detectId: true,
                detectRecordStatus: true
            };

            this.minTokenLength = function (minTokenLength) {
                this.searchOptions.minTokenLength = minTokenLength;
                return this;
            };

            this.matchAnyPrefix = function (matchAnyPrefix) {
                this.searchOptions.matchAnyPrefix = matchAnyPrefix;
                if (matchAnyPrefix) {
                    this.fuzziness(0);
                }
                return this;
            };

            this.matchAnySuffix = function (matchAnySuffix) {
                this.searchOptions.matchAnySuffix = matchAnySuffix;
                if (matchAnySuffix) {
                    this.fuzziness(0);
                }
                return this;
            };

            this.matchAnyWordPart = function () {
                return this.matchAnyPrefix(true).matchAnySuffix(true);
            };

            this.matchExactWord = function () {
                return this.matchAnyPrefix(false).matchAnySuffix(false);
            };

            this.searchByField = function (field) {
                this.searchOptions.byField = field;
                return this;
            };

            this.searchAnyField = function () {
                return this.searchByField('');
            };

            this.combineWith = function (combineOperator) {
                this.searchOptions.combineOperator = combineOperator;
                return this;
            };

            this.fuzziness = function (fuzziness) {
                this.searchOptions.fuzziness = fuzziness;
                if (fuzziness) {
                    this.matchExactWord();
                }
                return this;
            };

            this.detectId = function (detectId) {
                this.searchOptions.detectId = detectId;
                return this;
            };

            this.detectRecordStatus = function (detectRecordStatus) {
                this.searchOptions.detectRecordStatus = detectRecordStatus;
                return this;
            };

            this._isFieldNameLikeId = function (fieldName) {
                fieldName = fieldName.toLowerCase();
                return fieldName === 'id' || fieldName === '+id' || fieldName === '-id' || new RegExp("\.id$").test(fieldName);
            };

            function _buildStringQuery(queryString, searchOptions) {
                return _(queryString.split(/\s+/)).filter(function (searchToken) {
                    // make it ignore words (search tokens) that are shorter than the given limit
                    return searchToken.length >= searchOptions.minTokenLength;

                }).map(function (token) {
                    if (searchOptions.matchAnyPrefix) {
                        token = '*' + token;
                    }
                    if (searchOptions.matchAnySuffix) {
                        token = token + '*';
                    }
                    if (searchOptions.fuzziness) {
                        token += '~' + searchOptions.fuzziness;
                    }
                    if (searchOptions.byField) {
                        token = searchOptions.byField + ':' + token;
                    }

                    return token;

                }).value().join(' '+ searchOptions.combineOperator +' ');
            }

            this.buildStringQuery = function (queryString) {
                if (!(angular.isDefined(queryString) && queryString)) {
                    return '';
                }

                if (this.searchOptions.detectId && this.searchOptions.byField
                    && this._isFieldNameLikeId(this.searchOptions.byField)
                ) {
                    this.minTokenLength(1);
                    this.matchExactWord();
                    this.fuzziness(0);
                }

                if (this.searchOptions.detectRecordStatus && this.searchOptions.byField === 'recordStatus') {
                    this.minTokenLength(1);
                    this.matchExactWord();
                    this.fuzziness(0);
                }

                return _buildStringQuery(queryString, this.searchOptions);
            }
        }

        return {
            newInstance: function () {
                return new ElasticSearchStringQueryBuilder();
            }
        };
    }]);
