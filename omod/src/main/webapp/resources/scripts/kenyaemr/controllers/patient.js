/**
 * Controller for patient search results
 */
kenyaemrApp.controller('AdvancedPatientSearchResults', ['$scope', '$http', function($scope, $http) {

    $scope.query = '';
    $scope.results = [];
    $scope.showLoader = false;
    $scope.checkedInSelected = false;
    $scope.allSelected = false;

    /**
     * Initializes the controller
     * @param appId the current app id
     * @param which
     */
    $scope.init = function(appId, pageProvider, page) {
        $scope.appId = appId;
        $scope.pageProvider = pageProvider;
        $scope.page = page;
    };

    /**
     * Listens for the 'patient-search' event
     */
    $scope.$on('patient-search', function(event, data) {
        $scope.query = data.query;
        $scope.which = data.which;
        $scope.showLoader = true;
        $scope.refresh();
    });

    /**
     * Refreshes the person search
     */
    $scope.refresh = function() {
        if(!$scope.query && $scope.which === "checked-in") {
            $scope.allSelected= false;
            $scope.results = [];
            $scope.showLoader = false;
        }

        if($scope.query && $scope.which === "checked-in") {
            $scope.crossborder = false;
            $scope.checkedInSelected = true;
            $scope.allSelected = false;
            $http.get(ui.fragmentActionLink('kenyaemr', 'search', 'patients', { appId: $scope.appId, q: $scope.query, which: $scope.which })).
            success(function(data) {
                if($scope.checkedInSelected) {
                    $scope.results =  data;
                    $scope.showLoader = false;
                }
            });
        }

        if(!$scope.query && $scope.which === "all") {
            $scope.crossborder = false;
            $scope.checkedInSelected= false;
            $scope.results = [];
            $scope.showLoader = false;
        }

        if($scope.query && $scope.which === "all") {
            $scope.crossborder = false;
            $scope.checkedInSelected = false;
            $scope.allSelected = true;
            $http.get(ui.fragmentActionLink('kenyaemr', 'search', 'patients', { appId: $scope.appId, q: $scope.query, which: $scope.which })).
            success(function(data) {
                if($scope.allSelected) {
                    $scope.results =  data;
                    $scope.showLoader = false;
                }
            });
        }

        if($scope.query && $scope.which === "cross-border") {
            $scope.crossborder = true;
            $scope.checkedInSelected = false;
            $scope.allSelected = false;
            $http.get(ui.fragmentActionLink('crossborder2', 'search', 'patients', { appId: $scope.appId, q: $scope.query, which: $scope.which })).
            success(function(data) {
                if($scope.allSelected) {
                    $scope.results =  data;
                    $scope.showLoader = false;
                }
            });
        }

    };

    /**
     * Result click event handler
     * @param patient the clicked patient
     */
    $scope.onResultClick = function(patient) {
        ui.navigate($scope.pageProvider, $scope.page, { patientId: patient.id });
    };

}]);
