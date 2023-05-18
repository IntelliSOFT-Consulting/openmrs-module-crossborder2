kenyaemrApp.directive('ngEnter', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if(event.which === 13) {
                scope.$apply(function (){
                    scope.$eval(attrs.ngEnter);
                });

                event.preventDefault();
            }
        });
    };
});

kenyaemrApp.service('PatientService', function ($rootScope) {

    /**
     * Broadcasts new patient search parameters
     */
    this.updateSearch = function(which, cbIdQuery, clinicNoQuery, firstNameQuery, middleNameQuery, lastNameQuery, selectMpiQuery, mpiQuery) {

        $rootScope.$broadcast('patient-search', { which: which , cbIdQuery: cbIdQuery, clinicNoQuery: clinicNoQuery, firstNameQuery: firstNameQuery, middleNameQuery: middleNameQuery, lastNameQuery: lastNameQuery, selectMpiQuery: selectMpiQuery, mpiQuery: mpiQuery});
    };
});

/**
 * Controller for patient search form
 */
kenyaemrApp.controller('AdvancedPatientSearchForm', ['$scope', 'PatientService','$timeout', function($scope, patientService, $timeout) {

    $scope.query = [];

    $scope.init = function(which) {
        $scope.which = which;
        $scope.$evalAsync($scope.updateSearch); // initiate an initial search
    };
    $scope.delayOnChange = (function() {
        var promise = null;
        return function(callback, ms) {
            $timeout.cancel(promise); //clearTimeout(timer);
            promise = $timeout(callback, ms); //timer = setTimeout(callback, ms);
        };
    })();

    $scope.onQueryChange = function() {
        if($scope.query === '') {
            $scope.updateSearch();
        }
    };

    $scope.updateSearch = function() {
        patientService.updateSearch($scope.which, $scope.cbIdQuery, $scope.clinicNoQuery, $scope.firstNameQuery, $scope.middleNameQuery, $scope.lastNameQuery, $scope.selectMpiQuery, $scope.mpiQuery);
    };
}]);

/**
 * Controller for patient search results
 */
kenyaemrApp.controller('AdvancedPatientSearchResults', ['$scope', '$http', function($scope, $http) {

    $scope.query = [];
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
        $scope.which = data.which;
        $scope.clinicNoQuery = data.clinicNoQuery;
        $scope.firstNameQuery = data.firstNameQuery;
        $scope.cbIdQuery = data.cbIdQuery;
        $scope.lastNameQuery = data.lastNameQuery;
        $scope.middleNameQuery = data.middleNameQuery;
        $scope.mpiQuery = data.mpiQuery;
        $scope.selectMpiQuery = data.selectMpiQuery;

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
            $scope.searchObject = [];
            $scope.query = [];

            if ($scope.cbIdQuery) {
                $scope.query.push({"cbId": $scope.cbIdQuery});
            }
            if ($scope.clinicNoQuery) {
                $scope.query.push({"clinicNo": $scope.clinicNoQuery});
            }
            if ($scope.firstNameQuery) {
                $scope.query.push({"firstName": $scope.firstNameQuery});
            }
            if ($scope.lastNameQuery) {
                $scope.query.push({"lastName": $scope.lastNameQuery});
            }
            if ($scope.middleNameQuery) {
                $scope.query.push({"middleName": $scope.middleNameQuery});
            }
            if ($scope.mpiQuery) {
                $scope.query.push({"mpi": $scope.mpiQuery});
            }

            $http.get(ui.fragmentActionLink('crossborder2', 'advancedSearch', 'patients', { appId: $scope.appId, q: JSON.stringify($scope.query), which: $scope.which })).
            success(function(data) {
                if($scope.crossborder) {
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
