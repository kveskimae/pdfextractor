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

/**
 * Master Controller
 */

angular.module('RDash')
    .controller('MasterCtrl', ['$cookieStore', '$rootScope', '$scope', '$translate', 'AppData', 'BootstrapData', 'Bootstrapper', 'Reloader', MasterCtrl]);

function MasterCtrl($cookieStore, $rootScope, $scope, $translate, AppData, BootstrapData, Bootstrapper, Reloader) {
    // TODO query bootstrap data and if not found, then go into login state
    var self = this;
    self.usernamePattern = '\\w{3,20}';
    self.passwordPattern = '^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{5,}$';
    if (!AppData.loginData.loggedIn) {
        BootstrapData.get().$promise.then(function (data) {
            Bootstrapper.processBootstrapData(data);
        });
    }
    $rootScope.AppData = AppData;
    $rootScope.reload = function () {
        Reloader.reload().$promise.then(function() {
            AppData.alertsData.addSuccessAlert('Thank you for the updates! Application was reloaded successfully.');
        });
    };
    /**
     * Sidebar Toggle & Cookie Control
     */
    var mobileView = 992;
    $scope.getWidth = function() {
        return window.innerWidth;
    };
    $scope.$watch($scope.getWidth, function(newValue, oldValue) {
        if (newValue >= mobileView) {
            if (angular.isDefined($cookieStore.get('toggle'))) {
                $scope.toggle = ! $cookieStore.get('toggle') ? false : true;
            } else {
                $scope.toggle = true;
            }
        } else {
            $scope.toggle = false;
        }

    });
    $scope.toggleSidebar = function() {
        $scope.toggle = !$scope.toggle;
        $cookieStore.put('toggle', $scope.toggle);
    };

    window.onresize = function() {
        $scope.$apply();
    };
    $scope.changeLanguage = function (key) {
        $translate.use(key);
    };
}
