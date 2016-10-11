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
    .controller('ProfilePasswordCtrl', ['$state', '$stateParams', '$translate', 'AppData', 'ClientEdit', 'Clients', 'lodash', 'ProfileEmailEdit', 'ProfilePasswordEdit', ProfilePasswordCtrl]);
function ProfilePasswordCtrl($state, $stateParams, $translate, AppData, ClientEdit, Clients, lodash, ProfileEmailEdit, ProfilePasswordEdit) {
    var self = this;
    self.passwordData = {
        newPassword: null,
        retypedNewPassword: null,
        oldPassword: null,
        reset: function () {
            self.passwordData.newPassword = null;
            self.passwordData.retypedNewPassword = null;
            self.passwordData.oldPassword = null;
        }
    };
    self.cancelEditingPassword = function () {
        self.passwordData.reset();
        $state.go("profile");
    };
    self.savePassword = function () {
        if (!!self.passwordData.newPassword && !!self.passwordData.oldPassword) {
            ProfilePasswordEdit.put({}, {newPassword: self.passwordData.newPassword, oldPassword: self.passwordData.oldPassword}).$promise.then(
                function () {
                    AppData.alertsData.addSuccessAlert('Password updated successfully.');
                    self.passwordData.reset();
                    $state.go("profile");
                });
        }
    };
}