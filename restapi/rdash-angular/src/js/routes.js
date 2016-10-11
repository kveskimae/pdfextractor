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

'use strict';

/**
 * Route configuration for the RDash module.
 */
angular.module('RDash').config(['$stateProvider', '$urlRouterProvider',
    function ($stateProvider, $urlRouterProvider) {

        // For unmatched routes
        $urlRouterProvider.otherwise('/');

        // Application routes
        $stateProvider
            .state('client-edit', {
                url: '/client-edit/:id',
                templateUrl: 'templates/client-edit.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_CLIENT_EDIT' | translate }}",
                    parent: 'clients'
                }
            })
            .state('client-edit-new', {
                url: '/client-edit-new',
                templateUrl: 'templates/client-edit.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_CLIENT_EDIT_NEW' | translate }}",
                    parent: 'clients'
                }
            })
            .state('client-stats', {
                url: '/client-stats/:id',
                templateUrl: 'templates/client-stats.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_CLIENT_STATS' | translate }}",
                    parent: 'clients'
                }
            })
            .state('clients', {
                url: '/clients',
                templateUrl: 'templates/clients.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_CLIENTS' | translate }}"
                }
            })
            .state('dashboard', {
                url: '/dashboard',
                templateUrl: 'templates/dashboard.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_DASHBOARD' | translate }}"
                }
            })
            .state('invoice-review', {
                url: '/invoice-review/:id',
                templateUrl: 'templates/invoice-review.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_INVOICE_REVIEW' | translate }}",
                    parent: 'invoices-pending'
                }
            })
            .state('invoices-pending', {
                url: '/invoices-pending',
                templateUrl: 'templates/invoices-pending.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_INVOICES_PENDING' | translate }}"
                }
            })
            .state('invoices-trial', {
                url: '/invoices-trial',
                templateUrl: 'templates/invoices-trial.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_INVOICES_TRIAL' | translate }}"
                }
            })
            .state('login', {
                url: '/login',
                templateUrl: 'templates/login.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_LOGIN' | translate }}"
                }
            })
            .state('phrases', {
                url: '/phrases',
                templateUrl: 'templates/phrases.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_PHRASES' | translate }}",
                    parent: 'settings'
                }
            })
            .state('profile', {
                url: '/profile',
                templateUrl: 'templates/profile.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_PROFILE' | translate }}"
                }
            })
            .state('profile-email', {
                url: '/profile-email',
                templateUrl: 'templates/profile-email.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_PROFILE_EMAIL' | translate }}",
                    parent: 'profile'
                }
            })
            .state('profile-password', {
                url: '/profile-password',
                templateUrl: 'templates/profile-password.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_PROFILE_PASSWORD' | translate }}",
                    parent: 'profile'
                }
            })
            .state('settings', {
                url: '/settings',
                templateUrl: 'templates/settings.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_SETTINGS' | translate }}"
                }
            })
            .state('upload', {
                url: '/upload',
                templateUrl: 'templates/upload.html',
                ncyBreadcrumb: {
                    label: "{{ 'BREADCRUMB_UPLOAD' | translate }}"
                }
            })
        ;
    }
]);

angular.module('RDash').config(['$translateProvider',
    function ($translateProvider) {
        $translateProvider.translations('en', {
            // language names
            BUTTON_LANG_EN: 'english',
            BUTTON_LANG_IT: 'italian',

            // breadcrumbs
            BREADCRUMB_CLIENT_EDIT: 'Edit client',
            BREADCRUMB_CLIENT_EDIT_NEW: 'Add client',
            BREADCRUMB_CLIENT_STATS: 'Client history',
            BREADCRUMB_CLIENTS: 'Clients',
            BREADCRUMB_DASHBOARD: 'Home',
            BREADCRUMB_INVOICE_REVIEW: 'Review invoice',
            BREADCRUMB_INVOICES_PENDING: 'Pending invoices',
            BREADCRUMB_INVOICES_TRIAL: 'Trial invoices',
            BREADCRUMB_LOGIN: 'Login',
            BREADCRUMB_PHRASES: 'Phrases',
            BREADCRUMB_PROFILE: 'Profile',
            BREADCRUMB_PROFILE_EMAIL: 'Change email',
            BREADCRUMB_PROFILE_PASSWORD: 'Change password',
            BREADCRUMB_SETTINGS: 'Settings',
            BREADCRUMB_UPLOAD: 'Upload',

            // menu
            HEADER_BACK_OFFICE: 'Back Office',
            MENU_NAVIGATION: 'NAVIGATION',
            MENU_ITEM_DASHBOARD: 'Dashboard',
            MENU_ITEM_REVIEW: 'Review',
            MENU_ITEM_CLIENTS: 'Clients',
            MENU_ITEM_SETTINGS: 'Settings',
            MENU_ITEM_UPLOAD: 'Upload trial invoice',
            MENU_ITEM_VIEW_UPLOADED: 'View uploaded trials',
            MENU_ITEM_PROFILE: 'Profile',
            MENU_ITEM_LOGOUT: 'Logout',
            MENU_ITEM_LOGIN: 'Log in',

            // buttons
            SAVE_BUTTON: 'Save',
            DELETE_BUTTON: 'Delete',
            CANCEL_BUTTON: 'Cancel',
            BACK_BUTTON: 'Back',
            ADD_BUTTON: 'Add',
            QUERY_BUTTON: 'Query',
            VIEW_BUTTON: 'View',
            CHANGE_BUTTON: 'Change',
            ADD_CLIENT_BUTTON: 'Add client',
            REVIEW_BUTTON: 'Review',
            LOGIN_BUTTON: 'Log in',
            RELOAD_BUTTON: 'Reload',
            PHRASES_BUTTON: 'Phrases',
            CHOOSE_FILE_BUTTON: 'Choose file',
            // EDIT_BUTTON: 'Edit', use change

            // common labels
            USERNAME_LABEL: 'Username',
            PASSWORD_LABEL: 'Password',
            EMAIL_LABEL: 'Email',
            ENABLED_LABEL: 'Enabled',
            NEW_EMAIL_LABEL: 'New email',

            // common tooltips
            EDIT_PASSWORD_TOOLTIP: 'Password must be at least 5 characters, and must include at least one upper case letter, one lower case letter, and one numeric digit.',

            // client-edit
            CLIENT_EDIT_HEADER_TEXT_ADD:'Add new client',
            CLIENT_EDIT_HEADER_TEXT_CHANGE: 'Change client with ID ',
            CLIENT_EDIT_USERNAME_TOOLTIP: 'Contains 3-20 letters and numbers, underscore _ is allowed',
            CLIENT_EDIT_ACCOUNT_TYPE_LABEL: 'Account type',
            CLIENT_EDIT_ACCOUNT_TYPE_OPTION_TRIAL: 'Trial',
            CLIENT_EDIT_ACCOUNT_TYPE_OPTION_REGULAR: 'Regular',
            CLIENT_EDIT_TRIAL_LIMIT_LABEL: 'Trial limit',

            // client-stats
            CLIENT_STATS_HEADER_TEXT_START: 'Period statistics for user',
            CLIENT_STATS_START_DATE_LABEL: 'Start date (yyyy-MM-dd)',
            CLIENT_STATS_END_DATE_LABEL: 'End date (yyyy-MM-dd)',
            CLIENT_STATS_PERIOD_TOTAL: 'Period total:',
            CLIENT_STATS_BOX_TOTAL: 'Total',
            CLIENT_STATS_BOX_TOTAL_TOOLTIP: 'This is the total number of invoices that have been sent by this customer (and not failed).',
            CLIENT_STATS_BOX_THIS_MONTH_TOOLTIP: 'This is the number of invoices that were sent by this customer during current month.',
            CLIENT_STATS_BOX_THIS_MONTH: 'This month',

            // clients
            CLIENTS_HEADER_TEXT: 'Clients',
            CLIENTS_HEADER_TOOLTIP: 'These are the companies that can send invoices to us',
            CLIENTS_ADD_CLIENT_BUTTON_TOOLTIP: 'You can add new clients from here.',
            CLIENTS_USERNAME: 'Username',
            CLIENTS_USERNAME_TOOLTIP: "This is the client's login username.",
            CLIENTS_ACCOUNT_TYPE: 'Account type',
            CLIENTS_ACCOUNT_TYPE_TOOLTIP: "This is the client's account type.",
            CLIENTS_ENABLED: 'Enabled',
            CLIENTS_ENABLED_TOOLTIP: 'This shows if the account is active.',

            // dashboard
            DASHBOARD_TOTAL_TODAY: 'Today',
            DASHBOARD_TOTAL_TODAY_TOOLTIP: 'This is the total number of invoices that have been reviewed and set to complete state during today.',

            // invoice-review
            INVOICE_REVIEW_NAME_LABEL: 'Name',
            INVOICE_REVIEW_TOTAL_BEFORE_TAXES_LABEL: 'Total before taxes',
            INVOICE_REVIEW_TOTAL_BEFORE_TAXES_TOOLTIP: "Note: Decimal point (radix character) is dot symbol '.'",
            INVOICE_REVIEW_TOTAL_LABEL: 'Total',
            INVOICE_REVIEW_TOTAL_TOOLTIP: "Note: Decimal point (radix character) is dot symbol '.'",
            INVOICE_REVIEW_INVOICE_ID_LABEL: 'Invoice ID',
            INVOICE_REVIEW_ISSUE_DATE_LABEL: 'Issue date (yyyy-MM-dd)',
            INVOICE_REVIEW_VATIN_LABEL: 'VATIN',
            INVOICE_REVIEW_ADD_VATIN_LABEL: 'Add new VATIN',
            INVOICE_REVIEW_STATE_LABEL: 'State',

            // invoices-pending
            INVOICES_PENDING_HEADER_TEXT: 'Review pending invoices',
            INVOICES_PENDING_TABLE_HEADER_FILE: 'File',
            INVOICES_PENDING_TABLE_HEADER_NAME: 'Name',
            INVOICES_PENDING_TABLE_HEADER_INVOICE_ID: 'Invoice ID',

            // invoices-trial
            INVOICES_TRIAL_HEADER_TEXT: 'Trial invoices',
            INVOICES_TRIAL_TABLE_HEADER_FILE: 'File',
            INVOICES_TRIAL_TABLE_HEADER_STATE: 'State',
            INVOICES_TRIAL_TABLE_HEADER_NAME: 'Name',
            INVOICES_TRIAL_TABLE_HEADER_INVOICE_ID: 'Invoice ID',
            INVOICES_TRIAL_TABLE_HEADER_TOTAL_BEFORE_TAXES: 'Total before taxes',
            INVOICES_TRIAL_TABLE_HEADER_TOTAL: 'Total',
            INVOICES_TRIAL_TABLE_HEADER_ISSUE_DATE: 'Issue date',
            INVOICES_TRIAL_TABLE_HEADER_VATIN: 'VATIN',

            // login
            LOGIN_HEADER_TEXT: 'Log in',
            LOGIN_USERNAME_PLACEHOLDER: 'Enter Username',
            LOGIN_PASSWORD_PLACEHOLDER: 'Enter Password',
            LOGIN_MSG_TRY_AGAIN: 'Login failed. Please try again.',

            // phrases
            PHRASES_HEADER_TEXT: 'Phrases',
            PHRASES_RELOAD_BUTTON_TOOLTIP: 'The changes will be saved every time, but will only take effect in the real extraction after reloading. Do all your desired changes and then reload.',
            PHRASES_PHRASE_FIELD_LABEL: 'Phrase',
            PHRASES_PHRASE_FIELD_LABEL_TOOLTIP: "This phrase is for searching field. Letter case is irrelevant, so you can write in upper ('TOTALE'), lower ('totale') or mixed ('ToTalE') case. All Java regular expression rules apply, which you can try for example at regex101.com . For example, optional dot can be written as '[.]{0,1}', which means the dot in square brackets either zero or one time. So phrase 'S[.]{0,1}r[.]{0,1}l[.]{0,1}' matches both 'srl' and 's.r.l.'.",
            PHRASES_WEIGHT_FIELD_LABEL: 'Weight',
            PHRASES_WEIGHT_FIELD_LABEL_TOOLTIP: "The phrase with bigger weight has a preference when comparing different candidates. For example, let us say that there are two different numeric values for total. They are otherwise the same but one is after 'importo totale' with weight 5 and the other is after 'totale' with weight 1. Then the one after 'importo totale' will be chosen.",
            PHRASES_EMPTY_COLUMN_TOOLTIP: "Please note that changing weight changes the location of the selected row in table. The table is sorted by weight in descending order.",
            PHRASES_ADD_BUTTON_TOOLTIP: 'You can add new phrases from here.',

            // settings
            SETTINGS_HEADER_TEXT: 'Settings',
            SETTINGS_HEADER_TOOLTIP: 'These are the settings that impact the work of application',
            SETTINGS_RELOAD_BUTTON_TOOLTIP: 'The changes will be saved every time, but will only take effect in the real extraction after reloading. Do all your desired changes and then reload.',
            SETTINGS_TABLE_HEADER_SETTING_TYPE: 'Setting',
            SETTINGS_TABLE_HEADER_SETTING_TYPE_TOOLTIP: 'This is the setting type.',
            SETTINGS_TABLE_HEADER_SETTING_VALUE: 'Value',
            SETTINGS_TABLE_HEADER_SETTING_VALUE_TOOLTIP: 'This is the actual value for the setting.',

            // upload
            UPLOAD_HEADER_TEXT: 'Upload your trial invoices from here',
            UPLOAD_ONLY_PDF: 'Only PDF invoices',

            // profile
            PROFILE_HEADER_TEXT_START: 'My profile: ',
            PROFILE_EDIT_CHANGE_PASSWORD: 'My password',
            PROFILE_EDIT_CHANGE_EMAIL: 'Change email',
            PROFILE_EDIT_YOUR_ROLES: 'My roles',
            PROFILE_EDIT_ADMIN: 'ADMIN',
            PROFILE_EDIT_REVIEWER: 'Reviewer',
            PROFILE_EDIT_CLIENT: 'Client',
            PROFILE_EDIT_EXISTING_EMAIL: 'My email',
            PROFILE_TRIALS: 'My trials',

            // profile-email
            PROFILE_EMAIL_HEADER_TEXT: 'Change email',

            // profile-password
            PROFILE_PASSWORD_HEADER_TEXT: 'Change password',
            PROFILE_PASSWORD_NEW_PASSWORD_LABEL: 'New password',
            PROFILE_PASSWORD_RETYPE_NEW_PASSWORD_LABEL: 'Retype new password',
            PROFILE_PASSWORD_OLD_PASSWORD_LABEL: 'Old password'
        });
        $translateProvider.translations('it', {
            // language names
            BUTTON_LANG_EN: 'inglese',
            BUTTON_LANG_IT: 'italiano'
        });
        $translateProvider.preferredLanguage('en');
    }
]);