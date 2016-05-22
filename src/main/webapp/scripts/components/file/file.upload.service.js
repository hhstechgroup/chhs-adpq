'use strict';

angular.module('apqdApp')
    .factory('FileUpload',
        ['$http',
        function($http) {

            function uploadFile(file, uploadUrl, params, successFunction, errorFunction){
                var fd = new FormData();

                fd.append('file', file);

                for (var key in params) {
                    if (params.hasOwnProperty(key) && params[key]) {
                        fd.append(key, params[key]);
                    }
                }

                $http.post(uploadUrl, fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                .success(successFunction)
                .error(errorFunction);
            }

            return {
                uploadFile: uploadFile
            }
}]);
