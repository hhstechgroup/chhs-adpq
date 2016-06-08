'use strict';

angular.module('apqdApp')
    .service('DateUtils', function ($filter) {

        this.convertLocaleDateToServer = function (date) {
            if (date) {
                return $filter('date')(date, 'yyyy-MM-dd');
            } else {
                return null;
            }
        };

        this.convertLocaleDateFromServer = function (date) {
            if (date) {
                var dateString = date.split("-");
                return new Date(dateString[0], dateString[1] - 1, dateString[2]);
            }
            return null;
        };

        this.convertDateTimeFromServer = function (date) {
            if (date) {
                return new Date(date);
            } else {
                return null;
            }
        };

        // common date format for all date input fields
        this.dateformat = function () {
            return 'yyyy-MM-dd';
        };

        this.isSameDay = function (date1, date2) {
            if (_.isNil(date1) || _.isNil(date2)) {
                return false;
            } else {
                date1 = new Date(date1);
                date2 = new Date(date2);
                return date1.getFullYear() === date2.getFullYear() && date1.getMonth() === date2.getMonth()
                    && date1.getDate() === date2.getDate();
            }
        };

        this.formatCurrentTime = function () {
            var date = new Date();
            var hours = date.getHours() > 12 ? date.getHours() - 12 : date.getHours();
            var am_pm = date.getHours() >= 12 ? "PM" : "AM";
            var minutes = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
            var seconds = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
            var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
            var month = date.getMonth() + 1;
            var formattedMonth = month < 10 ? "0" + month : month;

            var time = " " + hours + ":" + minutes + ":" + seconds + " " + am_pm;
            return formattedMonth + '/' + day + '/' + date.getFullYear() + time;
        };
    })
    .filter('formatMailDate', ['DateUtils', function (DateUtils) {
        return function (mail) {
            var dateAsString;
            var ONE_DAY = 24 * 60 * 60 * 1000;

            if (!_.isNil(mail.dateUpdated)) {
                dateAsString = mail.dateUpdated;
            } else if (!_.isNil(mail.dateCreated)) {
                dateAsString = mail.dateCreated;
            }

            var pattern = "YYYY-MM-DD'T'HH:mm:ss'Z'";
            var date = DateUtils.convertDateTimeFromServer(dateAsString);
            if (new Date().getTime() - date.getTime() < ONE_DAY) {
                return moment(dateAsString, pattern).format('LT');
            } else {
                return moment(dateAsString, pattern).format('ll');
            }
        }
    }]);
