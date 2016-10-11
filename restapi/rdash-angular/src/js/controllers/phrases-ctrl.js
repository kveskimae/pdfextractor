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

angular.module('RDash').controller('PhrasesCtrl',
    ['lodash', 'PhraseEdit', 'Phrases', PhrasesCtrl]);
function PhrasesCtrl(lodash, PhraseEdit, Phrases) {
    var self = this;
    self.phraseToAdd = {
        keyPhrase: null,
        comparisonPart: null
    };
    self.selectedPhrase = null;
    self.locale = {
        selectedLocale: null
    };
    self.paymentType = {
        selectedPaymentType : null
    };
    self.locales = [
        {
            "id": "it",
            "label": "Italy"
        },
        {
            "id": "et",
            "label": "Estonia"
        }];
    self.paymentFieldTypes =
    {
        "et": [
            {
                "id": "total",
                "label": "Total"
            },
            {
                "id": "name",
                "label": "Name"
            },
            {
                "id": "invoice_id",
                "label": "Invoice ID"
            }
        ],
        "it": [
            {
                "id": "total",
                "label": "Total"
            },
            {
                "id": "total_before_taxes",
                "label": "Total before taxes"
            },
            {
                "id": "name",
                "label": "Name"
            },
            {
                "id": "invoice_id",
                "label": "Invoice ID"
            }
        ]
    };
    self.activePaymentFieldTypes = [];
    self.updatePhrases = function () {
        self.phrases = Phrases.get({locale:self.locale.selectedLocale, paymentFieldType: self.paymentType.selectedPaymentType});
    };
    self.setSelectedPhrase = function (phrase) {
        self.selectedPhrase = phrase;
    };
    self.savePhrase = function (phrase) {
        var clone = lodash.clone(phrase);
        PhraseEdit.update({id:clone.id}, clone);
        self.selectedPhrase = null;
    };
    self.deletePhrase = function (phrase) {
        PhraseEdit.delete({id:phrase.id});
        self.selectedPhrase = null;
        lodash.remove(self.phrases, {
            id: phrase.id
        });
    };
    self.updatePhraseType = function () {
        self.updatePhrases();
    };
    self.updateLanguage = function () {
        self.paymentType.selectedPaymentType = null;
        self.activePaymentFieldTypes = self.paymentFieldTypes[self.locale.selectedLocale];
    };
    self.addNewPhrase = function () {
        if (!!self.phraseToAdd.keyPhrase || !!self.phraseToAdd.comparisonPart) {
            // self.phraseToAdd.locale = self.locale;
            // self.phraseToAdd.paymentFieldType = self.paymentType;
            // var clone = lodash.clone(self.phraseToAdd);
            Phrases.post({locale:self.locale.selectedLocale, paymentFieldType: self.paymentType.selectedPaymentType},
                self.phraseToAdd
                // clone
            );
            self.phrases.push(self.phraseToAdd);
            self.phraseToAdd = {
                keyPhrase: null,
                comparisonPart: null
            };
        }
    };
};

