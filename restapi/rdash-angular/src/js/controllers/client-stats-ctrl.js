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

angular
    .module('RDash')
    .controller('ClientStatsCtrl', ['$state', '$stateParams', 'AppData', 'ClientStats', 'ClientStatsPeriod', ClientStatsCtrl]);
function ClientStatsCtrl($state, $stateParams, AppData, ClientStats, ClientStatsPeriod) {
    var self = this;
    self.id = $stateParams.id;
    self.totalNumber = null;
    self.totalThisMonth = null;
    self.totalBetweenDates = null;
    self.periodDates = {
        startDate: null,
        endDate: null
    };
    self.client = AppData.otherUsersData.findClientById(self.id);
    self.cancelEditing = function () {
        $state.go("clients");
    };
    self.convertDateToUTC = function(d) {
        d.setTime(d.getTime() - d.getTimezoneOffset()*60*1000 );
        return d;
    }
    self.queryBetweenDates = function () {
        self.periodDates.startDate = self.convertDateToUTC(self.periodDates.startDate);
        self.periodDates.endDate = self.convertDateToUTC(self.periodDates.endDate);
        var start = self.periodDates.startDate.toISOString().substring(0, 10);
        var end = self.periodDates.endDate.toISOString().substring(0, 10);
        ClientStatsPeriod.query({id:self.id, start:start, end:end}).$promise.then(
            function (result) {
                self.totalBetweenDates = result.totalBetweenDates;
            }
        );
    };
    ClientStats.query({id:self.id}).$promise.then(
        function (result) {
            self.totalNumber = result.totalNumber;
            self.totalThisMonth = result.totalThisMonth;
        }
    );
}