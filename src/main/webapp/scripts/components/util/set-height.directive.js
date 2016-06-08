angular.module('apqdApp')
    .directive('setHeight', function($window){
        return{
            priority: '3',
            link: function(scope, element, attrs) {
                var setElHeight = function() {
                    var windowHeight = $(window).height(),
                        elPositionRelativeDoc = $(element).offset().top,
                        indentSize = 30,
                        calcElHeight = windowHeight - elPositionRelativeDoc - indentSize + "px";
                    $(element).css('height', calcElHeight );
                };
                setElHeight();
                $(window).resize(setElHeight);
            }
        }
    });
