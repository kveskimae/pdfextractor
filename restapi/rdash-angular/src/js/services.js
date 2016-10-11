/*
 * Copyright (c) 2016 Kristjan Veskimae
 *
 *     Permission is hereby granted, free of charge, to any person obtaining
 *     a copy of this software and associated documentation files (the "Software"),
 *     to deal in the Software without restriction, including without limitation
 *     the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *     and/or sell copies of the Software, and to permit persons to whom the Software
 *     is furnished to do so, subject to the following conditions:
 *
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *     EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *     OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *     IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *     CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *     TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 *     OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

// bootstrap

angular.module('RDash').factory('BootstrapData', ['$resource',
    function($resource){
        return $resource('/rest/bootstrapdata', null, {
            get: {method: 'GET'}
        });
    }]);

// client-edit

angular.module('RDash').factory('ClientEdit', ['$resource',
    function($resource){
        return $resource('/rest/users/:id', null, {
            put: {method:'PUT', params:{id:'id'}, isArray:false},
            delete: {method:'DELETE', params:{id:'id'}}
        });
    }]);

// client-stats

angular.module('RDash').factory('ClientStats', ['$resource',
    function($resource){
        return $resource('/rest/statistics/:id', null, {
            query: {method:'GET', params:{id:'id'}, isArray:false}
        });
    }]);

angular.module('RDash').factory('ClientStatsPeriod', ['$resource',
    function($resource){
        return $resource('/rest/statistics/:id/period/:start/:end', null, {
            query: {method:'GET', params:{id:'id', start: 'start', end: 'end'}, isArray:false}
        });
    }]);

// clients

angular.module('RDash').factory('Clients', ['$resource',
    function($resource){
        return $resource('/rest/users/all/:authority', null, {
            get: {method: 'GET', params: {} , isArray : true},
            post: {method: 'POST', params: {} , isArray:false}
        });
    }]);

// invoice-review

angular.module('RDash').factory('UpdateWorkflow', ['$resource',
        function($resource){
            return $resource('/rest/review/:id', null, {
                update: {method:'PUT', params:{id:'id'}, isArray:false}
            });
        }]
);

// invoices-pending

angular.module('RDash').factory(
    'PollWorkflows', ['$resource',
        function($resource){
            return $resource('/rest/review/poller/:locale/:state/:posixTime', null, {
                query: {method:'GET', params:{}, isArray:false}
            });
        }]
);

// invoices-trial

angular.module('RDash').factory('PollTrials', ['$resource',
    function($resource){
        return $resource('/rest/trials/:locale/:posixTime', null, {
            query: {method:'GET', params:{}, isArray:false}
        });
    }]);

// phrases

angular.module('RDash').factory('Phrases', ['$resource',
    function($resource){
        return $resource('/rest/phrasetypes/:locale/:paymentFieldType', null, {
            get: {method:'GET', isArray:true},
            post: {method: 'POST', params: {} , isArray:false}
        });
    }]);

angular.module('RDash').factory('PhraseEdit', ['$resource',
    function($resource){
        return $resource('/rest/phrasetypes/:id', null, {
            put: {method:'PUT', params:{id:'id'}, isArray:false},
            delete: {method:'DELETE', params:{id:'id'}}
        });
    }]);

// profile

angular.module('RDash').factory('ProfileEmailEdit', ['$resource',
    function($resource){
        return $resource('/rest/profile/email', null, {
            put: {method:'PUT', params:{}, isArray:false}
        });
    }]);

angular.module('RDash').factory('ProfilePasswordEdit', ['$resource',
    function($resource){
        return $resource('/rest/profile/password', null, {
            put: {method:'PUT', params:{}, isArray:false}
        });
    }]);

// reload

angular.module('RDash').factory('Reloader', ['$resource',
    function($resource){
        return $resource('/rest/reload', null, {
            reload: {method: 'POST'}
        });
    }]);

// settings

angular.module('RDash').factory('AllSettings', ['$resource',
    function($resource){
        return $resource('/rest/settings', null, {
            query: {method:'GET', params:{}, isArray:true}
        });
    }]);

angular.module('RDash').factory('SaveSetting', ['$resource',
    function($resource){
        return $resource('/rest/settings/:settingType', null, {
            put: {method:'PUT', params:{settingType: 'settingType'}}
        });
    }]);

angular.module('ErrorCatcher', []).factory('errorHttpInterceptor', ['$exceptionHandler', '$q', '$rootScope', '$injector', 'AppData', function ($exceptionHandler, $q, $rootScope, $injector, AppData) {
        return {
            responseError: function responseError(rejection) {
                switch (rejection.status) {
                    case -1:
                        AppData.alertsData.addErrorAlert("Contacting server failed");
                        break;
                    case 302: // TODO server should return normal codes
                    case 401:
                        var $state = $injector.get("$state");
                        AppData.alertsData.addErrorAlert("You are not logged in");
                        $state.go("login");
                        break;
                    default:
                        var msgToDisplay = rejection.status + " " + rejection.statusText;
                        if (!!rejection.data && !!rejection.data.errorMsg) {
                            msgToDisplay = msgToDisplay + " : " + rejection.data.errorMsg;
                        }
                        AppData.alertsData.addErrorAlert(msgToDisplay);
                        break;
                }
                // $exceptionHandler("An HTTP request error has occurred.\nHTTP config: " + rejection.config + ".\nStatus: " + rejection.status);
                $exceptionHandler(rejection);
                return $q.reject(rejection);
            }
        };
    }])
    .config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push('errorHttpInterceptor');
    }]);

angular.module('RDash').factory('AppData', ['lodash', function(lodash){
    var self = {};
    self.loginData = {
        showError: false,
        loggedIn: false,
        username: null,
        email: null,
        trialLimit: null,
        isReviewer: false,
        isAdmin: false,
        isClient: false,
        authorities: [],
        login: function (data) {
            self.loginData.showError = false;
            self.loginData.loggedIn = true;
            self.loginData.username = data.username;
            self.loginData.email = data.email;
            self.loginData.authorities = data.authorities;
            self.dashboardData.noOfCompletedToday = data.completedToday;
            self.loginData.isReviewer = lodash.includes(self.loginData.authorities, 'REVIEWER');
            self.loginData.isAdmin = lodash.includes(self.loginData.authorities, 'ADMIN');
            self.loginData.isClient = lodash.includes(self.loginData.authorities, 'CLIENT');
            if (self.loginData.isClient) {
                self.loginData.trialLimit = data.trialLimit;
            }
        }
    };
    self.otherUsersData = {
        clients: null,
        findClientById: function (id) {
            return lodash.find(self.otherUsersData.clients, function(o) {
                return o.id == id;
            });
        }
    };
    self.alertsData = {
        alerts : [],
        addErrorAlert : function (errorMsg) {
            self.alertsData.alerts.push({type: 'danger', msg: errorMsg});
        },
        addSuccessAlert : function (successMsg) {
            self.alertsData.alerts.push({type: 'success', msg: successMsg});
        },
        closeAlert : function(index) {
            self.alertsData.alerts.splice(index, 1);
        }
    };
    self.dashboardData = {
        noOfCompletedToday : null
    };
    self.trialWorkflowsData = {
        lastTrialsPollPosixTime : 1,
        trialWorkflows : null,
        getCompletedTrialWorkflows: function () {
            return lodash.filter(self.trialWorkflowsData.trialWorkflows, function(tw) {
                return tw.state == 'completed';
            });
        }
    };
    self.workflowsData = {
        lastPollPosixTime : 1,
        newWorkflows : null,
        workflows: null,
        findWorkflowById: function (id) {
            return lodash.find(self.workflowsData.workflows, function(o) {
                return o.id == id;
            });
        },
        updateTitle: function () {
            self.workflowsData.newWorkflows = self.workflowsData.workflows.length;
            if (!!self.workflowsData.newWorkflows) {
                document.title = '(' + self.workflowsData.newWorkflows + ') New invoices';
            } else {
                document.title = 'No new invoices';
            }
        },
        removeWorkflowById: function (id) {
            if (!!self.workflowsData.newWorkflows) {
                self.workflowsData.newWorkflows--;
                self.workflowsData.updateTitle();
             }
             lodash.remove(self.workflowsData.workflows, {
                 id: id
             });
        }
    };
    self.settingsData = {
        allSettings : null
    };
    return self;
}]);

angular.module('RDash').factory('InvoicesPendingPoller', ['$timeout', 'AppData', 'PollWorkflows',
    function($timeout, AppData, PollWorkflows) {
        var self = {};
        self.pollServer = function (firstCall) {
            PollWorkflows.query({locale: 'it', state: 'pending_review', posixTime: AppData.workflowsData.lastPollPosixTime}).$promise.then(
                function(result){
                    angular.forEach(result.invoiceWorkflows, function(value) {
                        if (!!value.extractedData.ISSUE_DATE) {
                            value.extractedData.ISSUE_DATE = new Date(value.extractedData.ISSUE_DATE);
                        }
                        if (!!value.extractedData.VATIN) {
                            value.selectedVatin = value.extractedData.VATIN[0];
                        }
                    });
                    if (firstCall) {
                        AppData.workflowsData.workflows = result.invoiceWorkflows;
                    } else {
                        angular.forEach(result.invoiceWorkflows, function(value) {
                            AppData.workflowsData.workflows.push(value);
                        });
                    }
                    AppData.workflowsData.lastPollPosixTime = result.lastPollTime;
                    AppData.workflowsData.updateTitle();
                    $timeout(function () {
                        self.pollServer(false);
                    }, 10000);
                }
            );
        };
        return self;
    }]);

angular.module('RDash').factory('InvoicesTrialPoller', ['$timeout', 'lodash', 'AppData', 'PollTrials',
    function($timeout, lodash, AppData, PollTrials) {
        var self = {};
        self.pollServer = function (firstCall) {
            PollTrials.query({locale: 'it', posixTime: AppData.trialWorkflowsData.lastTrialsPollPosixTime}).$promise.then(
                function (result) {
                    angular.forEach(result.invoiceWorkflows, function (value) {
                        if (!!value.extractedData.ISSUE_DATE) {
                            value.extractedData.ISSUE_DATE = new Date(value.extractedData.ISSUE_DATE);
                        }
                        if (!!value.extractedData.VATIN) {
                            value.selectedVatin = value.extractedData.VATIN[0];
                        }
                    });
                    if (firstCall) {
                        AppData.trialWorkflowsData.trialWorkflows = result.invoiceWorkflows;
                    } else {
                        angular.forEach(result.invoiceWorkflows, function (value) {
                            lodash.remove(AppData.trialWorkflowsData.trialWorkflows, {
                                id: value.id
                            });
                            AppData.trialWorkflowsData.trialWorkflows.push(value);
                        });
                    }
                    AppData.trialWorkflowsData.lastTrialsPollPosixTime = result.lastPollTime;
                    $timeout(function () {
                        self.pollServer(false);
                    }, 10000);
                });
        };
        return self;
    }]);

angular.module('RDash').factory('Bootstrapper', ['$http', '$httpParamSerializerJQLike', '$state', 'AppData', 'InvoicesPendingPoller', 'InvoicesTrialPoller',
    function($http, $httpParamSerializerJQLike, $state, AppData, InvoicesPendingPoller, InvoicesTrialPoller) {
        var self = {};
        self.processBootstrapData = function (data) {
            if (!!data && !!data.username) {
                AppData.loginData.login(data);
                if (AppData.loginData.isReviewer) {
                    InvoicesPendingPoller.pollServer(true);
                }
                if (AppData.loginData.isClient) {
                    InvoicesTrialPoller.pollServer(true);
                }
                $state.go("dashboard");
            } else {
                AppData.loginData.showError = true;
                $state.go("login");
            }
        };
        return self;
    }]);

angular.module('RDash').factory('Authenticator', ['$http', '$httpParamSerializerJQLike', '$state', 'AppData', 'Bootstrapper', 'InvoicesPendingPoller', 'InvoicesTrialPoller',
    function($http, $httpParamSerializerJQLike, $state, AppData, Bootstrapper, InvoicesPendingPoller, InvoicesTrialPoller) {
        var self = {};
        self.login = function (credentials) {
            var data = $httpParamSerializerJQLike(credentials);
            var config = {
                headers : {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8;'
                }
            };
            $http.post('/rest/security_check', data, config)
                .success(function (data, status, headers, config) {
                    Bootstrapper.processBootstrapData(data);
                });
        };
        return self;
    }]);





