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
    .controller('InvoiceReviewCtrl', ['$state', '$stateParams', 'AppData', 'lodash', 'UpdateWorkflow', InvoiceReviewCtrl]);
function InvoiceReviewCtrl($state, $stateParams, AppData, lodash, UpdateWorkflow) {
    var self = this;
    self.id = $stateParams.id;
    self.workflow = AppData.workflowsData.findWorkflowById(self.id);
    self.getIframeSrc = function (fileName) {
        return '/rest/invoicefile/' + fileName;
    };
    self.newvatin = {
        value : null
    };
    self.cancelEditing = function () {
        $state.go("invoices-pending");
    };
    self.addVatin = function() {
        if (!!self.newvatin.value) {
            self.workflow.extractedData.VATIN.push(self.newvatin.value);
            self.newvatin.value = null;
        }
    };
    self.saveWorkflow = function (workflow) {
        var clone = lodash.clone(workflow);
        if (!!clone.selectedVatin) {
            var index = clone.extractedData.VATIN.indexOf(clone.selectedVatin);
            if (index > -1) {
                clone.extractedData.VATIN.splice(index, 1);
                clone.extractedData.VATIN.unshift(clone.selectedVatin);
            }
            delete clone.selectedVatin;
        }
        UpdateWorkflow.update({id:clone.id}, clone).$promise.then(
            function () {
                if (clone.state != 'pending_review') {
                    AppData.workflowsData.removeWorkflowById(clone.id);
                }
                self.workflow = null;
                $state.go("invoices-pending");
            }
        );
    };
}