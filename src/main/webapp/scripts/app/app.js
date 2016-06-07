'use strict';

angular.module('apqdApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate',
    'ngResource', 'ngCookies', 'ngAria', 'ngCacheBuster', 'ngFileUpload', 'ngToast', 'infinite-scroll',
    // jhipster-needle-angularjs-add-module JHipster will add new module
    'ui.bootstrap', 'ui.bootstrap.datetimepicker', 'ui.router',  'infinite-scroll', 'angular-loading-bar',
    'ui.select', 'ngSanitize', 'ui.mask', 'ngScrollbars', 'sticky', 'ui-leaflet'])

    .run(function ($rootScope, $location, $window, $http, $state, $translate, Language,
                   Auth, Principal, ENV, VERSION) {
        // update the window title using params in the following
        // precendence
        // 1. titleKey parameter
        // 2. $state.$current.data.pageTitle (current state page title)
        // 3. 'global.title'
        var updateTitle = function(titleKey) {
            if (!titleKey && $state.$current.data && $state.$current.data.pageTitle) {
                titleKey = $state.$current.data.pageTitle;
            }
            $translate(titleKey || 'global.title').then(function (title) {
                $window.document.title = title;
            });
        };

        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

            // Update the language
            Language.getCurrent().then(function (language) {
                $translate.use(language);
            });

        });

        var history = [];
        $rootScope.backToPreviousState = function () {
            var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";
            $location.path(prevUrl);
        };

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            history.push($location.$$path);

            var titleKey = 'global.title' ;

            // Remember previous state unless we've been redirected to login or we've just
            // reset the state memory after logout. If we're redirected to login, our
            // previousState is already set in the authExpiredInterceptor. If we're going
            // to login directly, we don't want to be sent to some previous state anyway
            if (toState.name !== 'login' && $rootScope.previousStateName) {
              $rootScope.previousStateName = fromState.name;
              $rootScope.previousStateParams = fromParams;
            }

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }
            updateTitle(titleKey);
        });

        // if the current translation changes, update the window title
        $rootScope.$on('$translateChangeSuccess', function() { updateTitle(); });


        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                Principal.hasAuthority('ROLE_ADMIN')
                    .then(function (result) {
                        if (result) {
                            $state.go('metrics');
                        } else {
                            $state.go('ch-inbox.messages', {directory: 'inbox'}, {reload: true});
                        }
                    });
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };

        $rootScope.goMainPage = function() {
            Principal.hasAuthority('CASE_WORKER').then(function(has) {
                if (has) {
                    $state.go('ch-inbox.messages', {directory: 'inbox'}, {reload: true});
                }
            });

            Principal.hasAuthority('PARENT').then(function(has) {
                if (has) {
                    $state.go('ch-inbox.messages', {directory: 'inbox'}, {reload: true});
                }
            });

            Principal.hasAuthority('ROLE_ADMIN').then(function(has) {
                if (has) {
                    $state.go('metrics', {}, {reload: true});
                }
            });
        };

        $rootScope.hasAnyAuthority = function(authority) {
            return Principal.hasAnyAuthority(authority);
        }
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {
        //enable CSRF
        $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'header@': {
                    templateUrl: 'scripts/components/header/header.html'
                },
                'main.nav@site': {
                    templateUrl: 'scripts/components/main-nav/main.nav.html',
                    controller: 'MainNavController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });

        $httpProvider.interceptors.push('errorHandlerInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');
        $httpProvider.interceptors.push('notificationInterceptor');

        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('en');
        $translateProvider.useCookieStorage();
        $translateProvider.useSanitizeValueStrategy('escaped');
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage();
        tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

    })
    // jhipster-needle-angularjs-add-config JHipster will add new application configuration
    .config(['$urlMatcherFactoryProvider', function($urlMatcherFactory) {
        $urlMatcherFactory.type('boolean', {
            name : 'boolean',
            decode: function(val) { return val === true || val === "true"; },
            encode: function(val) { return val ? 1 : 0; },
            equals: function(a, b) { return this.is(a) && a === b; },
            is: function(val) { return [true,false,0,1].indexOf(val) >= 0; },
            pattern: /bool|true|0|1/
        });
    }]);
