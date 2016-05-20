'use strict';

angular.module('intakeApp')
    .factory('File',
        ['FileUpload', '$resource',
            function(FileUpload, $resource) {

                function uploadFile(file, username, successFunction, errorFunction){
                    return FileUpload.uploadFile(file, "api/file/upload", {username: username}, successFunction, errorFunction);
                }

                function getFileIds(username){
                    return $resource('/api/file/' + username + '/list');
                }

                return {
                    uploadFile: uploadFile,
                    getFileIds: getFileIds
                }
            }]);

