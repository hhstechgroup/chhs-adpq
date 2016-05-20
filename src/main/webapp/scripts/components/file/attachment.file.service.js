'use strict';

angular.module('intakeApp')
    .factory('AttachmentFile',
            ['FileUpload',
            function(FileUpload) {

                function uploadFile(file, username, referralId, description, successFunction, errorFunction){
                    return FileUpload.uploadFile(
                        file,
                        "api/attachmentFile/upload",
                        {username: username, referralId: referralId, description: description},
                        successFunction, errorFunction
                    );
                }

                return {
                    uploadFile: uploadFile
                }
            }]);
