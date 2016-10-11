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
    .controller('ClientEditCtrl', ['$state', '$stateParams', '$translate', 'AppData', 'ClientEdit', 'Clients', 'lodash', ClientEditCtrl]);
function ClientEditCtrl($state, $stateParams, $translate, AppData, ClientEdit, Clients, lodash) {
    var self = this;
    self.id = $stateParams.id;
    self.isAddMode = !self.id;
    // var currentLang = $translate.proposedLanguage() || $translate.use();
    self.titleKey = self.isAddMode ? 'CLIENT_EDIT_HEADER_TEXT_ADD' : 'CLIENT_EDIT_HEADER_TEXT_CHANGE';
    self.titleText = $translate.instant(self.titleKey);
    if (!self.isAddMode) {
        self.titleText = self.titleText + self.id;
    }
    // self.titleText = self.isAddMode ? 'Add new client' : 'Change client with ID ' + self.id;
    self.generateNewClient = function () {
        var ret = {
            username: null,
            password: null,
            email: null,
            enabled: true,
            accountType: 'TRIAL',
            trialLimit: 10
        };
        return ret;
    };
    if (self.isAddMode) {
        self.client = self.generateNewClient();
    } else {
        self.client = lodash.find(AppData.otherUsersData.clients, function (o) {
            return o.id == self.id;
        });
    }
    self.cancelEditing = function () {
        $state.go("clients");
    };
    self.saveClient = function (client) {
        if (!!self.client.username && !!self.client.password && !!self.client.accountType) {
            if (self.isAddMode) {
                Clients.post({authority: "client"}, self.client).$promise.then(
                    function (data) {
                        AppData.otherUsersData.clients.push(data);
                        self.client = self.generateNewClient();
                        $state.go("clients");
                    });
            } else {
                var clone = lodash.clone(self.client);
                ClientEdit.put({id: clone.id}, clone).$promise.then(function () {
                    self.client = null;
                    $state.go("clients");
                });
            }
        }
    };
}