'use strict';

angular.module('apqdApp')
    .filter('fileSizeDisplay', [function(){
        return function(bytes, decimals) {
            if(bytes == 0) return '0 Byte';
            if(!decimals)decimals = 3;
            var k = 1000; // or 1024 for binary
            var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
            var i = Math.floor(Math.log(bytes) / Math.log(k));
            return parseFloat((bytes / Math.pow(k, i)).toFixed(decimals)) + ' ' + sizes[i];
        };
    }]);
