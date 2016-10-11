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
    .controller('ProfileEmailCtrl', ['$state', '$stateParams', '$translate', 'AppData', 'ClientEdit', 'Clients', 'lodash', 'ProfileEmailEdit', 'ProfilePasswordEdit', ProfileEmailCtrl]);
function ProfileEmailCtrl($state, $stateParams, $translate, AppData, ClientEdit, Clients, lodash, ProfileEmailEdit, ProfilePasswordEdit) {
    var self = this;
    self.emailData = {
        newEmail: null,
        passwordForChangingEmail: null,
        reset: function () {
            self.emailData.newEmail = null;
            self.emailData.passwordForChangingEmail = null;
        }
    };
    self.cancelEditingEmail = function () {
        self.emailData.reset();
        $state.go("profile");
    };
    self.saveEmail = function () {
        if (!!self.emailData.newEmail && !!self.emailData.passwordForChangingEmail) {
            ProfileEmailEdit.put({}, {
                newEmail: self.emailData.newEmail,
                passwordForChangingEmail: self.emailData.passwordForChangingEmail
            }).$promise.then(
                function () {
                    AppData.alertsData.addSuccessAlert('Email updated successfully.');
                    AppData.loginData.email = self.emailData.newEmail;
                    self.emailData.reset();
                    $state.go("profile");
                });
        }
    };
}