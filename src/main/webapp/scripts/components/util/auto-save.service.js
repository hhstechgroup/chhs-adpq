'use strict';

angular.module('apqdApp')
    .service('AutoSaveService', function ($rootScope, DateUtils) {

        var ACTIVITY_AUTO_SAVE_INTERVAL = 10000;
        var INACTIVITY_AUTO_SAVE_INTERVAL = 500;
        var autoSaveInvoked = false;

        function invokeAutoSave(formScope) {
            var promise;
            autoSaveInvoked = true;
            formScope.$emit('apqdApp:savingInProgress');

            if (formScope.saveWithoutValidation) {
                promise = formScope.saveWithoutValidation(true);
            } else {
                promise = formScope.save(true);
            }

            if (!_.isUndefined(promise) && promise.then) {
                promise.then(function () {
                    onSuccess();
                });
            } else {
                onSuccess();
            }
        }

        function onSuccess() {
            $rootScope.$broadcast('apqdApp:autoSaveEvent', 'Last saved at ' + DateUtils.formatCurrentTime());
        }

        function setUpAutoSave(formScope, model4monitoring) {
            var inactivityMonitoringTimer = null;
            var activityMonitoringTimer = null;
            var changesExists = false;
            autoSaveInvoked = false;

            function startAutoSaveTimer() {
                return setTimeout(function () {

                    if (!changesExists && isFormValid()) {
                        invokeAutoSave(formScope);
                    }

                    inactivityMonitoringTimer = startInactivityMonitoringTimer();

                }, INACTIVITY_AUTO_SAVE_INTERVAL);
            }

            function startInactivityMonitoringTimer() {
                return setInterval(function () {

                    if (changesExists) {
                        changesExists = false;
                        clearInterval(inactivityMonitoringTimer);
                        inactivityMonitoringTimer = startAutoSaveTimer();
                        formScope.$emit('apqdApp:hasUnsavedChangesEvent');
                    }

                }, 200);
            }

            function startActivityMonitoringTimer() {
                return setInterval(function () {

                    if (!autoSaveInvoked && changesExists) {
                        invokeAutoSave(formScope);
                    }

                    autoSaveInvoked = false;

                }, ACTIVITY_AUTO_SAVE_INTERVAL);
            }

            function isFormValid() {
                if (formScope.saveWithoutValidation) {
                    return true;
                }

                var form = null;
                _.each(formScope, function (field) {
                    if (_.has(field, '$name')) {
                        form = field;
                        return false;
                    }
                });

                return (!_.isNull(form) && !form.$invalid);
            }

            formScope.$watch(model4monitoring, function() {
                changesExists = true;
            }, true);

            formScope.$on("$destroy", function () {
                clearInterval(inactivityMonitoringTimer);
                clearInterval(activityMonitoringTimer);
            });

            inactivityMonitoringTimer = startInactivityMonitoringTimer();
            activityMonitoringTimer = startActivityMonitoringTimer();
        }

        return {
            setUpAutoSave: setUpAutoSave,
            invokeAutoSave : invokeAutoSave
        }
    });
