'use strict';

angular.module('intakeApp')
    .directive('activeMenu', function($translate, $locale, tmhDynamicLocale) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var language = attrs.activeMenu;

                scope.$watch(function() {
                    return $translate.use();
                }, function(selectedLanguage) {
                    if (language === selectedLanguage) {
                        tmhDynamicLocale.set(language);
                        element.addClass('active');
                    } else {
                        element.removeClass('active');
                    }
                });
            }
        };
    })
    .directive('activeLink', function(location) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var clazz = attrs.activeLink;
                var path = attrs.href;
                path = path.substring(1); //hack because path does bot return including hashbang
                scope.location = location;
                scope.$watch('location.path()', function(newPath) {
                    if (path === newPath) {
                        element.addClass(clazz);
                    } else {
                        element.removeClass(clazz);
                    }
                });
            }
        };
    })
    .directive('mainNavSwitcher', function() {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var mainNavContainer = $(element);

                function extendMainNav() {
                    if (mainNavContainer.hasClass('extended-nav')) {
                        return;
                    } else {
                        mainNavContainer.addClass('extended-nav');
                    }
                }


                function showOverlay() {
                    if (!$('.layout-overlay').length) {
                        $('body').append('<div class="layout-overlay" />');
                    }
                }

                function hideOverlay() {
                    if ($('.layout-overlay').length) {
                        $('.layout-overlay').remove();
                    }
                }

                function closeNavBar() {
                    if (mainNavContainer.hasClass('extended-nav')){
                        mainNavContainer.removeClass('extended-nav');
                        mainNavContainer.find('.open').removeClass('open');
                        hideOverlay();
                    }
                }

                mainNavContainer.on('click', '.navbar-toggle', function(e){
                        e.preventDefault();
                        extendMainNav();
                        showOverlay();
                });

                mainNavContainer.on('click', '.nav-pane-general .dropdown-toggle', function(e) {
                    e.preventDefault();
                    if (mainNavContainer.hasClass('extended-nav')){
                        return;
                    } else {
                        extendMainNav();
                        showOverlay();
                    }
                });

                mainNavContainer.on('click', '.nav-pane-general .dropdown-sublist li', function(e) {
                    e.stopPropagation();
                });

                $('.navbar-brand').on('click', closeNavBar);

                $(document).on('click', function(event) {
                    if (!$(event.target).closest(mainNavContainer).length) {
                        closeNavBar();
                    }
                });

                mainNavContainer.hover(function() {
                    if (!_.isNil(this.closeNavTimerID)){
                        clearTimeout(this.closeNavTimerID);
                    }
                }, function() {
                    if (mainNavContainer.hasClass('extended-nav')){
                        this.closeNavTimerID = setTimeout(function() {
                            closeNavBar();
                        }, 1500);
                    }
                });

            }
        };
    });
